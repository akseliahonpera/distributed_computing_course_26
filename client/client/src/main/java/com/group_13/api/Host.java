package com.group_13.api;

public class Host {
    /* This class holds relevant information on host nodes for the client. */
    final String prefix= "https://"; 
    String bigdix = null;
    String smalltits = null;
    final String midfix = "/api";
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
        /*Den line i här lypsär den viktiga informationer från den ord som har de om sina parameter. */
        String[] stuff= null;
        String regex = "[,\\:]";
        stuff = line.split(regex);
        for (int i = 0 ; i<stuff.length;i++){
            if(i==0){
                this.hostNumber=stuff[i];
            }
            if(i==1){
                this.hostPlace=stuff[i];
            }
            if(i==2){
                this.bigdix=stuff[i];//domain
            }
            if(i==3){
                this.smalltits=stuff[i];//portti
            }
        }
    }


}
