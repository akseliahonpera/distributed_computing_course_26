
package com.group_13;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONObject;

public class TestData
{

    private TreeMap<String, ArrayList<String>> stringVars = null;
    private TreeMap<String, ArrayList<Integer>> integerVars = null;

    private ArrayList<String> allVarNames = null;
    private Random rng = null;

    public TestData()
    {
        allVarNames = new ArrayList<>();
        stringVars = new TreeMap<>();
        integerVars = new TreeMap<>();

        rng = new Random();
    }
    
    public void addStringVariable(String varName, String csvFilePath) throws Exception
    {
        String str = Files.readString(Path.of(csvFilePath));
        ArrayList<String> values = new ArrayList<>(Arrays.asList(str.split(",")));
        stringVars.put(varName, values);
        allVarNames.add(varName);
    }

    public void addIntegerVariable(String varName, int minVal, int maxVal)
    {
        ArrayList<Integer> bounds = new ArrayList<>();
        bounds.add(minVal);
        bounds.add(maxVal);

        integerVars.put(varName, bounds);
        allVarNames.add(varName);
    }

    public JSONObject generateRandomData() {
        JSONObject object = new JSONObject();

        for (String var : stringVars.keySet()) {
            ArrayList<String> values = stringVars.get(var);

            object.put(var, values.get(rng.nextInt(values.size())));
        }

        for (String var : integerVars.keySet()) {
            ArrayList<Integer> bounds = integerVars.get(var);

            object.put(var, rng.nextInt(bounds.get(0), bounds.get(1)));
        }

        return object;
    }

    public JSONObject generateSingleVariable() {
        JSONObject object = new JSONObject();

        String var = allVarNames.get(rng.nextInt(allVarNames.size()));

        if (stringVars.containsKey(var)) {
            ArrayList<String> values = stringVars.get(var);
            object.put(var, values.get(rng.nextInt(values.size())));
        } else if (integerVars.containsKey(var)) {
            ArrayList<Integer> bounds = integerVars.get(var);
            object.put(var, rng.nextInt(bounds.get(0), bounds.get(1)));
        }

        return object;
    }

    public String generateRandomValue(String varName)
    {
        if (stringVars.containsKey(varName)) {
            ArrayList<String> values = stringVars.get(varName);

            return values.get(rng.nextInt(values.size()));
        } else if (integerVars.containsKey(varName)) {
            ArrayList<Integer> bounds = integerVars.get(varName);

            return Integer.toString(rng.nextInt(bounds.get(0), bounds.get(1)));
        }

        return "";
    }
}