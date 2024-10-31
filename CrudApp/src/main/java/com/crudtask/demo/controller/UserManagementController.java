package com.crudtask.demo.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crudtask.demo.entity.User;
import com.crudtask.demo.service.UserService;
import com.crudtask.demo.user.WebUser;

import jakarta.validation.Valid;

@Controller
public class UserManagementController {

    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/addUser")
    public String addUser(Model model){

        model.addAttribute("webUser", new WebUser());

        return "add-user";
    }

    @PostMapping("/addUser/processingTheAddUser")
    public String proccessingTheAddUser(@Valid@ModelAttribute("webUser")WebUser webUser, BindingResult bindingResult, Model model){

        String userName = webUser.getUserName();

        if(bindingResult.hasErrors()){
            return "add-user";
        }

        User user = userService.findByUserName(userName);
        if(user!=null){
            model.addAttribute("webUser",new WebUser());
            model.addAttribute("registrationError","Username Already exists");
            System.out.println("Username already exists....");
            return "add-user";
        }
        userService.save(webUser);
        return "add-confirmation";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, Model model) {
        logger.info("Inside Update User.....");
        try {
            logger.info("Inside Update User try.....");
            User user = userService.findByUserId(id);
            model.addAttribute("user", user);
            return "update-form";
        } catch (Exception e) {
            logger.info("Error loading user for update"+ e);
            model.addAttribute("error", "An error occurred while loading the user profile.");
            return "error-page"; 
        }
    }

    @PostMapping("/update/processingTheUpdate")
    public String processingUpdate(@ModelAttribute("user") User user, Model model) {
        logger.info("Inside Update process.....");
        try {
            userService.updateUser(user);
            model.addAttribute("user", user);
            logger.info("Inside Update process.....");
            return "update-confirmation";
        } catch (Exception e) {
            logger.info("Error updating user profile "+e);
            model.addAttribute("error", "An error occurred while updating the profile.");
            return "update-form"; 
        }
    }

    @GetMapping("/approveUser/{UserId}")
    public String approveUser(@PathVariable("UserId")int id){
        logger.info("Inside approve user process.....");
        userService.approveUser(id);
        return "redirect:/list";
    }

    @GetMapping("/rejectUser/{UserId}")
    public String rejectUser(@PathVariable("UserId")int Id){
        userService.rejectUser(Id);

        return "redirect:/list";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("UserId") int Id) {
        logger.info("Inside delete controller method.....");

        userService.deleteUser(Id);

        return "redirect:/list";
    }



}
