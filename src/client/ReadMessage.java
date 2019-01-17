package client;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadMessage extends Thread {

  private BufferedReader in;
  ClientLogic clientLogic;

  @Override
  public void run() {

    String str;
    try {
      while (true) {
        str = in.readLine(); // ждем сообщения с сервера
        if (str.equals("stop")) {
          ClientLogic.downService(); // харакири
          break; // выходим из цикла если пришло "stop"
        }
        System.out.println(str); // пишем сообщение с сервера на консоль
      }
    } catch (IOException e) {
      ClientLogic.downService();
    }
  }

}
