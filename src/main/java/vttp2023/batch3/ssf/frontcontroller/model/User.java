package vttp2023.batch3.ssf.frontcontroller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class User implements Serializable {
    @NotNull
    @Size(min = 2, message = "Your username has to be at least 2 characters!")
    private String username;
    @NotNull
    @Size(min = 2, message = "Your password has to be at least 2 characters!")
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
