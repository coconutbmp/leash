package com.coconutbmp.leash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SignInTests {
    @Test
    public void incorrectEmail_noAt(){
        assertFalse(loginUtils.validateEmail("WrongEmail.com"));
    }

    @Test
    public void incorrectEmail_noDot(){
        assertFalse(loginUtils.validateEmail("WrongEmail@gmailcom"));
    }

    @Test
    public void correctEmail(){
        assertTrue(loginUtils.validateEmail("RightEmail@gmail.com"));
    }
}
