package com.coconutbmp.leash;

public interface RequestHandler { // interface to handle server responses
    public abstract void processResponse(String response); // make function required by all requests to handle responses
}
