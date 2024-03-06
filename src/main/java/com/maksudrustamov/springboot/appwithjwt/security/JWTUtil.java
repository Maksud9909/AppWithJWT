package com.maksudrustamov.springboot.appwithjwt.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Класс который работает с JWt token
 * Здесь мы будем генерировать и валидировать токен для клиента
 */
@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;
    public String generateToken(String username){ // токен будет создаваться на основе юзернейма
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant()); // жизнь токена будет всего 60 минут

        return JWT.create()
                .withSubject("User Details") // что вообще хранится в этом токене
                .withClaim("username",username) // это payload, те самые пара ключ значение, данные пользователя хранятся здесь
                .withIssuedAt(new Date()) // это с какого времени надо отсчитывать
                .withIssuer("SecurityApp") // кто выдал этот токен, а так вообще тут стоит название вашего приложения
                .withExpiresAt(expirationDate) // когда время токена истечет
                .sign(Algorithm.HMAC256(secret)); // наш secret, и по какому алгоритму решается
    }


    /**
     * нам клиент будет кидать запрос с JWT_token, который мы ему выдали, и мы должны этот токен валидировать
     * также мы должны извлечь из него claim(username), чтобы потом найти этого пользователя по базе по его имени,
     * чтобы провести аутентификацию
     * @param token - это и есть jwt token
     * @return - получаем юзера
     * @throws JWTVerificationException - если не проходить проверку
     */
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        // теперь мы этот токен валидируем, чтобы понять, что токен был выдан нами
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details") // у него должен быть такой Subject user details
                .withIssuer("SecurityApp")
                .build();


        // здесь теперь мы проверяем те правила, которые написали
        // после этого, эта переменная возваращает нам jwt
        DecodedJWT jwt = jwtVerifier.verify(token);

        return jwt.getClaim("username").asString(); // через jwt мы получаем юзера(payload)

    }
}
