package com.group_13;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

import javax.sound.midi.SysexMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_13.model.Patient;
import com.group_13.model.Result;
import com.group_13.service.PatientService;



public class Client {
    enum State {
        TEST,
        LOGIN,
        MENU,
        LOGOUT,
        EXIT

    }

    private static Client INSTANCE = null;
    private State currentState = State.TEST;

    private Client(){};

    public static void start() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
            INSTANCE.mainLoop();
        }
    }

    private void mainLoop() {

        while (currentState != State.EXIT) {

            switch (currentState) {
                case TEST:
                    currentState = testConnection();
                    break;
                case LOGIN:
                    currentState = performLogin();
                    break;
                case LOGOUT:
                    currentState = performLogout();
                    break;
                default:
                    break;
            }
        }

        System.out.println("Client closed");

    }


    private State testConnection() {


        Patient testPatient = new Patient();
        testPatient.setId("123");
        testPatient.setFName("Teemu");
        testPatient.setLName("Teekkari");
        testPatient.setDateofbirth("20.06.2005");
        testPatient.setSocialsecnum("200605501");
        testPatient.setAddress("Pentti Kaiteran katu 1");
        testPatient.setPhone("358443219832");
        testPatient.setEmergencycontact("N/A");
        testPatient.setHomehospital("OYS");


        System.out.println("Creating patient");
        PatientService patientService = PatientService.getInstance();

        try {
            Result result = patientService.createPatient(testPatient);
            System.out.println(result.isSuccess());
            System.out.println(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        

        return State.EXIT;
    }



    private State performLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username >> ");
        String userName = scanner.nextLine();

        System.out.print("Password >> ");
        String passWord = scanner.nextLine();


        return State.MENU;
    }

    private State performLogout() {
        
        return State.EXIT;
    }


    public static void main(String[] args) {
        System.out.println("Starting Client");
        Client.start();
    }

}
