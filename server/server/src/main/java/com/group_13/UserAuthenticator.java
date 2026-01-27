
package com.group_13;

public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator {

    public UserAuthenticator(String realm) {
        super(realm);
    }

    public boolean checkCredentials(String username,String password) {
        return true;
    }
}