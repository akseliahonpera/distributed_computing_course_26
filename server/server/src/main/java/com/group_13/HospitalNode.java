package com.group_13;

class HospitalNode
{
    public HospitalNode(int id, String name, String address)
    {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    private int id;
    private String name;
    private String address;

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
        return sb.toString();
    }

    public static HospitalNode fromString(String str)
    {
        try {
            String parts[] = str.split(",");
            return new HospitalNode(Integer.parseInt(parts[0]), parts[1], parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
