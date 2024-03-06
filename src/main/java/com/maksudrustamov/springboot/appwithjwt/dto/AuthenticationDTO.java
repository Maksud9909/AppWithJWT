package com.maksudrustamov.springboot.appwithjwt.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Сюда будут посылаться данные для аутентификации, а для этого нам нужен юзернейм и пароль
 */
public class AuthenticationDTO {
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2,max = 100,message = "Name length should be from 2 to 100")
    private String username;

    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
