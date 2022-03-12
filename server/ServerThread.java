import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread extends Thread {
    Socket socket;
    DataInputStream in=null;
    DataOutputStream out=null;
    String hostaddress;
    ServerThread(Socket t,String hostaddress){
        socket=t;
        this.hostaddress=hostaddress;
        try {
            in=new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){}
    }
    @Override
    public void run(){
        while(true){
            ExecutorService executor = Executors.newFixedThreadPool(8);
            try {
                String str=in.readUTF();
                String parseTag="%([0-9]{1,2});";
                Pattern pTag = Pattern.compile(parseTag);
                Matcher mTag = pTag.matcher(str);
                Future<Boolean> future1;
                Future<Boolean> future2;
                Future<Boolean> future3;
                Future<String> future4;
                Future<String> future5;
                Future<String> future6;
                Future<String> future7;
                Future<Boolean> future8;
                Future<Boolean> future9;
                Future<String> future10;
                int tag=-1;
                if (mTag.find()) {
                    tag = Integer.parseInt(mTag.group(1));
                }

                if(tag==0){
                    future1 = executor.submit(new Register(str));
                    try {
                        if(future1.get()){
                            try {
                                out.writeUTF("注册成功\n");
                            } catch (IOException e) {
                            }
                        }
                        else{
                            try {
                                out.writeUTF("注册失败\n");
                            } catch (IOException e) {
                            }
                        }
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                }else if(tag==8){
                    future9=executor.submit(new Login(str,hostaddress,"admin_info","id:([a-zA-Z0-9]{4,10});"));
                    try{
                        if(future9.get()){
                            out.writeUTF("1");
                            System.out.println("管理员登录成功");
                        }else{
                            out.writeUTF("0");
                            System.out.println("管理员登录失败");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else if(tag==9){
                    future10=executor.submit(new outPdf());
                    try{
                        if(future10.get()!=null){
                            out.writeUTF(future10.get());
                            System.out.println(future10.get());
                        }else{
                            out.writeUTF("导出失败");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    future2 = executor.submit(new Login(str,hostaddress,"user_info","id:([0-9]{10});"));
                    try {
                        if (future2.get()) {
                            try {
                                /*out.writeUTF("登录成功\n");*/
                                out.writeUTF("1");
                                System.out.println("登录成功");
                                switch(tag){
                                    case 2:
                                        future3= executor.submit(new DelACC(str));
                                        if(future3.get()){
                                            out.writeUTF("销户成功\n");
                                        }
                                        else{
                                            out.writeUTF("销户失败\n");
                                        }
                                        break;
                                    case 3:
                                        future4= executor.submit(new CheckBlc(str));
                                        if(future4.get().equals("FALSE")){
                                            out.writeUTF("余额查询失败\n");
                                        }
                                        else{
                                            out.writeUTF("余额查询成功,账户余额为: "+future4.get()+"\n");
                                        }
                                        break;
                                    case 4:
                                        future5 = executor.submit(new DrawMoney(str));
                                        if(future5.get().equals("FALSE1")){
                                            out.writeUTF("取款失败,无此银行账户\n");
                                        }else if(future5.get().equals("FALSE3")){
                                            out.writeUTF("请输入正确的金额\n");
                                        }
                                        else if(future5.get().equals("FALSE2")){
                                            out.writeUTF("取款失败,余额不足\n");
                                        }else{
                                            out.writeUTF("取款成功，账户余额为: "+future5.get()+"\n");
                                        }
                                        break;
                                    case 5:
                                        future6 = executor.submit(new SaveMoney(str));
                                        if(future6.get().equals("FALSE")){
                                            out.writeUTF("存款失败，无此银行用户\n");
                                        }else if(future6.get().equals("FALSE1")){
                                            out.writeUTF("请输入正确的存款金额");
                                        }else{
                                            out.writeUTF("存款成功,账户余额为: "+future6.get()+"\n");
                                        }
                                        break;
                                    case 6:
                                        future7 = executor.submit(new Transfer(str));
                                        if(future7.get().equals("FALSE1")){
                                            out.writeUTF("转账失败，无此银行账户\n");
                                        }else if(future7.get().equals("FALSE2")){
                                            out.writeUTF("转账失败,账户余额不足\n");
                                        }else if(future7.get().equals("FALSE3")){
                                            out.writeUTF("请输入正确的金额\n");
                                        }
                                        else{
                                            out.writeUTF("转账成功，账户余额为: "+future7.get()+"\n");
                                        }
                                        break;
                                    case 7:
                                        future8 = executor.submit(new ChangeInfo(str));
                                        if(future8.get()){
                                            out.writeUTF("账户修改成功\n");
                                        }
                                        else{
                                            out.writeUTF("账户修改失败\n");
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } catch (IOException e) {
                            }
                        } else {
                            try {
                                out.writeUTF("0");
                            } catch (IOException e) {
                            }
                        }
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                }
            } catch (IOException e) {
                System.out.println("用户离开"+this.hostaddress);
                executor.shutdown();
                System.out.println("销户");
                break;
            }
        }
    }
}