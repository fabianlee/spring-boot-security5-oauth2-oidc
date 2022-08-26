package org.fabianlee.springsecurityoauth2resource.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OAuth2Controller {
	
    @GetMapping(value="/")
    public String index(Model model) {
    	return "index";
    }
    
    @GetMapping(value="/testjwt")
    public String test(Model model) {
    	model.addAttribute("accessToken","");
    	return "testjwt";
    }
    

}
