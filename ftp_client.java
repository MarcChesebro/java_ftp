import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class ftp_client {

    public static void main(String[] args) throws Exception {
        String sentence;
        int controlPort = 1;


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

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(ControlSocket.getInputStream()));

            System.out.println("\nWhat would you like to do: \n" +
                    " list: file.txt || retr: file.txt ||stor: file.txt  || close");
            sentence = inFromUser.readLine();

            while (!sentence.startsWith("quit")) {

                if (sentence.equals("list:")) {

                    int dataPort = controlPort + 2;


                    outToServer.writeBytes(dataPort + " " + sentence + " " + '\n');

                    ServerSocket welcomeData = new ServerSocket(dataPort);
                    Socket dataSocket = welcomeData.accept();

                    // Chesck for status code 200 OK
                    String statusCode = inFromServer.readLine();
                    if (!statusCode.startsWith("200")){
                        // If it was not 200 return bad status code
                        System.out.println(statusCode);
                        welcomeData.close();
                        dataSocket.close();
                        System.out.println("\nWhat would you like to do: \n" +
                                " list: file.txt || retr: file.txt ||stor: file.txt  || close");
                        sentence = inFromUser.readLine();
                        continue;

                    }

                    BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

                    String line = inData.readLine();
                    while(line != null){
						
                        System.out.println(line);
                        line = inData.readLine();
                    }

                    welcomeData.close();
                    dataSocket.close();

                } else if (sentence.startsWith("retr:")) {


                    int dataPort = controlPort + 2;


                    outToServer.writeBytes(dataPort + " " + sentence + " " + '\n');

                    ServerSocket welcomeData = new ServerSocket(dataPort);
                    Socket dataSocket = welcomeData.accept();

                    StringTokenizer user_tokens = new StringTokenizer(sentence);
                    user_tokens.nextToken(); // skip command
                    String filename = user_tokens.nextToken();

                    // Chesck for status code 200 OK
                    String statusCode = inFromServer.readLine();
                    if (!statusCode.startsWith("200")){
                        // If it was not 200 return bad status code
                        System.out.println(statusCode);
                        welcomeData.close();
                        dataSocket.close();
                        System.out.println("\nWhat would you like to do next: \n" +
                                " list: file.txt || retr: file.txt ||stor: file.txt  || quit");
                        sentence = inFromUser.readLine();
                        continue;
                    }


                    BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                    BufferedWriter toFile = new BufferedWriter(new FileWriter(filename));
                    String line = inData.readLine();
                    while(line != null){
			toFile.write(line);
                        toFile.newLine();
			line = inData.readLine();
                    }
	            toFile.close();
                    welcomeData.close();
                    dataSocket.close();


                } else if (sentence.startsWith("stor:")) {

                } else if (sentence.startsWith("quit:")) {
                    System.out.println("Exiting.....");
                } else{
                    System.out.println("Invalid Command");
                }

                System.out.println("\nWhat would you like to do next: \n" +
                        " list: file.txt || retr: file.txt ||stor: file.txt  || quit");
                sentence = inFromUser.readLine();

            }

            ControlSocket.close();
            System.out.println("Conection Closed");
        }
    }
}
