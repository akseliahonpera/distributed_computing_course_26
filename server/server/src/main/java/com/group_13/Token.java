package com.group_13;

import java.security.SecureRandom;
import java.util.Base64;

import org.json.JSONObject;

public class Token
{
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    private final long expires;
    private final String str;

    public Token(long durationSeconds)
    {
        this.str = generateToken();
        this.expires = epochTime() + durationSeconds;
    }

    public Token(JSONObject obj) {
        expires = obj.getLong("expiration");
        str = obj.getString("token");
    }

    public String getTokenStr()
    {
        return str;
    }

    public boolean hasExpired()
    {
        return (epochTime() > expires);
    }

    public long getExpirationTime()
    {
        return expires;
    }

    private long epochTime() 
    {
        return System.currentTimeMillis() / 1000l;
    }

    private static String generateToken() {
        byte[] randomBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}