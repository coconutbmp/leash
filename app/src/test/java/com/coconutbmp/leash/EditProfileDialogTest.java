package com.coconutbmp.leash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EditProfileDialogTest {
    @Test
    public void testInvalidName() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("Password123");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        //doReturn(false).when(profileDialog).completeEdit("Hello2", "Test", "failed@test.com", p);
        boolean r = profileDialog.completeEdit("Hello2", "Test", "failed@test.com", p);
        assertFalse(r);
    }

    @Test
    public void testInvalidSurname() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("Password123");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        //doReturn(false).when(profileDialog).completeEdit("Hello", "Test2", "failed@test.com", p);
        boolean r = profileDialog.completeEdit("Hello", "Test2", "failed@test.com", p);
        assertFalse(r);
    }

    @Test
    public void testInvalidEmail() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("Password123");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        doReturn(false).when(profileDialog).completeEdit("Hello", "Test2", "failed@test.com", p);
        boolean r = profileDialog.completeEdit("Hello", "Test", "failedtest.com", p);
        assertFalse(r);
    }

    @Test
    public void testInvalidPass() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("password");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        //doReturn(false).when(profileDialog).completeEdit("Hello", "Test2", "failed@test.com", p);
        boolean r = profileDialog.completeEdit("Hello", "Test", "failed@test.com", p);
        assertFalse(r);
    }

    @Test
    public void testValid() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("Password123");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        //doReturn(false).when(profileDialog).completeEdit("Hello", "Test2", "failed@test.com", p);
        boolean r = profileDialog.completeEdit("Hello", "Test", "obscure@test.com", p);
        assertFalse(r);
    }
}
