// package com.group_13;

// import java.awt.SystemTray;
// import java.io.File;
// import java.lang.reflect.Constructor;
// import java.lang.reflect.Field;
// import java.lang.reflect.Method;
// import java.net.URL;
// import java.net.URLClassLoader;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Random;
// import java.util.Scanner;
// import java.util.concurrent.ThreadLocalRandom;

// import com.group_13.api.AuthAPI;
// import com.group_13.api.BaseAPI;
// import com.group_13.model.Patient;
// import com.group_13.model.Result;
// import com.group_13.service.AuthService;
// import com.group_13.service.PatientService;
// import com.group_13.ui.ServerChoser;

// public class Testing {

//     private static void makePatients(int patientCount) throws Exception {
//         ArrayList<Patient> testPatients = new ArrayList<>();

//         final String[] lastNames = Files.readString(Path.of(
//                 "C:\\Users\\JONIK\\distributed_systems\\client\\client\\src\\main\\java\\com\\group_13\\sukunimet.txt"))
//                 .split("\n");

//         final String[] firstNames = Files.readString(Path.of(
//                 "C:\\Users\\JONIK\\distributed_systems\\client\\client\\src\\main\\java\\com\\group_13\\etunimet.txt"))
//                 .split("\n");

//         for (int i = 0; i < patientCount; i++) {

//             Patient testPatient = new Patient();

//             testPatient.setFName(firstNames[ThreadLocalRandom.current().nextInt(0, firstNames.length)].strip());
//             testPatient.setLName(lastNames[ThreadLocalRandom.current().nextInt(0, lastNames.length)].strip());

//             // testPatient.setFName(
//             //         Testing.generateRandomString(ThreadLocalRandom.current().nextInt(1,
//             //                 20), 65, 122));
//             // testPatient.setLName(
//             //         Testing.generateRandomString(ThreadLocalRandom.current().nextInt(1,
//             //                 20), 65, 122));

//             testPatient.setAddress("Tie " + Testing.generateRandomString(3, 48, 57));
//             testPatient.setDateofbirth(generateRandomDate());
//             testPatient.setPhone(Testing.generateRandomString(10, 48, 57));
//             testPatient.setSocialsecnum(Testing.generateRandomString(7, 48, 57));
//             testPatient.setEmergencycontact("");
//             String[] hospitals = { "oulu", "tampere", "helsinki" };
//             testPatient.setHomehospital(hospitals[ThreadLocalRandom.current().nextInt(0, hospitals.length)]);
//             testPatients.add(testPatient);
//         }

        
//         for (Patient patient : testPatients) {
//             PatientService.getInstance().createPatientAsync(patient)
//             .orTimeout(1, java.util.concurrent.TimeUnit.SECONDS)
//             .whenComplete((result, ex) -> {
//                 if(ex != null) {
//                     System.out.println("Error creating patient: "+ ex.getMessage());
//                 }
//                 else if (result.isSuccess()) {
//                     // System.out.println("Successfully created patient!");
//                 }
//                 else {
//                     System.out.println("Error creating patients: " + result.getMessage());
//                 }
//             })
//             .join();
//         }
        
        

//     }

//     private static void showPatientsTable() throws Exception {
//         Result<Patient[]> result = PatientService.getInstance().getAllPatients();
//         Patient[] patients = result.getData();  
//         for (Patient patient : patients) {
//             System.out.println(patient);
//         }
//     }

//     private static void deleteAllPatients() throws Exception {
//         Result<Patient[]> result = PatientService.getInstance().getAllPatients();
//         Patient[] patients = result.getData();
//         for (Patient patient : patients) {
//             PatientService.getInstance().deletePatient(patient);
//         }
//     }


//     private static String generateRandomString(int length, int leftLimit, int rightLimit) {
//         Random random = new Random();
//         return random.ints(leftLimit, rightLimit + 1)
//                 .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                 .limit(length)
//                 .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                 .toString();

//     }

//     private static String generateRandomDate() {

//         LocalDate start = LocalDate.of(1900, 1, 1);
//         LocalDate end = LocalDate.now();

//         long startEpochDay = start.toEpochDay();
//         long endEpochDay = end.toEpochDay();

//         long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
//         LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

//         String dateString = randomDate.toString();
//         return dateString;
//     }

//     public static void main(String[] args) throws Exception {

//         // Class<?> chooser = Class.forName("com.group_13.ui.ServerChoser");
    
//         // ServerChoser ouluChooser = (ServerChoser) chooser.getDeclaredMethod("getInstance", null).invoke(null, args);
//         // Constructor<ServerChoser> constructor = ServerChoser.class.getDeclaredConstructor(null);
//         // constructor.setAccessible(true);
//         // ServerChoser tampereChooser =  constructor.newInstance(args);
//         // ServerChoser helsinkiChooser = constructor.newInstance(args);
        
//         // ouluChooser.setServer(0);
//         // tampereChooser.setServer(1);
//         // helsinkiChooser.setServer(2);

