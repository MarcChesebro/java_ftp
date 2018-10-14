import java.net.*;
import java.io.*;
import java.util.*;

final class ftp_thread implements Runnable{

    private Socket controlConnection;

    // Constructor
    // pass the control connection socket in
    public ftp_thread(Socket socket) throws Exception {
        this.controlConnection = socket;
    }

    // this runs on start after the thread has been setup
    public void run(){
        try{
            processCommand();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    // this will watch the control connection and execute the commands
    private void processCommand() throws Exception{

        // wrap input and output in buffered streams
        DataOutputStream outToClient = new DataOutputStream(controlConnection.getOutputStream());
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(controlConnection.getInputStream()));

        // read input from user
        while(true){

            String fromClient = inFromClient.readLine();

            StringTokenizer tokens = new StringTokenizer(fromClient);
            String frstln = tokens.nextToken();
            int port = Integer.parseInt(frstln);

            String clientCommand = tokens.nextToken();

            // each command should create a data socket and execute the command


            System.out.println(clientCommand);
            // list command
            if (clientCommand.equals("list:")) {

                Socket dataSocket = new Socket(controlConnection.getInetAddress(), port);
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                //TODO print list
                File folder = new File("./media");
                String[] files = folder.list();
                if (files != null) {
                    for (String file : files) {
                        dataOutToClient.writeBytes(file);
                    }
                }else{
                    dataOutToClient.writeBytes("There are no files");
                }
                dataSocket.close();
                System.out.println("Data Socket closed");
            }


            if (clientCommand.equals("retr:")) {

            }

            if (clientCommand.equals("stor:")) {

            }

            if (clientCommand.equals("quit:")) {

            }

            if (clientCommand.equals("connect:")) {

            }
        }
    }
}
