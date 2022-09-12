package com.coconutbmp.leash;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class loginUtils { // functions for login validation
    static public boolean validateName(String name){ // validate user's name and/or surname
        Pattern special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~/`-]"); // list of invalid symbols
        Matcher isSpecial = special.matcher(name);
        if(name.isEmpty() || isSpecial.find()){ // check name is filled and doesn't have symbols
            return false;
        }
        for (int i = 0; i < name.length(); i++){ // loop to find if name has numbers
            char c = name.charAt(i);
            if(Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
    static public boolean validateEmail(String email){ // validate email
        if(email.isEmpty() || !email.contains("@") || !email.contains(".")){ //must be filled and have an '@' and a '.'
            return false;
        }
        return true;
    }

    static public boolean validatePass(String pass, String confirm){ // validate password
        boolean capitalFlag = false, numFlag = false, lowerFlag = false;
        if(confirm == null){ // if null, not doing confirmation of password
            if(pass.length() < 8){ // password must be of length 8 or more
                return false;
            }

            // loop and check if there is an instance of a capital, lowercase and number in password
            for (int i = 0; i < pass.length(); i++){
                char c = pass.charAt(i);
                if(Character.isUpperCase(c)){
                    capitalFlag = true;
                }
                else if(Character.isDigit(c)){
                    numFlag = true;
                }
                else if(Character.isLowerCase(c)){
                    lowerFlag = true;
                }
            }
            if(!capitalFlag || !lowerFlag || !numFlag){
                return false;
            }
        }
        else if(!confirm.equals(pass)){ // if confirm isn't null then passwords must match
            return false;
        }
        return true;
    }

    public static String Hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    static private byte[] getSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    static private String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }
        else{
            return hex;
        }
    }

    static public boolean verifyPassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
