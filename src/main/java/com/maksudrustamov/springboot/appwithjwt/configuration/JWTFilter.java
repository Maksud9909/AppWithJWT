package com.maksudrustamov.springboot.appwithjwt.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.maksudrustamov.springboot.appwithjwt.security.JWTUtil;
import com.maksudrustamov.springboot.appwithjwt.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author maksudrustamov
 * Будет отлавливать каждый запрос, и будет с ними работать
 */
@Component
public class JWTFilter extends OncePerRequestFilter { // мы будем проверять каждый раз запрос один раз

    private final JWTUtil jwtUtil;

    private final PersonDetailsService personDetailsService;
    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // какой заголовок в этом запросе, извлекаем его, затем будет передовать наш jwt token


        // если хедер не пустой, и начинается с слова Bearer
        if (authHeader !=null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7); // наш jwt token должен начинаться с 7 символа

            if (jwt.isBlank()){ // если jwt пустой, значит что-то не то в регистрации пользователя
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT Token in Bearer header");
            }else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt); // если провалидировалось то ок, и мы взяли юзернейм
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username); // получаем его из базы

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken
                            (userDetails,userDetails.getPassword(), userDetails.getAuthorities());  // здесь идет авторизация этого пользователя

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exception){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT Token");
                }
            }


        }
        filterChain.doFilter(request,response); // передаем дальше реквест и респонс
    }
}
