package com.group_13;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

class HospitalNetwork
{
    private TreeMap<Integer, HospitalNode> nodes = null;
    public HospitalNetwork()
    {
        nodes = new TreeMap<>();
    }

    public HospitalNode getNode(int id)
    {
        return nodes.get(id);
    }

    public void addNode(HospitalNode n)
    {
        nodes.put(n.getId(), n);
    }

    public ArrayList<HospitalNode> getAllNodes()
    {
        ArrayList<HospitalNode> nodeArray = new ArrayList<>();
        for (Map.Entry<Integer, HospitalNode> entry : nodes.entrySet()) {
            nodeArray.add(entry.getValue());
        }   
        return nodeArray;
    }

    @Override
    public String toString() {
        ArrayList<HospitalNode> nodeSet = getAllNodes();

        StringBuilder sb = new StringBuilder();
        for (HospitalNode n : nodeSet) {
            sb.append(n.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static HospitalNetwork fromString(String str) {
        HospitalNetwork net = new HospitalNetwork();

        String lines[] = str.split("\n");
        for (String line : lines) {
            HospitalNode node = HospitalNode.fromString(line);
            if (node != null) {
                net.addNode(node);
            }
        }
        return net;
    }
}