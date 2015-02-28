package com.example.game;

/**
 * Created by gumin on 12/6/14.
 */

/**
 * Created by gumin on 12/6/14.
 */

/**
 * Created by gumin on 12/6/14.
 */

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Map;
        import java.util.Set;

/**
 * This is a simple server application. This server receive a string message
 * from the Android mobile phone and show it on the console.
 * Author by Lak J Comspace
 */
public class server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static String message;
    private static PrintWriter out;
    public static Map<String, String> userMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(7000); // Server socket

        } catch (IOException e) {
            System.out.println("Could not listen on port: 7000");
        }

        System.out.println("Server started. Listening to the port 7000");

        while (true) {
            try {

                clientSocket = serverSocket.accept(); // accept the client connection
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader); // get the client message
                message = bufferedReader.readLine();
                String[] userInfo = message.split(":");
                String score =  userMap.get(userInfo[0]);
                if(score != null)
                {
                    if(Integer.parseInt(score)<Integer.parseInt(userInfo[1]))
                    {
                        userMap.remove(userInfo[0]);
                        userMap.put(userInfo[0],userInfo[1]);
                    }

                }
                else
                {
                    userMap.put(userInfo[0],userInfo[1]);
                }
                Set keys = userMap.keySet();
                for (Iterator iter = keys.iterator(); iter.hasNext();)
                {
                    String key = (String) iter.next();
                    String value = (String) userMap.get(key);
                    System.out.println(key+":"+value);
                }
                System.out.println();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                System.out.println(out.checkError());
                out.println("hashcbasjcadsh");
                out.flush();
//                System.out.println(message);
                inputStreamReader.close();
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            }
        }

    }

}
