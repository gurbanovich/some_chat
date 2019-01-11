package server;

import static server.ServerLogic.getUserList;

import client.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.Timer;

public class ServerThread extends Thread {

  private final static int DELAY = 10000;

  private Socket socket;
  private Message mes;
  private String name;
  private int inPacks = 0;
  private int outPacks = 0;
  private boolean flag = false;
  private Timer timer;

  public ServerThread(Socket socket) throws ClassNotFoundException, IOException {
    this.socket = socket;
    this.start();
  }

  public void run() {
  try {
    final ObjectInputStream inputStream   = new ObjectInputStream(this.socket.getInputStream());
    final ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream());

    this.mes = (Message) inputStream.readObject();
    this.name = this.mes.getName();


    if (! this.mes.getMessage().equals("HELLO MESSAGE")) {
      System.out.println("[" + this.mes.getName() + "]: " + this.mes.getMessage());
    //  getChatHistory().addMessage(this.c);
    } else {
      //outputStream.writeObject(getChatHistory());
      this.broadcast(getUserList().getClientsList(), new Message("Server-Bot", "The user " + name + " has been connect"));
    }
    getUserList().addUser(name, socket, outputStream, inputStream);
// СДелать выбор клиента
    this.mes.setUsers(getUserList().getNames());
    this.broadcast(getUserList().getClientsList(), this.mes);

    /*this.timer = new Timer(DELAY, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          if (inPacks == outPacks) {
            outputStream.writeObject(new Ping());
            outPacks++;
            System.out.println(outPacks + " out");
          } else {
            throw new SocketException();
          }
        } catch (SocketException ex1) {
          System.out.println("packages not clash");
          System.out.println(name + " disconnected!");
          getUserList().deleteUser(name);
          broadcast(getUserList().getClientsList(), new Message("Server-Bot", "The user " + name + " has been disconnect", getUserList().getNames()));
          flag = true;
          timer.stop();
        }  catch (IOException ex2) {
          ex2.printStackTrace();
        }
      }
    });

    this.timer.start();
    outputStream.writeObject(new Ping());
    this.outPacks++;
    System.out.println(outPacks + " out");

    while (true) {
      if(this.flag) {
        this.flag = false;
        break;
      }
      this.mes = (Message) inputStream.readObject();

      if (this.mes instanceof Ping) {
        this.inPacks++;
        System.out.println(this.inPacks + " in");

      } else if (! mes.getMessage().equals("HELLO MESSAGE")) {
        System.out.println("[" + name + "]: " + mes.getMessage());
        //getChatHistory().addMessage(this.c);

      } else {
        //outputStream.writeObject(getChatHistory());
        this.broadcast(getUserList().getClientsList(), new Message("Server-Bot", "The user " + name + " has been connect"));
      }

      this.mes.setUsers(getUserList().getNames());

      if (! (mes instanceof Ping) && ! mes.getMessage().equals("HELLO MESSAGE")) {
        System.out.println("Send broadcast Message: " + mes.getMessage() + "");
        this.broadcast(getUserList().getClientsList(), this.mes);
      }
    }*/

  } catch (SocketException e) {
    System.out.println(name + " disconnected!");
    getUserList().deleteUser(name);
    broadcast(getUserList().getClientsList(), new Message("Server-Bot", "The user " + name + " has been disconnect", getUserList().getNames()));
    this.timer.stop();
  } catch (IOException e) {
    e.printStackTrace();
  } catch (ClassNotFoundException e) {
    e.printStackTrace();
  }


  }

  private void broadcast(ArrayList<Client> clientsArrayList, Message message) {
    try {
      for (Client client : clientsArrayList) {
        client.getOs().writeObject(message);
      }
    } catch (SocketException e) {
      System.out.println("in broadcast: " + name + " disconnected!");
      getUserList().deleteUser(name);
      this.broadcast(getUserList().getClientsList(), new Message("Server-Bot", "The user " + name + " has been disconnected", getUserList().getNames()));

      timer.stop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
