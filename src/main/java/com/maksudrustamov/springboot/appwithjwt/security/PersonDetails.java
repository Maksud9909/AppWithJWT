package com.maksudrustamov.springboot.appwithjwt.security;

import com.maksudrustamov.springboot.appwithjwt.entity.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // права доступа, к какому-то юзеру нельзя будет заходить куда-то
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword(); // тут мы получаем пароль нашего пользователя
    }

    @Override
    public String getUsername() {
        return this.person.getUsername(); // тут мы получаем юзернейм пользователя
    }

    @Override
    public boolean isAccountNonExpired() { // здесь мы должны прописать логику, про то, что акк не просрочен
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // здесь мы должны прописать логику, про то, что акк не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // логика, для того, что пароль не просрочен
        return true;
    }

    @Override
    public boolean isEnabled() { // этот аккаунт работает ли, включен ли он
        return true;
    }


    // нужно, чтобы получать данные аутентифициоравнного пользователя
    public Person getPerson(){
        return this.person;
    }
}
