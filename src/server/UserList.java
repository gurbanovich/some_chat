package server;

import client.Client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserList {

  private Map<String, Client> onlineUsers = new HashMap<String, Client>();

  public void addUser(String name, Socket socket) {
    System.out.println( name +" connected" );

    if (!this.onlineUsers.containsKey(name)) {
      this.onlineUsers.put(name , new Client(socket));
    } else {
      int i = 1;
      while(this.onlineUsers.containsKey(name)) {
        name = name + i;
        i++;
      }
      this.onlineUsers.put(name , new Client(socket));
    }
  }

  public void deleteUser(String name) {
    this.onlineUsers.remove(name);
  }

  public String getNames() {
    String[] names = this.onlineUsers.keySet().toArray(new String[0]);
    String strNames = null;
    for(String name : names) {
      strNames = strNames + " \n" +name;
    }
    return strNames;
  }

  public ArrayList<Client> getClientsList() {
    ArrayList<Client> clientsList = new ArrayList<Client>(this.onlineUsers.entrySet().size());

    //String s = "";
    for(Map.Entry<String, Client> m : this.onlineUsers.entrySet()){
      clientsList.add(m.getValue());
      System.out.println(m.getKey());
     // s = s + m.getKey();
    }

    return clientsList;
  }

  
  
}
