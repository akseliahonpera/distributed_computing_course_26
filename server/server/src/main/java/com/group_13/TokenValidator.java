
package com.group_13;

import java.util.HashMap;


public final class TokenValidator {
    private static TokenValidator INSTANCE;
    private HashMap<String, Token> tokens;

    private TokenValidator() {        
        tokens = new HashMap<>();
    }

    public static synchronized TokenValidator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TokenValidator();
        }
        return INSTANCE;
    }

    public Token newToken()
    {
        Token newToken = new Token(60*15);

        tokens.put(newToken.getTokenStr(), newToken);

        return newToken;
    }
    public boolean isValidTokenStr(String tokenStr)
    {
        if (!tokens.containsKey(tokenStr)) {
            return false;
        }
        return !tokens.get(tokenStr).hasExpired();
    }
}