package org.fabianlee.springsecurityoauth2resource.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

        private List<User> userListV1 = new ArrayList<User>(Arrays.asList(new User("moe"), new User("larry"), new User("curly")));
        
        
        @GetMapping(value="/me")
        public Map<String,String> meJSON() {
        	// get logged in user context
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        	JwtAuthenticationToken jwt = (JwtAuthenticationToken)auth;

        	// just for debugging purposes, let's show all claims at console
        	log.debug("auth authorities: " + auth.getAuthorities());
        	log.debug("jwt authorities: " + jwt.getAuthorities());
        	for(Map.Entry<String,Object> entry : jwt.getToken().getClaims().entrySet() ) {
        		log.debug("claim: " + entry.getKey() + "=" + entry.getValue());
        	}

        	// construct Map that can return json object with essential info
            Map<String,String> info = new HashMap<String,String>(){{
            		put("email",jwt.getToken().getClaimAsString("email"));
            		put("authorities",auth.getAuthorities().toString());
            		put("group",jwt.getToken().getClaimAsString("group"));
            		put("scp",jwt.getToken().getClaimAsString("scp"));
            }};

            return info;
        }

        @GetMapping
        public Iterable<User> findAllUsers() {
            log.debug("doing findAllUsers");
            log.info("doing findAllUsers");
            log.warn("doing findAllUsers");
            log.error("doing findAllUsers");
            return userListV1;
        }
        
        @DeleteMapping
        @PreAuthorize("hasRole('managers') && hasAuthority('SCOPE_api_delete')")
        public Iterable<User> deleteUser() {
        	int nusers = userListV1.size();
            log.debug("called deleteUser");
            if (userListV1.size() > 0) {
                userListV1.remove(userListV1.size() - 1);
            }
        	log.debug("deleteUser: #users was " + nusers + " and is now  " + userListV1.size());

            return userListV1;
        }
        
        @GetMapping(value="/engineer")
        @PreAuthorize("hasRole('engineers') || hasRole('managers')")
        public ResponseEntity<List<User>> listEngineers() {
            log.debug("listEngineers");
            
            List<User> users = new ArrayList<User>(
            		Arrays.asList(
            				new User("engineer1"),
            				new User("engineer2")
            				) 
            		);
            return new ResponseEntity<List<User>>(users,HttpStatus.OK);
        }
        
        @GetMapping(value="/manager")
        @PreAuthorize("hasRole('managers')")
        public ResponseEntity<List<User>> listManagers() {
            log.debug("listManagers");
            
            // log user email to show how audit entry could be created for sensitive info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.debug("AUDIT entry, sensitive info requested by: " + ((JwtAuthenticationToken)authentication).getToken().getClaimAsString("email"));
            
            List<User> managers = new ArrayList<User>( 
            		Arrays.asList(
            				new User("manager1")
            				) 
            		);
            return new ResponseEntity<List<User>>(managers,HttpStatus.OK);
        }

        /*      
        // SecurityFilterChain cors() method takes care of OPTIONS being available without authentication  
        @RequestMapping(method = RequestMethod.OPTIONS)
        ResponseEntity<?> singularOptions() 
        {
           return ResponseEntity
               .ok()
               .allow(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.OPTIONS)
                   .build();
        }
*/        
        
}
