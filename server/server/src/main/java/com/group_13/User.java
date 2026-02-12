package com.group_13;

class User
{
    private String ID;
    private String username;
    private String passwordHash;
    private String privileges;

    public String getPrivileges() {
        return privileges;
    }
    
    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    
}