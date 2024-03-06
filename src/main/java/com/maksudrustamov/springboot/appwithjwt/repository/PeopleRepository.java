package com.maksudrustamov.springboot.appwithjwt.repository;

import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username); // мы получаем здесь optional(из-за того, что может такого человека и не быть)\
}
