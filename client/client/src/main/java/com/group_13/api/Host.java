package com.group_13.api;

import java.util.ArrayList;

public class Host {
    
    final String prefix= "https://"; 
    String bigdix = null;
    String smalltits = null;
    final String midfix = "/api/";
    String url = null;

    String hostNumber = null;//retard alert
    String hostPlace = null;
    boolean ownLocal = false;


    
    public Host(String confLine){
        parseLine(confLine);
        setUrl();
    }

    private void setUrl(){
        this.url = prefix+bigdix+":"+smalltits+midfix;
        //System.out.println(this.url);
    }

    public String getUrl(){
        return this.url;
    }
     public String getHostPlace(){
        return this.hostPlace;
    }

    private void parseLine(String line){
        String[] stuff= null;
        String regex = "[,\\:]";
        stuff = line.split(regex);
        /*for(int j = 0 ; j<stuff.length;j++){
            System.out.println("stuff_lista: "+stuff[j]);
        }*/
        
        for (int i = 0 ; i<stuff.length;i++){
            if(i==0){
                this.hostNumber=stuff[i];
            }
            if(i==1){
                this.hostPlace=stuff[i];
            }
            if(i==2){
                this.bigdix=stuff[i];
            }
            if(i==3){
                this.smalltits=stuff[i];
            }
        }
    }


}
