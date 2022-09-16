package com.coconutbmp.leash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SignInTests {

    @Test
    public void incorrectName_contains_special_char(){
        assertFalse(loginUtils.validateName("Wrong!Name"));
    }

    @Test
    public void incorrectName_empty(){
        assertFalse(loginUtils.validateName(""));
    }

    @Test
    public void incorrectName_contains_number(){
        assertFalse(loginUtils.validateName("Name123"));
    }

    @Test
    public void correctName(){
        assertTrue(loginUtils.validateName("Name"));
    }

    @Test
    public void incorrectEmail_noAt(){
        assertFalse(loginUtils.validateEmail("WrongEmail.com"));
    }

    @Test
    public void incorrectEmail_noDot(){
        assertFalse(loginUtils.validateEmail("WrongEmail@gmailcom"));
    }

    @Test
    public void incorrectEmail_empty(){
        assertFalse(loginUtils.validateEmail(""));
    }

    @Test
    public void correctEmail(){
        assertTrue(loginUtils.validateEmail("RightEmail@gmail.com"));
    }

    @Test
    public void incorrectPass_less_than_8(){
        assertFalse(loginUtils.validatePass("Test", null));
    }

    @Test
    public void incorrectPass_NoCapital(){
        assertFalse(loginUtils.validatePass("test1234", null));
    }

    @Test
    public void incorrectPass_NoDigit(){
        assertFalse(loginUtils.validatePass("TestPass", null));
    }

    @Test
    public void incorrectPass_NoMatch(){
        assertFalse(loginUtils.validatePass("Test1234", "Test4567"));
    }

    @Test
    public void correctPass(){
        assertTrue(loginUtils.validatePass("Test1234", null));
    }

    @Test
    public void correctPass_Matching(){
        assertTrue(loginUtils.validatePass("Test1234", "Test1234"));
    }
}
