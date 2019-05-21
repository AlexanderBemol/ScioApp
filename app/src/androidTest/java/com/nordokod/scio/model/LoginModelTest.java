package com.nordokod.scio.model;

import com.nordokod.scio.entity.User;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

public class LoginModelTest {

    LoginModel lm = mock(LoginModel.class);
    @Test
    public void userIsLogged() {
    }

    @Test
    public void loginWithMail1() {
        User user = new User();
        user.setEmail("aleex1742@gmail.com");
        user.setPassword("17421742");
        lm.loginWithMail(user);
    }

    @Test
    public void loginWithMail2() {
        User user = new User();
        user.setEmail("");
        user.setPassword("17421742");
        lm.loginWithMail(user);

    }

}