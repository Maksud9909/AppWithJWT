package com.maksudrustamov.springboot.appwithjwt.util;

import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import com.maksudrustamov.springboot.appwithjwt.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    } // мы здесь указываем какому классу нужен валидатор

    @Override
    public void validate(Object o, Errors errors) { //
        Person person = (Person) o;
        Optional<Person> optionalPerson = peopleService.findByUsername(person.getUsername());
        if (optionalPerson.isPresent()){
            errors.rejectValue("username","","Человек с таким именем пользователя уже существует");
        }
    }
}





















// @Override
//    public void validate(Object o, Errors errors) {
//        Person person = (Person) o;
//        try {
//            personDetailsService.loadUserByUsername(person.getUsername());
//        }catch (UsernameNotFoundException ignored){
//            return; // все ок пользователь не найден
//        }
//        errors.rejectValue("username","","Человек с таким именем пользователя существует");
//    }