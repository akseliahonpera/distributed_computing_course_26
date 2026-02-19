package com.group_13;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class HospitalNetwork
{
    private static HospitalNetwork INSTANCE = null;

    private TreeMap<Integer, HospitalNode> nodes = null;
    private int localNodeId = -1;
    private String confPath = null;
    private HospitalNetwork()
    {
        nodes = new TreeMap<>();
    }

    public static synchronized HospitalNetwork getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new HospitalNetwork();
        }
        return INSTANCE;
    }
 
    public HospitalNode getNode(int id)
    {
        return nodes.get(id);
    }

    public HospitalNode getNodeByRowId(long id)
    {
        int nodeId = (int)((id >> 48) & 0xFFFF);
        return getNode(nodeId);
    }

    public HospitalNode getLocalNode()
    {
        return nodes.get(localNodeId);
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

    public void loadFromFile(String filePath) {
        try {
            String str = Files.readString(Path.of(filePath));
            confPath = filePath;
            nodes = new TreeMap<>();

            String lines[] = str.split("\n");
            for (String line : lines) {
                HospitalNode node = HospitalNode.fromString(line);
                if (node != null) {
                    addNode(node);
                    if (!node.isReplica()) {
                        localNodeId = node.getId();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load network configuration: " + e.getMessage());
        }
    }
    public void save()
    {
        try {
            Files.writeString(Path.of(confPath), this.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to write network configuration: " + e.getMessage());
        }
    }
}