
package com.group_13;


public class UserAuthenticator {
    private static UserAuthenticator INSTANCE;


    private UserAuthenticator() {        
        
    }

    public static UserAuthenticator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new UserAuthenticator();
        }
        return INSTANCE;
    }

    public boolean isValidUser(String username, String password)
    {
        return true;
    }
}