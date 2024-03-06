package com.maksudrustamov.springboot.appwithjwt.service;

import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import com.maksudrustamov.springboot.appwithjwt.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> findByUsername(String username){
        return peopleRepository.findByUsername(username);
    }

    @Transactional // так как нам нужно менять базу данных
    public void register(Person person){
        String encodedPassword = passwordEncoder.encode(person.getPassword()); // он так шифрует пароль
        person.setRole("ROLE_USER");

        person.setPassword(encodedPassword); // мы добавили этот пароль человеку
        peopleRepository.save(person); // он добавляет нового человека в базу при регистрации
    }
}