//         // Class<?> authAPIClass = Class.forName("com.group_13.api.AuthAPI");
//         // AuthAPI ouluAuthAPI = (AuthAPI) authAPIClass.getDeclaredMethod("getInstance", null).invoke(null, args);
//         // // Constructor<AuthAPI> authAPIConstructor = 


        




//         // URLClassLoader loader1 = new URLClassLoader(new URL[] {new File("C:\\Workspace\\Distributed_Systems\\distributed_computing_course_26\\client\\client\\src\\main\\java\\com\\group_13\\").toURL()}, Thread.currentThread().getContextClassLoader());
//         // Class<?> ouluBaseAPI = loader1.loadClass("com.group_13.api.BaseAPI");
//         // ouluBaseAPI.getDeclaredMethod("setBaseURL", String.class).invoke(null, ouluChooser.getServerAddress());
//         // Class<?> ouluAuthAPI = loader1.loadClass("com.group_13.api.AuthAPI");
//         // AuthAPI ouluAuthAPIInstance = (AuthAPI)ouluAuthAPI.getDeclaredMethod("getInstance", null).invoke(null, args);
//         // System.out.println(ouluAuthAPIInstance.AUTH_ENDPOINT);

//         // Class<?> ouluAuthService = loader1.loadClass("com.group_13.service.PatientService");
//         // Class<?> ouluPatientService = loader1.loadClass("com.group_13.service.PatientService");
//         // PatientService ouluPatientInstance = (PatientService)ouluPatientService.getDeclaredMethod("getInstance", null).invoke(ouluPatientService, args);
        
        
        
//         // URLClassLoader loader2 = new URLClassLoader(new URL[] {new File("C:\\Workspace\\Distributed_Systems\\distributed_computing_course_26\\client\\client\\src\\main\\java\\com\\group_13\\api\\BaseAPI.java").toURL()}, Thread.currentThread().getContextClassLoader());
//         // Class<?> tampereBaseAPI = loader2.loadClass("com.group_13.api.BaseAPI");
//         // tampereBaseAPI.getDeclaredMethod("setBaseURL", String.class).invoke(null, tampereChooser.getServerAddress());
//         // Class<?> tampereAuthAPI = loader2.loadClass("com.group_13.api.AuthAPI");
//         // AuthAPI tampereAuthAPIInstance = (AuthAPI)tampereAuthAPI.getDeclaredMethod("getInstance", null).invoke(null, args);
//         // System.out.println(tampereAuthAPIInstance.AUTH_ENDPOINT);
        
        
//         // URLClassLoader loader3 = new URLClassLoader(new URL[] {new File("C:\\Workspace\\Distributed_Systems\\distributed_computing_course_26\\client\\client\\src\\main\\java\\com\\group_13\\api\\BaseAPI.javas").toURL()}, Thread.currentThread().getContextClassLoader());
//         // Class<?> helsinkiBaseAPI = loader3.loadClass("com.group_13.api.BaseAPI");
//         // helsinkiBaseAPI.getDeclaredMethod("setBaseURL", String.class).invoke(null, helsinkiChooser.getServerAddress());

     


        
//         // ouluAuthAPI.printURL();
//         // tampereAuthAPI.printURL();

//         // Class<?> auth = Class.forName("com.group_13.api.AuthAPI");
//         // AuthAPI ouluAuthAPI = (AuthAPI) auth.getDeclaredMethod("getInstance", null).invoke(ouluBaseAPI, args);
//         // ouluAuthAPI.printURL();

//         // AuthAPI testi = AuthAPI.getInstance();
//         // testi.printURL();
        




//         ServerChoser serverChoser = ServerChoser.getInstance();
//         serverChoser.setServer(0);
//         String targetServer = serverChoser.getServerAddress();
//         System.out.println("Target server: " + targetServer);
//         BaseAPI.setBaseURL(targetServer);

//         String userName = "root";
//         String userPassWord = "root";
//         Result<Void> result = AuthService.getInstance().login(userName, userPassWord);
        
//         Scanner scanner = new Scanner(System.in);

//         while (true) {
//             System.out.println("----------------------------------------------------------------------");
//             System.out.println("1. Create random patients");
//             System.out.println("2. Show patients table");
//             System.out.println("3. Clear patients table");
//             System.out.println("e. Exit");
//             System.out.println("----------------------------------------------------------------------");
//             System.out.print("input> ");
            
//             String input = scanner.nextLine();
            
//             if (input.equals("e")) {
//                 break;
//             } 
//             else if (input.equals("1")) {
//                 System.out.print("Give number of patients> ");
//                 makePatients(Integer.valueOf(scanner.nextLine()));
//             }
//             else if (input.equals("2")) {
//                 showPatientsTable();
//             }
//             else if (input.endsWith("3")) {
//                 deleteAllPatients();
//             }

//         }
//         scanner.close();

//     }

// }