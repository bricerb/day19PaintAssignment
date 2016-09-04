package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Brice on 8/25/16.
 */
public class Client {

    String strokeString;

//    public static void main(String[] args) {
    public void startClient() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("Running");

        try {
            Socket clientSocket = new Socket("localhost", 8005);
//            Socket clientSocket = new Socket("10.0.0.126", 8024); // Rebecca
//            Socket clientSocket = new Socket ("10.0.0.136", 8080); // Jessica
//            Socket clientSocket = new Socket ("10.0.0.138", 8080); // Yehia
//            Socket clientSocket = new Socket ("10.0.0.132", 8585); // Donald
//            Socket clientSocket = new Socket ("10.0.0.134", 8005); // Brett
            System.out.println("Connected to Server");


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Type msg");
            out.write(inputScanner.nextLine());
            System.out.println("type something else");
            out.println();

            int counter = 0;
            while(counter < 15) {
                System.out.println(strokeString);
                counter++;
            }

//            System.out.println("Type exit to exit.");
//            System.out.println("Please enter your User Name");
//            String nameInput = inputScanner.nextLine();
//            out.println("name=" + nameInput);

//            String clientMessage = ("name=" + inputScanner.nextLine());

//            while(true) {


//                out.println();

//                String clientMessage = inputScanner.nextLine();
//                if(clientMessage.equals("exit")) {
//                    break;
//                }
//                out.println(clientMessage);
//                System.out.println(in.readLine());
//                out.println(in.readLine());
//
//            out.println("I regret nothing... IN BRICE WE TRUST");
//
//                String serverRespone = in.readLine();
//            }

            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getJSONString(String jsonString) {
        strokeString = jsonString;
    }

}
