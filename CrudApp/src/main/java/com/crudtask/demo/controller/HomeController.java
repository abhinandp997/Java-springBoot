package com.crudtask.demo.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.crudtask.demo.entity.User;
import com.crudtask.demo.service.UserService;

@Controller
public class HomeController {

    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/")
    public String showHome(Authentication authentication, Model model, @ModelAttribute("user")User user){

        String username = authentication.getName();

        User theUser = userService.findByUserName(username);

        model.addAttribute("user",theUser);
        return "home";
    }


    @GetMapping("/list")
    public String showList(Model model){

        List<User> users = userService.findAllUser();
        model.addAttribute("users", users);
        return "list";
    }

    

}
