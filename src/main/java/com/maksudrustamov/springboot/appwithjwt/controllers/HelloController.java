package com.maksudrustamov.springboot.appwithjwt.controllers;


import com.maksudrustamov.springboot.appwithjwt.security.PersonDetails;
import com.maksudrustamov.springboot.appwithjwt.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {
    private final AdminService adminService;

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }


    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo(){
        // через SecurityContextHolder мы получаем доступ к обертеке Person, а через обертку уже получаем доступ к самой сущности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); // закастили до человека, который регался


        return personDetails.getUsername();
    }

    @GetMapping("/admin")
    public String adminPage(){
        adminService.doAdminStuff();
        return "admin";
    }


}
