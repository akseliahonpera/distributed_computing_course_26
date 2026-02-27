package com.group_13;

public class HospitalNode {
    public HospitalNode(int id, String name, String address, boolean isReplica) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isReplica = isReplica;
    }

    private int id;
    private String name;
    private String address;
    private boolean isReplica;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(',');
        sb.append(name);
        sb.append(',');
        sb.append(address);
        sb.append(',');
        sb.append((isReplica ? "true" : "false"));
        return sb.toString();
    }

    public static HospitalNode fromString(String str) {
        try {
            String parts[] = str.split(",");
            return new HospitalNode(Integer.parseInt(parts[0]), 
                                    parts[1], 
                                    parts[2], 
                                    parts[3].strip().equalsIgnoreCase("true"));

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
}
