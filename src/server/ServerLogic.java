package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogic {

  private int port;
  private static UserList userList = new UserList();
  private Socket clientSocket;
  private ServerSocket serverSocket;

  public ServerLogic(int port) throws IOException, ClassNotFoundException {
    this.serverSocket = new ServerSocket(port);
    //this.userList = new UserList();
  }

  public void processServer() throws IOException {
    String name;
    try {
      while (true) {
        this.clientSocket = null;
        while (clientSocket == null) {
          clientSocket = this.serverSocket.accept();
          try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            name = (String) in.readLine();
            this.userList.addUser(name, clientSocket, in, out);
            if(this.userList.getNames() != null) {
              out.writeObject(this.userList.getNames());
              out.flush();
            }

          } catch (IOException ie) {
            System.err.println("I/O exception");
            ie.printStackTrace();
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
