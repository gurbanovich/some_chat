package run;

import java.io.IOException;
import server.ServerLogic;

public class RunServer {

  private static int port = 8082;

  public static void main(String[] args) throws IOException, ClassNotFoundException {

    ServerLogic st = new ServerLogic(port);

    st.processServer();

  }

}
