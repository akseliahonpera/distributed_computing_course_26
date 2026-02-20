package com.group_13;

import java.time.Instant;
import java.util.TreeMap;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

/*

    Alustava idea on luoda luokka, joka vastaa changelog taulun päivitysten lähettämisestä muille servuille.
    Lähettäminen voi tapahtua esim. ajastimen mukaisesti tai lähettäminen voidaan forcettaa jolloin ajastin ohitetaan.
    
    Pidä muistissa milloin viimeksi eri servuille on lähetetty muutokset, sekä mitkä muutokset on lähetty(changelog rowid)


    Minun ajatus synkronoinnista oli semmonen että, replicat hakee omistajilta muutokset. 
    Replicat pitää kirjaa ajasta milloin viimeksi synkronoitu ja kysyy muutokset omistajalta:
     - Lähettää queryn GET http://owneraddress/api/sync?since=lastsynctime
     - Omistajan backendi hakee muutokset SELECT * FROM changelog WHRE timestamp > lastsynctime
     - Ja lähettää muutokset JSONArrayssa
     - Replica ajaa muutokset replica databaseen

    Tein tuolla minun idealla synkkauksen (ReplicaSync.java + muutokset RequestHandler.java)

    -Ara
*/


public class DataBaseBackUpper {

    private static DataBaseBackUpper INSTANCE = null;
    private HospitalNetwork hospitalNetwork = null;
    private TreeMap<Integer, Pair<Long, Long>> lastUpdate = null;

    private DataBaseBackUpper(){};

    public static synchronized DataBaseBackUpper getInstance() {
        
        if(INSTANCE == null) {
            INSTANCE = new DataBaseBackUpper();
        }
        return INSTANCE;
    } 

    public synchronized void initBackUpper(HospitalNetwork hospitalNetwork) {

        // Safe whole network just in case
        this.hospitalNetwork = hospitalNetwork;

        // Currently add all known servers (excluding itself) to be possible backup locations
        // On startup send whole changelog
        for(HospitalNode node : hospitalNetwork.getAllNodes()) {
            if (node.getId() != hospitalNetwork.getLocalNode().getId()) {

                // Send whole changlog()
                // Pitää miettiä implementaatio lähettämiselle ja vastaanotolle

                Long exampleRowID = Long.getLong("13");
                Long currentTime = Instant.now().toEpochMilli();
                lastUpdate.put(node.getId(), new Pair<Long, Long>(exampleRowID, currentTime));
            }
        }
 
    }

    


    public synchronized void triggerBackup() {
        //TODO 
        // Triggeraa/Pakota lähettäminen
    }



}
