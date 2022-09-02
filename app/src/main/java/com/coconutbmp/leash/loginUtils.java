package com.coconutbmp.leash;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginUtils {
    static public boolean validateName(String name){
        Pattern special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~/`-]");
        Matcher isSpecial = special.matcher(name);
        if(name.isEmpty() || isSpecial.find()){
            return false;
        }
        for (int i = 0; i < name.length(); i++){
            char c = name.charAt(i);
            if(Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
    static public boolean validateEmail(String email){
        if(email.isEmpty() || !email.contains("@") || !email.contains(".")){
            return false;
        }
        return true;
    }

    static public boolean validatePass(String pass, String confirm){
        boolean capitalFlag = false, numFlag = false, lowerFlag = false;
        if(confirm == null){
            if(pass.length() < 8){
                return false;
            }
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
        else if(!confirm.equals(pass)){
            return false;
        }
        return true;
    }

    static public String hash(String pass) throws NoSuchAlgorithmException{
        String generated = null;
        String salt = getSalt();

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(salt.getBytes());
            byte[] bytes = md.digest(pass.getBytes());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++){
                builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generated = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generated;
    }

    static private String getSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }
}
