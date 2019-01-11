package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

  private String name;
  private Socket socket;
  private ObjectOutputStream os;
  private ObjectInputStream is;

  public Client(Socket socket, ObjectOutputStream os, ObjectInputStream is) {
  }


  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public ObjectOutputStream getOs() {
    return os;
  }

  public void setOs(ObjectOutputStream os) {
    this.os = os;
  }

  public ObjectInputStream getIs() {
    return is;
  }

  public void setIs(ObjectInputStream is) {
    this.is = is;
  }
}
