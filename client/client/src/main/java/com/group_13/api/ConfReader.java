package com.group_13.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;

public class ConfReader {
    /*
    This class can be used to read config files with host information on each row. 
    Returns arraylist of Host objects built from the config file.
    */
    public ArrayList<Host> loadFromFile(String filePath) {
        try {
            String str = Files.readString(Path.of(filePath));
            ArrayList<Host> hostUrls = new ArrayList<>();

            String lines[] = str.split("\n");
            for (String line : lines) {
                Host host = new Host(line);
                //System.out.println(host.getUrl());
                hostUrls.add(host);
            }
            return hostUrls;


        } catch (IOException e) {
            System.out.println("fornite: " + e.getMessage());
            return null;
        }
    }

}
