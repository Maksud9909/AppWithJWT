package com.maksudrustamov.springboot.appwithjwt.configuration;


import com.maksudrustamov.springboot.appwithjwt.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Этот класс является главным где мы настраиваем SpringSecurity
 * Здесь мы будем настраивать аутентификацию и авторизацию, и все другое
 */
@Configuration
@EnableWebSecurity // это дает понять Spring, что это конфигурационный файл для Security
@EnableGlobalMethodSecurity(prePostEnabled = true) // теперь мы можем использовать аннотацию PreAuthority
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Конфигирируем Spring Security
     * Конфигурируем Авторизацию то есть саму страницу для аутентификации
     * @param httpSecurity в этот метод поступает http запрос
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{


        // тут мы пишем условия
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN") // для доступа к этой странице у тебя должен быть ADMIN
                .antMatchers("/auth/login","/error","/auth/registration").permitAll() // если ноунейм юзер заходит мы его пускаем
                .anyRequest().hasAnyRole("USER","ADMIN")// для всех остальных страниц все имеют доступ
                .and()
                .formLogin().loginPage("/auth/login") // тут мы пишем где кастомная страница нужна для аутентификации
                .loginProcessingUrl("/process_login") // куда мы хотим отправлять данные с формы (/process_login)
                .defaultSuccessUrl("/hello",true)// куда мы попадем в случае успешной аутентификации, второй, чтобы по любому нас туда отправлял в случае успеха
                .failureUrl("/auth/login?error")// мы здесь говорим, что если не получится, то нужно идти в страницу error
                .and()
                .logout().logoutSuccessUrl("/logout") // при перехоже на сылку, у человека будет стираться cookies
                .logoutSuccessUrl("/auth/login") // что будет если он выйдет с аккаунта
                .and().sessionManagement() // не надо сохранять нашу сессию на сервере(говорим Spring)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // не какая сессия не хранится на сервере

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // помогает проводить аутентификацию
    }


    // настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder()); // мы дали понять спрингу, что надо именно это service использовать
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(); // мы сказали, что пароль не шифруется
    }


    @Bean
    @Override // UsernamePasswordAuthenticationToken на основе него он позволяет делать регистрацию
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // мы его вернем, чтобы использовать в контроллере, также он позоволяет проводить аутентификацию UsernamePasswordAuthenticationToken
        return super.authenticationManagerBean();
    }



}
