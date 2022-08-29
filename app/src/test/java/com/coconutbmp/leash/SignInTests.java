package com.coconutbmp.leash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SignInTests {
    @Test
    public void incorrectEmail_noAt(){
        assertFalse(MainActivity.validateEmail("WrongEmail.com"));
    }

    @Test
    public void incorrectEmail_noDot(){
        assertFalse(MainActivity.validateEmail("WrongEmail@gmailcom"));
    }

    @Test
    public void correctEmail(){
        assertTrue(MainActivity.validateEmail("RightEmail@gmail.com"));
    }
}
