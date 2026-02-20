
package com.group_13;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserAuthenticator {
    private static UserAuthenticator INSTANCE;


    private UserAuthenticator() {        
        
    }

    public static synchronized UserAuthenticator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new UserAuthenticator();
        }
        return INSTANCE;
    }

    public boolean isValidUser(String username, String password)
    {
        try {
            HashMap<String,String> query = new HashMap<>();
            query.put("username", username);
            JSONArray result = DataBaseQueryHelper.query(DataBaseManager.getOwnDataBase(), "users", query);
            
            for (int i = 0; i < result.length(); i++) {
                String uname = result.getJSONObject(i).getString("username");
                String hash = result.getJSONObject(i).getString("passwordhash");

                if (uname.equals(username) && PasswordUtil.verifyPassword(password, hash)) {
                    return true;
                }
            }
        } catch (Exception e) { }

        return false;
    }

    public void addUser(String username, String password, int privileges) {
        try {
            JSONObject newObject = new JSONObject();

            newObject.put("username", username);
            newObject.put("passwordhash", PasswordUtil.hashPassword(password));
            newObject.put("privileges", Integer.toString(privileges));

            DataBaseQueryHelper.insert(DataBaseManager.getOwnDataBase(), "users", newObject);
        } catch (Exception e) { 
            System.out.println("Error when adding user: " + e.getMessage());
        }
    }

    public boolean userExists(String username) {
        try {
            HashMap<String,String> query = new HashMap<>();
            query.put("username", username);
            JSONArray result = DataBaseQueryHelper.query(DataBaseManager.getOwnDataBase(), "users", query);

            return (result.length() > 0);
        } catch (Exception e) { }

        return false;
    }
}