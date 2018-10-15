import java.net.*;
import java.io.*;
import java.util.*;

final class ftp_thread implements Runnable {
    private static int threadCount;
    private int user;
    private Socket controlConnection;
    private String statusOk = "200 OK\n";
    private String statusMissing = "550 File Not Found\n";

    // Constructor
    // pass the control connection socket in
    public ftp_thread(Socket socket) throws Exception {
        this.controlConnection = socket;
    }

    // this runs on start after the thread has been setup
    public void run() {
        try {
            processCommand();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // this will watch the control connection and execute the commands
    private void processCommand() throws Exception {

        // wrap input and output in buffered streams
        DataOutputStream outToClient = new DataOutputStream(controlConnection.getOutputStream());
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(controlConnection.getInputStream()));
	user = threadCount;
	System.out.println("Client" + threadCount++ + " has connected!");
        // read input from user
        while (true) {

            String fromClient = inFromClient.readLine();
            if (fromClient == null){
                break;
            }
            StringTokenizer tokens = new StringTokenizer(fromClient);
            String frstln = tokens.nextToken();
            int port = Integer.parseInt(frstln);

            String clientCommand = tokens.nextToken();

            // each command should create a data socket and execute the command


            System.out.println("Client" + user + ": " + clientCommand);
            // list command
            if (clientCommand.equals("list:")) {

                Socket dataSocket = new Socket(controlConnection.getInetAddress(), port);
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                outToClient.writeBytes(statusOk);

                //TODO print list
                File folder = new File("./media");
                String[] files = folder.list();
                if (files != null) {
		            dataOutToClient.writeBytes("\nFiles:\n");
                    for (String file : files) {
                        dataOutToClient.writeBytes(file + "\n");
                    }
		            dataOutToClient.writeBytes("(End of Files)\n");
                } else {
                    dataOutToClient.writeBytes("There are no files");
                }
                dataSocket.close();
                System.out.println("Data Socket closed");
            }


            if (clientCommand.equals("retr:")) {
                Socket dataSocket = new Socket(controlConnection.getInetAddress(), port);
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                String filename = tokens.nextToken();
                File f = new File("./media/" + filename);
                if (!f.exists() || f.isDirectory()){
                    outToClient.writeBytes(statusMissing);
                    continue;
                }

                outToClient.writeBytes(statusOk);

                BufferedReader fileOut = new BufferedReader(new FileReader("./media/" + filename));
                String line = fileOut.readLine();

                while(line != null){
                    dataOutToClient.writeBytes(line + "\n");
                    line = fileOut.readLine();
                }
                dataSocket.close();
		fileOut.close();
            }

            if (clientCommand.equals("stor:")) {
                Socket dataSocket = new Socket(controlConnection.getInetAddress(), port);
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                outToClient.writeBytes(statusOk);

                String filename = tokens.nextToken();

                BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                BufferedWriter toFile = new BufferedWriter(new FileWriter("./media/" + filename));
                String line = inData.readLine();
                while(line != null){
                    toFile.write(line);
                    toFile.newLine();
                    line = inData.readLine();
                }
		dataOutToClient.close();
		inData.close();
                toFile.close();
                dataSocket.close();
            }

            if (clientCommand.equals("quit:")) {
                System.out.println("Client" + user + " has disconnected.");
		break;
            }
        }

        controlConnection.close();
    }
}
