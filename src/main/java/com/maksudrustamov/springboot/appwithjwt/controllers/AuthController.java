package com.maksudrustamov.springboot.appwithjwt.controllers;


import com.maksudrustamov.springboot.appwithjwt.dto.AuthenticationDTO;
import com.maksudrustamov.springboot.appwithjwt.dto.PersonDTO;
import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import com.maksudrustamov.springboot.appwithjwt.security.JWTUtil;
import com.maksudrustamov.springboot.appwithjwt.service.PeopleService;
import com.maksudrustamov.springboot.appwithjwt.util.PersonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator; // проверка
    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, PeopleService peopleService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

//    @GetMapping("/login")
//    public String loginPage(){
//        return "auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute("person")Person person){ // в модель положит пустого человека для фронта
//        return "auth/registration";
//    }




    @PostMapping("/registration")
    public Map<String,String> performRegistration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {

        Person person = convertToPerson(personDTO); // конвертируем DTo в человека

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем обратно на страницу регистрации
            return Map.of("Message","Erorr");
        }

        // Если ошибок нет, регистрируем пользователя
        peopleService.register(person);

        // когда регистрация прошла теперь мы возвращаем jwt token
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token",token);
    }

    @PostMapping("/login")
    public Map<String,String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){

        // стандартный класс для енкапсуляции логина и пароля
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(),
                authenticationDTO.getPassword());


        try {
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException exception){ // неправильный логин и пароль, которых нет в базе
            return Map.of("Message","incorrect credentials");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token",token);
    }







    public Person convertToPerson(PersonDTO personDTO){
        return this.modelMapper.map(personDTO, Person.class);
    }
}
