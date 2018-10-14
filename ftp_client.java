import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class ftp_client {

    public static void main(String[] args) throws Exception {
        String sentence;
        String modifiedSentence;
        boolean isOpen = true;
        int controlPort = 1;
        boolean notEnd = true;
        String statusCode;
        boolean clientgo = true;


        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        sentence = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(sentence);


        if (sentence.startsWith("connect")) {
            String serverName = tokens.nextToken(); // pass the connect command
            serverName = tokens.nextToken();
            controlPort = Integer.parseInt(tokens.nextToken());
            System.out.println("You are connected to " + serverName);

            Socket ControlSocket = new Socket(serverName, controlPort);

            DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());

            DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));

            while (isOpen && clientgo) {

                sentence = inFromUser.readLine();

                System.out.println(sentence);
                if (sentence.equals("list:")) {

                    int dataPort = controlPort + 2;
                    outToServer.writeBytes(dataPort + " " + sentence + " " + '\n');

                    ServerSocket welcomeData = new ServerSocket(dataPort);
                    Socket dataSocket = welcomeData.accept();

                    BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

                    String line = inData.readLine();
                    while(line != null){
                        System.out.println(line);
                        line = inData.readLine();
                    }

                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || close");

                } else if (sentence.startsWith("retr: ")) {

                }
            }
        }
    }
}
