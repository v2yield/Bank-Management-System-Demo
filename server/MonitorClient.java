import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * @author v2yield
 */
public class MonitorClient {
    public static void main(String  args[]) {
        ServerSocket server=null;
        Socket client=null;
        try {
            server=new ServerSocket(4331);
        }catch(IOException e1){
            System.out.println("正在监听");
        }
        while(true) {
            try {
                System.out.println("正在监听");
                client=server.accept();
                System.out.println("客户的地址:"+client.getInetAddress());
            }catch (IOException e) {
                System.out.println("正在等待客户");
            }
            if(client!=null) {
                //为每个客户启动一个专门的线程。
                new ServerThread(client,client.getInetAddress().toString()).start();
            }else{continue;}
        }
    }
}


