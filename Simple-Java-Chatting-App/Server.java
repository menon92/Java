import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server 
{
    private static final int PORT = 9001;
    // to take an user only one time;
    private static HashSet<String> names = new HashSet<String>();
    // keep all the chat history here;
    private static HashSet<PrintWriter> writersAllMessage = new HashSet<PrintWriter>();

    public static void main(String[] args)
    {
        System.out.println("Server is running :)");
        System.out.println("Now Start your Client Program!");
        
        try 
        {
        	ServerSocket listener = new ServerSocket(PORT);
        
            while (true) 
            {
                new HandlerForMultipleClient(listener.accept()).start();
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    // Thread ..
    private static class HandlerForMultipleClient extends Thread
    {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        // constructor 
        public HandlerForMultipleClient(Socket socket) {
            this.socket = socket;
        }

        public void run() 
        {
            try 
            {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream())); // take input form client;
                out = new PrintWriter(socket.getOutputStream(), true); // send input to client;

                // keep requesting until name is submitt 
                while (true) 
                {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) 
                    {
                        if (!names.contains(name)) 
                        {
                            names.add(name);
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the
                writersAllMessage.add(out);

               // accepted message and print them :)
                while (true) 
                {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writersAllMessage) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } 
            catch (IOException e) 
            {
                System.out.println(e);
            } 
            finally 
            {
                //close all the stuff's;
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writersAllMessage.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
