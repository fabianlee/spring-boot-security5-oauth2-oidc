package org.fabianlee.springsecurityoauth2client.oauth2;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class OAuth2Controller {
	
    // template engine, used for text templates
    @Autowired
    protected SpringTemplateEngine mMessageTemplateEngine;
    
    @GetMapping(value="/")
    public String index(Model model) {
    	return "index";
    }
	
    @GetMapping(value="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String user(@AuthenticationPrincipal OAuth2User principal) {
    	
        String theName = null;
        if (principal!=null)
        	theName = principal.getAttribute("name")==null ? principal.getName():principal.getAttribute("name");
        else
        	theName = "";
        
        final Context theContext = new Context();
        theContext.setVariable("principalName",theName);
        theContext.setVariable("principal",principal);
        String theTextMessage =  mMessageTemplateEngine.process("text/user.txt",theContext);
        return theTextMessage;        
    }

    @GetMapping("/id")
	public String userinfo(Model model, @AuthenticationPrincipal OidcUser principal) {

        String theName = principal.getAttribute("name")==null ? principal.getName():principal.getAttribute("name");
        model.addAttribute("name",theName);
        
        log.debug("oidc: " + principal.toString());
        OidcIdToken idToken = principal.getIdToken();
        model.addAttribute("oidc",principal);
        model.addAttribute("id_required_fields",new ArrayList<String>(Arrays.asList("iss","sub","aud","exp","iat","nonce")));

        return "idtoken";
    }

    @RequestMapping("/access")
    //@CrossOrigin
    public String getAccessToken(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        
        if (accessToken!=null) {
        	model.addAttribute("accessToken",accessToken);
	        log.debug("access type: " + accessToken.getTokenType().getValue());
        }else {
        	log.debug("@RegisteredOAuth2AuthorizedClient is null");
        }

        return "accesstoken";
    }

}
