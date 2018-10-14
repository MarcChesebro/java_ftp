
final class ftp_thread implements Runnable{

    private Socket controlConnection;

    // Constructor
    // pass the control connection socket in
    public HttpRequest(Socket socket) throws Exception {
        this.controlConnection = socket;
    }

    // this runs on start after the thread has been setup
    public void run(){
        try{
            processCommand();
        } catch (Exeption e){
            System.out.println(e);
        }
    }

    // this will watch the control connection and execute the commands
    private void processCommand(){

        // wrap input and output in buffered streams
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        // read input from user
        while(true){

            fromClient = inFromClient.readLine();

            StringTokenizer tokens = new StringTokenizer(fromClient);
            frstln = tokens.nextToken();
            port = Integer.parseInt(frstln);

            clientCommand = tokens.nextToken();

            // each command should create a data socket and execute the command

            // list command
            if (clientCommand.equals("list:")) {

                Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                //TODO print list

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