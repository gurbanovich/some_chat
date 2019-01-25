package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import server.Message;
//import sun.util.calendar.LocalGregorianCalendar.Date;

public class ClientLogic {

  private Socket socket;
  private ObjectInputStream is; // поток чтения из сокета
  //private BufferedWriter out; // поток чтения в сокет
  private ObjectOutputStream os;
  private BufferedReader inputUser; // поток чтения с консоли
  private String addr; // ip адрес клиента
  private int port; // порт соединения
  private String nickname; // имя клиента
  // текущая дата
  private Date time = new Date();
  private Date dtime;
  private SimpleDateFormat dt1;
  private String name;

  /**
   * для создания необходимо принять адрес и номер порта
   */

  public ClientLogic(String addr, int port) {
    this.addr = addr;
    this.port = port;
    try {
      this.socket = new Socket(addr, port);
    } catch (IOException e) {
      System.err.println("Socket failed");
    }
    try {
      // потоки чтения из сокета / записи в сокет, и чтения с консоли
      inputUser = new BufferedReader(new InputStreamReader(System.in));
      is = new ObjectInputStream(socket.getInputStream());
      os = new ObjectOutputStream(socket.getOutputStream());
      //this.pressNickname(); // перед началом необходимо спросит имя

      System.out.print("Press your nick:   ");
      try {
        this.name = inputUser.readLine();
        os.writeObject("Hello " + name + "\n");
        os.flush();
      } catch (IOException ignored) {
      }

      new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
      new WriteMsg(this.name)
          .start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
    } catch (IOException e) {
      // Сокет должен быть закрыт при любой
      // ошибке, кроме ошибки конструктора сокета:
      ClientLogic.this.downService();
    }
    // В противном случае сокет будет закрыт
    // в методе run() нити.
  }

  /**
   * просьба ввести имя,
   * и отсылка эхо с приветсвием на сервер
   */

  /*private void pressNickname() {
    System.out.print("Press your nick: ");
    try {
      nickname = inputUser.readLine();
      out.write("Hello " + nickname + "\n");
      out.flush();
    } catch (IOException ignored) {
    }

  }*/

  /**
   * закрытие сокета
   */
  private void downService() {
    try {
      if (!socket.isClosed()) {
        socket.close();
        is.close();
        os.close();
        os.close();
      }
    } catch (IOException ignored) {
    }
  }


  // нить чтения сообщений с сервера
  private class ReadMsg extends Thread {

    @Override
    public void run() {

      String str;
      try {
        while (true) {
          str = (String) is.readLine(); // ждем сообщения с сервера
          if (str.equals("stop")) {
            ClientLogic.this.downService(); // харакири
            break; // выходим из цикла если пришло "stop"
          }
          System.out.println(str); // пишем сообщение с сервера на консоль
        }
      } catch (IOException e) {
        ClientLogic.this.downService();
      }
    }
  }

  // нить отправляющая сообщения приходящие с консоли на сервер
  public class WriteMsg extends Thread {

    private String name;

    public WriteMsg(String name) {
      this.name = name;
    }

    @Override
    public void run() {
      while (true) {
        String userWord;
        try {
          //dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
          //dtime = time; // время
          userWord = inputUser.readLine(); // сообщения с консоли
          if (userWord.equals("stop")) {
            Message mes = new Message(name, userWord);
            os.writeObject(mes); // отправляем на сервер
            os.flush();
            ClientLogic.this.downService(); // харакири
            break; // выходим из цикла если пришло "stop"
          } else if (userWord.equals("get users")) {
            System.out.println(userWord);
            Message mes = new Message(name, userWord);
            os.writeObject(mes); // отправляем на сервер
           // os.flush();
          } else {
            System.out.println(userWord);
            String[] fn = userWord.split("\\#");
            System.out.println();
            String userName = fn[0];
            System.out.println(userName);
            String message = fn[1];
            System.out.println(message);
            Message mes = new Message(name, message, userName);
            System.out.println(mes.getMessage());
            os.writeObject(mes); // отправляем на сервер
            os.flush();
          }
          os.flush(); // чистим
          os.flush();
        } catch (IOException e) {
          ClientLogic.this.downService(); // в случае исключения тоже харакири

        }

      }
    }
  }
}


