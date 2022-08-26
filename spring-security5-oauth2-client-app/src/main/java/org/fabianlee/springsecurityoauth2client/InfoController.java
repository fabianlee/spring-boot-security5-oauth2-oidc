package org.fabianlee.springsecurityoauth2client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class InfoController {

    // pull from build.gradle via 'springBoot' directive
    @Autowired
    private BuildProperties buildProperties;

    @RequestMapping(value="/info")
    public String info(Model model) {

    	model.addAttribute("buildProperties",buildProperties);
        return "info";

    }
    
    @GetMapping(value="/infojson")
    public ResponseEntity<Map<String,String>> listEngineers() {
        
        Map<String,String> info = new HashMap<String,String>();
        info.put("version",buildProperties.getVersion());
        info.put("name",buildProperties.getName());
        info.put("group",buildProperties.getGroup());
        
        return new ResponseEntity<Map<String,String>>(info,HttpStatus.OK);
    }
    
   
}

