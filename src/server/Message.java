package server;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Message implements Serializable {

  private String name;
  private String message;
  private String users;
  private Date time;

  public Message(String name, String message){
    this.name = name;
    this.message = message;
    this.time = java.util.Calendar.getInstance().getTime();
  }

  public Message(String name, String message, String users){
    this.name = name;
    this.message = message;
    this.time = java.util.Calendar.getInstance().getTime();
    this.users = users;
  }

  public String getDate(){
    Time tm = new Time(this.time.getTime());
    return tm.toString();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsers() {
    return users;
  }

  public void setUsers(String users) {
    this.users = users;
  }
}
