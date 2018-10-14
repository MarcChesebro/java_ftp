import java.io.*;
import java.net.*;
import java.util.*;

public final class ftp_server {

    public static void main(String argv[]) throws Exception {

        String fromClient;
        String clientCommand;
        byte[] data;


        ServerSocket welcomeSocket = new ServerSocket(12000);
        String frstln;

        while (true) {
            //Wait for request at welcome socket
            Socket connectionSocket = welcomeSocket.accept();

            //create a new thread for the user
            user_thread = new ftp_thread(connectionSocket);

            // start the thread
            user_thread.start();
        }

    }
}
    
