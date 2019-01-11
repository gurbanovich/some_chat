package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogic {

  private static int port;
  private static UserList userList;
  private Socket clientSocket;
  private ServerSocket serverSocket;

  public ServerLogic(int port) throws IOException, ClassNotFoundException {
    this.serverSocket = new ServerSocket(port);
    this.userList = new UserList();
  }

  public void processServer() throws IOException {
    String name;
    try {
      while (true) {
        this.clientSocket = null;
        while (clientSocket == null) {
          clientSocket = this.serverSocket.accept();
          try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()));
            name = in.readLine();
            out.write(this.userList.getNames());
            out.flush();
            this.userList.addUser(name, clientSocket);
          } catch (IOException ie) {
            System.err.println("I/O exception");
            ie.printStackTrace();
          }
        }
      }
    } finally {
      serverSocket.close();
    }
  }

  public synchronized static UserList getUserList() {
    return userList;
  }

}
