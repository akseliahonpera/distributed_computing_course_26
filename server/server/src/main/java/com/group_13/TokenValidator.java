
package com.group_13;

import java.util.HashMap;


public final class TokenValidator {
    private static final long TOKEN_DURATION = 60*15;

    private static TokenValidator INSTANCE;
    private HashMap<String, Token> tokens = null;

    private TokenValidator() {        
        tokens = new HashMap<>();
    }

    public static synchronized TokenValidator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TokenValidator();
        }
        return INSTANCE;
    }

    public synchronized Token newToken()
    {
        Token newToken = new Token(TOKEN_DURATION);

        tokens.put(newToken.getTokenStr(), newToken);

        return newToken;
    }
    public synchronized boolean isValidTokenStr(String tokenStr)
    {
        if (!tokens.containsKey(tokenStr)) {
            return false;
        }
        return !tokens.get(tokenStr).hasExpired();
    }

    public void refreshTokenStr(String tokenStr) {
        if (tokens.containsKey(tokenStr)) {
            tokens.get(tokenStr).refresh(TOKEN_DURATION);
        }
    }
}