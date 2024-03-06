package com.maksudrustamov.springboot.appwithjwt.service;

import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import com.maksudrustamov.springboot.appwithjwt.repository.PeopleRepository;
import com.maksudrustamov.springboot.appwithjwt.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Этот класс загружает пользователя в userDetail
 */
@Service // чтобы сервис знал, что он загружает пользователя, нужно имплементировать это UserDetailsService
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);


        if (person.isEmpty()){ // если такого человека, с таким именем нет, то выбрасывай исключение
            throw new UsernameNotFoundException("User not found!");
        }
        return new PersonDetails(person.get());
    }
}
