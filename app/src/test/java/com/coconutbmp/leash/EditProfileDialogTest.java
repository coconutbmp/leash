package com.coconutbmp.leash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EditProfileDialogTest {
    @Test
    public void testInvalidName() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("password");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        doNothing().when(profileDialog).completeEdit("Hello2", "Test", "failed@test.com", p);
        profileDialog.completeEdit("Hello2", "Test", "failed@test.com", p);
        verify(profileDialog, times(1)).completeEdit("Hello2", "Test", "failed@test.com", p);
    }

    @Test
    public void testInvalidSurname() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String p = loginUtils.Hash("password");
        EditProfileDialog profileDialog = mock(EditProfileDialog.class);
        doNothing().when(profileDialog).completeEdit("Hello", "Test2", "failed@test.com", p);
        profileDialog.completeEdit("Hello", "Test2", "failed@test.com", p);
        verify(profileDialog, times(1)).completeEdit("Hello", "Test2", "failed@test.com", p);
    }
}
