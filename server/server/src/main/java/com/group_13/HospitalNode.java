package com.group_13;

public class HospitalNode
{
    public HospitalNode(int id, String name, String address, boolean isReplica, long lastSyncTime)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isReplica = isReplica;
        this.lastSyncTime = lastSyncTime;
    }

    private int id;
    private String name;
    private String address;
    private boolean isReplica;
    private long lastSyncTime;

    private Token authToken = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(',');
        sb.append(name);
        sb.append(',');
        sb.append(address);
        sb.append(',');
        sb.append((isReplica ? "true" : "false"));
        sb.append(',');
        sb.append(lastSyncTime);
        return sb.toString();
    }

    public static HospitalNode fromString(String str)
    {
        try {
            String parts[] = str.split(",");
            return new HospitalNode(Integer.parseInt(parts[0]), 
                                    parts[1], 
                                    parts[2], 
                                    parts[3].strip().equalsIgnoreCase("true"), 
                                    Long.parseLong(parts[4].trim()));

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isReplica() {
        return isReplica;
    }

    public void setIsReplica(boolean isReplica) {
        this.isReplica = isReplica;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public Token getAuthToken() {
        if (authToken == null || authToken.hasExpired()) {
            authToken = AuthService.getAuthTokenForServer(address);
        }
        return authToken;
    }
}
