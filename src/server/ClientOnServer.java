package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientOnServer extends Thread {

  private final static int DELAY = 10000;

  private Socket socket;
  private Message mes;
  private String name;
  private ObjectOutputStream os;
  private ObjectInputStream is;

  public ClientOnServer(Socket socket, ObjectInputStream is, ObjectOutputStream os) throws ClassNotFoundException, IOException {
    this.socket = socket;
    this.is = is;
    this.os = os;
    this.start();
  }

  public void run() {
    try {
       /*is = new ObjectInputStream(this.socket.getInputStream());
       ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream());
       BufferedWriter out = new BufferedWriter(
          new OutputStreamWriter(socket.getOutputStream()));*/
      /*InputStream isss = this.socket.getInputStream();
      System.out.println(isss.toString());
      ObjectInputStream iss = new ObjectInputStream(isss);*/
      this.mes = (Message) is.readObject();
      this.name = this.mes.getName();
      for (ClientOnServer cos : ServerLogic.getUserList().getClientsList()) {
        cos.os.writeObject(this.name);
        cos.os.flush();
      }

      while (true) {
        if (this.mes.getMessage().equalsIgnoreCase("get users")) {
          this.os.writeObject(ServerLogic.getUserList().getNames());
          this.os.flush();
        } else if (this.mes.getMessage().equalsIgnoreCase("stop")) {

        } else {
          this.mes = (Message) is.readObject();
          String words = mes.getMessage();
          String date = mes.getDate();
          String users = mes.getUsers();
          String outMessage = "Encoding: " + name + ": " + date + " " + words;
          ClientOnServer cos = ServerLogic.getUserList().getUser(name);
          cos.os.writeObject(outMessage);
          cos.os.flush();

        }
      }

    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
    }

  }

  /*private void broadcast(ArrayList<Client> clientsArrayList, Message message) {
    try {
      for (Client client : clientsArrayList) {
        client.getOs().writeObject(message);
      }
    } catch (SocketException e) {
      System.out.println("in broadcast: " + name + " disconnected!");
      getUserList().deleteUser(name);
      this.broadcast(getUserList().getClientsList(),
          new Message("Server-Bot", "The user " + name + " has been disconnected",
              getUserList().getNames()));

      timer.stop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

}
