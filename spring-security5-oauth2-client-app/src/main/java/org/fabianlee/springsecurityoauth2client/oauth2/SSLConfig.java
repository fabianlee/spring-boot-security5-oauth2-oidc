package org.fabianlee.springsecurityoauth2client.oauth2;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
@Configuration
public class SSLConfig {

	// if we wanted to pull from environmental properties
    //@Autowired
    //private Environment env;
	
	// pull from application.yml
	@Value("${server.ssl.enabled")
	private String serverSSLEnabled;

	@PostConstruct
	private void configureSSL() {
		
		boolean isSSLEnabled = "true".equalsIgnoreCase(serverSSLEnabled);
		log.debug("isSSLEnabled: " + isSSLEnabled);

		// if using SSL for this application, then use 'mycacerts' from classpath
		// if NOT using SSL, then just trust all certs
		//
		// this is specifically for communicating with local OAuth servers like ADFS
		// that may have self-signed or custom CA certificates
		if (isSSLEnabled) {
			File absFile = null;
			try {
				absFile = ResourceUtils.getFile("classpath:mycacerts");
				log.debug("mycacerts classpath real location: " + absFile.exists() + " at " + absFile.toPath());
			} catch (Exception exc) {
			}
			String trustStoreLocation = absFile.toPath().toString(); // "src/main/resources/mycacerts";
			String trustStorePassword = "changeit";
			System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
			System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
			log.debug("now using the 'mycacerts' trustStore from the classpath");
			
		} else {
			
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] { trm }, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception exc) {
				log.error("there was an error setting up the x509TrustManager", exc);
			}
			log.debug("now using all-trusting x509TrustManager");
			
		}

	}

    // trust all certs, NOT FOR PRODUCTION
    // https://stackoverflow.com/questions/1219208/is-it-possible-to-get-java-to-ignore-the-trust-store-and-just-accept-whatever
    TrustManager trm = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    };    
    
} // class
