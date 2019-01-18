package run;

import client.ClientLogic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class RunClient {


    public static String ipAddr = "localhost";
    public static int port = 8082;

    /**
     * создание клиент-соединения с узананными адресом и номером порта
     * @param args
     */

    public static void main(String[] args) {
      new ClientLogic(ipAddr, port);
    }



    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;

    ClientLogic cl = new ClientLogic(ipAddr, port);






}
