package Flybank.Client;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

import static com.sun.javafx.application.PlatformImpl.exit;

class user {
    static String uid;
    static String pas;
}
public class Test  {

    public Test() {
        new shouye();
    }
    public static void main(String[] args) {
        new Test();
        user thisuser =new user();
    }
}
//shouye 类
class shouye extends JFrame implements ActionListener {
    JButton but1,but2,but3,but4,but5;
    Box box1,box2,box3,box4,box5;

    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    Thread thread;
    String str;
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==but1){
            new kaihu();
            dispose();
        }
        else if(e.getSource()==but2){
            new log(1);
            dispose();
        }else if(e.getSource()==but3){
            if(socket.isConnected()){
            }else{
                try {
                    InetAddress address = InetAddress.getByName("150.158.32.175");
                    InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
                    socket.connect(socketAddress);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    if(socket.isConnected()){
                        but1.setEnabled(true);
                        but2.setEnabled(true);
                        but5.setEnabled(true);
                    }
                }catch (IOException e1){
                    System.out.println("与服务器已断开");
                }
            }
        }else if(e.getSource()==but4){
            System.exit(0);
        }else if(e.getSource()==but5){
            new log(8);
            dispose();
        }
    }
    public  shouye(){
        socket=new Socket();
        box1=Box.createHorizontalBox();
        box2=Box.createHorizontalBox();
        box3=Box.createHorizontalBox();
        box4=Box.createHorizontalBox();
        box5=Box.createHorizontalBox();
        but1=new JButton("开户");
        but2=new JButton("登录");
        but3=new JButton("连接服务器");
        but4=new JButton("退出");
        but5=new JButton("管理员登录");
        box1.add(but1);box2.add(but2);box5.add(but5);box3.add(but3);box4.add(but4);
        but1.setEnabled(false);
        but2.setEnabled(false);
        but5.setEnabled(false);
        but1.addActionListener(this);
        but2.addActionListener(this);
        but3.addActionListener(this);
        but4.addActionListener(this);
        but5.addActionListener(this);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        con.add(box1);
        con.add(box2);
        con.add(box5);
        con.add(box3);
        con.add(box4);
        setSize(500,100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
//mod 类 修改信息 mod(1)改密码 mod2 改手机号 mod3改名字
class mod extends JFrame {
    mod(int mode){
        if(mode==1) {
            new trans(4);
            dispose();
        } else if(mode ==2) {
            new trans(5);
        } else if(mode ==3) {
            new trans(6);
        }
    }
}
//modify类 修改信息界面
class modify extends JFrame implements ActionListener   {
    JButton but1,but2,but3,but4;
    Box box1,box2,box3;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    @Override
    public void actionPerformed(ActionEvent e){
        try {
            if (e.getSource() == but1) {
                out.writeUTF("%1;id:" + user.uid + ";passwd:" + user.pas + ";");
                String str = in.readUTF();
                if (str.equals("0")) {
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                } else {
                    new mod(1);
                }
            } else if (e.getSource() == but2) {
                out.writeUTF("%1;id:" + user.uid + ";passwd:" + user.pas + ";");
                String str = in.readUTF();
                if (str.equals("0")) {
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                } else {
                    new mod(2);
                }
            } else if (e.getSource() == but3) {
                out.writeUTF("%1;id:" + user.uid + ";passwd:" + user.pas + ";");
                String str = in.readUTF();
                if (str.equals("0")) {
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                } else {
                    new mod(3);
                }
            } else if (e.getSource() == but4) {
                new main();
                dispose();
            }
        }catch (IOException ee){
        }
    }
    public modify(){
        socket=new Socket();
        try {
            InetAddress address = InetAddress.getByName("150.158.32.175");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ee) {
        }
        but1=new JButton("修改密码");
        but2=new JButton("修改手机号");
        but3=new JButton("修改名字");
        but4=new JButton("退出");
        box1 =Box.createHorizontalBox();box2=Box.createHorizontalBox();box3=Box.createVerticalBox();
        box1.add(but1);
        box2.add(but2);
        box3.add(but3);
        Container con=getContentPane();
        con.add(box1);con.add(box2);con.add(box3);con.add(but4);
        con.setLayout(new FlowLayout());
        but1.addActionListener(this);but2.addActionListener(this);but3.addActionListener(this);but4.addActionListener(this);
        setSize(400,100);
        setVisible(true);
    }
}
//trans类 trans(1)转账 trans(2)存钱 trans(3)取钱
class trans extends JFrame implements ActionListener   {
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    Box box1,box2,box3;
    JButton conf,conf2,conf3,conf4,conf5,conf6,conf7,conf8,conf9,conf10,conf11,conf12;
    JTextField aimUser,money;
    @Override
    public void actionPerformed(ActionEvent e){
        try{
            InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ee){
        }
        if(e.getSource()==conf){
            try{
                out.writeUTF("%6;id:"+user.uid+";passwd:"+user.pas+";id2:"+aimUser.getText().toString().trim()+";transferm:"+money.getText().toString().trim()+";");
                String str =in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }
                else{
                String str1=in.readUTF();
                JOptionPane.showMessageDialog(null, str1, "标题",JOptionPane.INFORMATION_MESSAGE);
                new main();
                }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf2){
            try{
                out.writeUTF("%5;id:"+user.uid+";passwd:"+user.pas+";"+"save:"+money.getText().toString().trim()+";");
                String str =in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                    String str1 = in.readUTF();
                    JOptionPane.showMessageDialog(null, str1, "标题", JOptionPane.INFORMATION_MESSAGE);
                    new main();
                }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf3){
            try {
                out.writeUTF("%4;id:" + user.uid + ";passwd:" + user.pas + ";" + "draw:" + money.getText().toString().trim() + ";");
                String str = in.readUTF();
                if (str.equals("0")) {
                    JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                } else{
                    String str1 = in.readUTF();
                    JOptionPane.showMessageDialog(null, str1, "标题", JOptionPane.INFORMATION_MESSAGE);
                    new main();
            }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf4){
            try{
                if(money.getText().toString().trim().length()>=4){
                    String str;
                    String str1;
                    out.writeUTF("%7;id:"+user.uid+";passwd:"+user.pas+";"+"passwd1:"+money.getText().toString().trim()+";");//修改密码
                    str =in.readUTF();
                    if(str.equals("0")){
                        JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                        dispose();
                        new shouye();
                    }else{
                        str1=in.readUTF();
                        JOptionPane.showMessageDialog(null, str1, "标题",JOptionPane.INFORMATION_MESSAGE);
                        out.writeUTF("%7;id:"+user.uid+";passwd:"+user.pas+";");
                        System.out.println("%7;id:"+user.uid+";passwd:"+user.pas+";");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "出现非法字符,修改失败", "标题",JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf5){
            try{
                if(money.getText().toString().trim().length()==11){
                    out.writeUTF("%7;id:"+user.uid+";passwd:"+user.pas+";"+"phone:"+money.getText().toString().trim()+";");//修改手机号
                    String str =in.readUTF();
                    if(str.equals("0")){
                        JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                        dispose();
                        new shouye();
                    }else {
                        String str1 = in.readUTF();
                        JOptionPane.showMessageDialog(null, str1, "标题", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "修改失败", "标题",JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf6){
            try{
                if(money.getText().toString().trim().length()>0){
                    out.writeUTF("%7;id:"+user.uid+";passwd:"+user.pas+";"+"name:"+money.getText().toString().trim()+";");//修改名字
                    String str =in.readUTF();
                    if(str.equals("0")){
                        JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                        dispose();
                        new shouye();
                    }else {
                        String str1 = in.readUTF();
                        JOptionPane.showMessageDialog(null, str1, "标题", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "修改失败", "标题",JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf7 || e.getSource()==conf8 || e.getSource()==conf9){
            dispose();
            new main();
        }
        dispose();
    }
    public trans(int mode){
        socket =new Socket();
        if(mode==1){
            aimUser=new JTextField("uid",15);
            money=new JTextField("金额",15);
            conf=new JButton("确定");
            conf9=new JButton("退出");
            box1 =Box.createHorizontalBox();box2=Box.createHorizontalBox();box3=Box.createVerticalBox();
            box1.add(new Label("目标用户"));box1.add(aimUser);
            box2.add(new Label("转账金额"));box2.add(money);
            Container con=getContentPane();
            con.add(box1);con.add(box2);con.add(conf);
            con.add(conf9);
            con.setLayout(new FlowLayout());
            conf.addActionListener(this);
            conf9.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }
        else if(mode==2){
            money=new JTextField("金额",15);
            conf2=new JButton("确定");
            conf8=new JButton("退出");
            box2=Box.createVerticalBox();
            box2.add(new Label("存钱金额"));
            box2.add(money);
            Container con=getContentPane();
            con.add(box2);
            con.add(conf2);
            con.add(conf8);
            con.setLayout(new FlowLayout());
            conf2.addActionListener(this);
            conf8.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }
        else if(mode==3){
            money=new JTextField("金额",15);
            conf3=new JButton("确定");
            conf7=new JButton("退出");

            box2=Box.createVerticalBox();
            box2.add(new Label("取钱金额"));
            box2.add(money);
            Container con=getContentPane();
            con.add(box2);
            con.add(conf3);
            con.add(conf7);
            con.setLayout(new FlowLayout());
            conf3.addActionListener(this);
            conf7.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }else if(mode==4){
            money=new JTextField("密码",15);
            conf4=new JButton("确定");
            conf10=new JButton("退出");
            box2=Box.createVerticalBox();
            box2.add(new Label("修改密码"));box2.add(money);
            Container con=getContentPane();
            con.add(box2);con.add(conf4);con.add(conf10);
            con.setLayout(new FlowLayout());
            conf4.addActionListener(this);
            conf10.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }	else if(mode==5){
            money=new JTextField("手机号",15);
            conf5=new JButton("确定");
            conf11 =new JButton("退出");
            box2=Box.createVerticalBox();
            box2.add(new Label("修改手机"));box2.add(money);
            Container con=getContentPane();
            con.add(box2);con.add(conf5);con.add(conf11);
            con.setLayout(new FlowLayout());
            conf5.addActionListener(this);
            conf11.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }else if(mode==6){
            money=new JTextField("姓名",15);
            conf6=new JButton("确定");conf12=new JButton("退出");
            box2=Box.createVerticalBox();
            box2.add(new Label("修改名字"));box2.add(money);
            Container con=getContentPane();
            con.add(box2);con.add(conf6);con.add(conf12);
            con.setLayout(new FlowLayout());
            conf6.addActionListener(this);
            conf12.addActionListener(this);
            setSize(300,150);
            setVisible(true);
        }
    }
}

//refer类 查询余额
class refer extends JFrame  {
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    public refer(){
        socket =new Socket();
        try{
            InetAddress address = InetAddress.getByName("150.158.32.175");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("%3;id:"+user.uid+";passwd:"+user.pas+";");
            String  str=in.readUTF();
            if(str.equals("0")){
                JOptionPane.showMessageDialog(null, "密码已修改,请重新登录", "标题",JOptionPane.WARNING_MESSAGE);
                dispose();
                new shouye();
            }else {
                String yue1 = in.readUTF();
                JOptionPane.showMessageDialog(null, yue1, "标题", JOptionPane.INFORMATION_MESSAGE);
                new main();
            }
        }catch (IOException ee){
        }
    }
}

//main类
class main extends JFrame implements ActionListener {
    JButton connection, computer, botton1, botton2, botton3, botton4, botton5, botton6, botton7, botton8, botton9, botton10,botton11;
    Box boxV1, boxV2, boxV3, boxV4, boxV5, boxV6, boxV7, boxV8, boxV9, boxV10,boxV11;
    JTextField id;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    public main(){
        socket=new Socket();
        try {
            InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ee){

        }
        id=new JTextField("id",15);
        botton2=new JButton("销户");
        botton3=new JButton("查询余额");
        botton4=new JButton("取钱");
        botton5=new JButton("转账");
        botton6=new JButton("修改账户信息");
        botton10=new JButton("存钱");
        botton11=new JButton("退出");
        boxV2=Box.createVerticalBox();boxV2.add(botton2);
        boxV3=Box.createVerticalBox(); boxV3.add(botton3);
        boxV4=Box.createVerticalBox(); boxV4.add(botton4);
        boxV5=Box.createVerticalBox(); boxV5.add(botton5);
        boxV6=Box.createVerticalBox();boxV6.add(botton6);
        boxV10=Box.createVerticalBox(); boxV10.add(botton10);
        boxV11=Box.createVerticalBox(); boxV11.add(botton11);
        //降低代码可读性
        add(botton2);add(botton3);add(botton4);add(botton10);add(botton5);add(botton6);add(botton11);
        botton2.addActionListener(this);	botton3.addActionListener(this);	botton4.addActionListener(this);	botton5.addActionListener(this);	botton6.addActionListener(this);	botton10.addActionListener(this);botton11.addActionListener(this);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        setSize(700,100);
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        try {
            if (e.getSource() == botton2) {
                out.writeUTF("%1;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                    new cancel();
                    dispose();
                }
            } else if (e.getSource() == botton3) {
                new refer();
                dispose();
            } else if (e.getSource() == botton4) {
                out.writeUTF("%1;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                new trans(3);
                dispose();
                }
            } else if (e.getSource() == botton5) {
                out.writeUTF("%1;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                new trans(1);
                dispose();
                }
            } else if (e.getSource() == botton6) {
                out.writeUTF("%1;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                new modify();
                dispose();
                }
            }
            else if (e.getSource() == botton10) {
                out.writeUTF("%1;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "密码已修改或用户已注销,请退出", "标题",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new shouye();
                }else {
                    new trans(2);
                    dispose();
                }
            } else if (e.getSource() == botton11) {
                dispose();
                new shouye();
            }
        }catch(IOException ee){
        }
    }
}

//adminmain类
class adminmain extends JFrame implements ActionListener {
    JButton botton1, botton2, botton3,botton4;
    Box boxV1, boxV2, boxV3,boxV4;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    public adminmain(){
        socket=new Socket();
        try {
            InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ee){

        }
        botton1=new JButton("导入xls文件开户");
        botton2=new JButton("导出xls文件");
        botton3=new JButton("导出pdf");
        botton4=new JButton("退出");
        boxV1=Box.createVerticalBox();boxV1.add(botton1);
        boxV2=Box.createVerticalBox(); boxV2.add(botton2);
        boxV3=Box.createVerticalBox(); boxV3.add(botton3);
        boxV4=Box.createVerticalBox(); boxV4.add(botton4);
        //降低代码可读性
        add(botton1);add(botton2);add(botton3);add(botton4);
        botton1.addActionListener(this);
        botton2.addActionListener(this);
        botton3.addActionListener(this);
        botton4.addActionListener(this);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        setSize(400,100);
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e){
            if (e.getSource() == botton1) {
                try {
                    ImportExcel.excelImport();
                    JOptionPane.showMessageDialog(null, "EXCEL导入成功", "标题",JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "EXCEL导入失败，请检查xls文件", "标题",JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                }
            } else if (e.getSource() == botton2) {
                    ExportExcel exportExcel = new ExportExcel();
                    exportExcel.Result();
                    JOptionPane.showMessageDialog(null, "EXCEL导出成功", "标题",JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == botton3) {
                try {
                    out.writeUTF("%9;");
                    String para =in.readUTF();
                    System.out.println(para);
                    try {
                        new PDFOutput(para).Result();
                        JOptionPane.showMessageDialog(null, "PDF导出成功", "标题",JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "PDF导出失败", "标题",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (e.getSource() == botton4) {
                dispose();
                new shouye();
            }
    }
}


//log 类
class log  extends JFrame implements ActionListener{
    JButton conf;
    Box box1,box2,box3;
    JTextField pas, xuehao;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    String str;
    String uidtmp,pstmp;
    int tag=0;
    @Override
    public void actionPerformed(ActionEvent e){
        try {
            InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            if (e.getSource() == conf) {
                uidtmp=xuehao.getText().toString().trim();
                pstmp=pas.getText().toString().trim();
                user.uid=uidtmp;
                user.pas=pstmp;
                str="%"+this.tag+";"+"id:"+uidtmp+";passwd:"+pstmp+";";
                System.out.println(str);
                out.writeUTF(str);
                String m =in.readUTF();
                System.out.println(m);
                if(m.charAt(0)=='1'){
                    JOptionPane.showMessageDialog(null, "登陆成功", "标题",JOptionPane.INFORMATION_MESSAGE);
                    if(this.tag==1){
                        new main();
                    }else if (this.tag==8){
                        new adminmain();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "账号或密码输入错误", "标题",JOptionPane.WARNING_MESSAGE);
                    new shouye();
                }
                dispose();
            }
        }catch (IOException ee){
        }
    }
    public log(int tag){
        this.tag=tag;
        socket=new Socket();
        box1=Box.createHorizontalBox();
        box2=Box.createHorizontalBox();
        box3=Box.createHorizontalBox();
        box1.add(new JLabel("输入卡号"));
        box2.add(new JLabel("输入密码"));
        xuehao=new JTextField("xuehao",15);
        pas=new JTextField("pas",15);
        conf=new JButton("确定");
        box1.add(xuehao);
        box2.add(pas);
        box3.add(conf);
        conf.addActionListener(this);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        con.add(box1);
        con.add(box2);
        con.add(box3);
        setSize(600,100);
        setVisible(true);
    }
}

//cancel销户 类
class cancel extends JFrame implements ActionListener {
    JButton conf1,conf2;
    Box box1;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==conf1){
            try{
                InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
                InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
                socket.connect(socketAddress);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("%2;id:"+user.uid+";passwd:"+user.pas+";");
                String str=in.readUTF();
                if(str.equals("0")){
                    JOptionPane.showMessageDialog(null, "账户已删除", "标题",JOptionPane.WARNING_MESSAGE);
                    new shouye();
                }else{
                String str1=in.readUTF();
                JOptionPane.showMessageDialog(null, str1, "标题",JOptionPane.WARNING_MESSAGE);
                new shouye();
                }
                dispose();
            }catch (IOException ee){
            }
        }
        if(e.getSource()==conf2){
            dispose();
            new main();
        }
    }
    public  cancel(){
        socket =new Socket();
        box1=Box.createHorizontalBox();
        conf1=new JButton("确定");
        conf1.addActionListener(this);
        conf2=new JButton("退出");
        conf2.addActionListener(this);
        box1.add(new Label("你确定要为当前账户销户吗?"));
        box1.add(conf1);
        box1.add(conf2);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        con.add(box1);
        setSize(400,100);
        setVisible(true);
    }
}

//注册（开户）类
class kaihu extends JFrame implements ActionListener{
    JButton Confirm;
    String str;
    JTextField name, pas, xuehao, phone, sex, brith;
    JTextArea showError;
    Socket socket=null;
    DataInputStream in=null;
    DataOutputStream out=null;
    @Override
    public void actionPerformed(ActionEvent e){
        try {
            if (e.getSource() == Confirm) {
                Body op = new Body();
                String uid = null;
                if ((uid = op.AccountOpen(new String(name.getText()), new String(pas.getText()), new String(xuehao.getText()), new String(phone.getText()), new String(sex.getText()), new String(brith.getText()))) != null) {
                    str = "%0;" + "id:" + uid.toString().trim() + ";name:" + name.getText().toString().trim() + ";passwd:" + pas.getText().toString().trim() + ";account:" + xuehao.getText().toString().trim() + ";phone:" + phone.getText() + ";gender:" + sex.getText() + ";birthdate:" + brith.getText() + ";";
                    System.out.println(str);
                    out.writeUTF(str);
                    String status = in.readUTF();
                    JOptionPane.showMessageDialog(null, status, "标题", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    JOptionPane.showMessageDialog(null, op.strtmp.toString(), "标题", JOptionPane.INFORMATION_MESSAGE);
                }
                dispose();
                socket.close();
                new shouye();
            }
        }catch (IOException ee){

        }
    }
    public  kaihu(){
        socket=new Socket();
        StringBuffer uitmp=new StringBuffer();
        name=new JTextField("less than 10",15);
        pas=new JTextField("more than 4 ",15);
        xuehao=new JTextField("Your id card",15);
        phone=new JTextField("like 133****6373",15);
        sex=new JTextField("M/F",15);
        brith=new JTextField("xxxx-xx-xx",15);
        Box boxtmp1=Box.createHorizontalBox();Box boxtmp2=Box.createHorizontalBox();Box boxtmp3=Box.createHorizontalBox();Box boxtmp4=Box.createHorizontalBox();Box boxtmp5=Box.createHorizontalBox();Box boxtmp6=Box.createHorizontalBox();Box boxtmp7=Box.createHorizontalBox();
        boxtmp4.add(new JLabel("输入姓名"));
        boxtmp3.add(new JLabel("输入密码"));
        boxtmp2.add(new JLabel("输入账号"));
        boxtmp5.add(new JLabel("输入手机"));
        boxtmp6.add(new JLabel("输入性别"));
        boxtmp7.add(new JLabel("出生日期"));
        boxtmp2.add(xuehao);
        boxtmp3.add(pas);
        boxtmp4.add(name);
        boxtmp5.add(phone);
        boxtmp6.add(sex);
        boxtmp7.add(brith);
        Box baseBox=Box.createVerticalBox();
        Box confirmBox=Box.createVerticalBox();
        Confirm=new JButton("确定");
        confirmBox.add(Confirm);
        baseBox.add(boxtmp2);
        baseBox.add(boxtmp3);
        baseBox.add(boxtmp4);
        baseBox.add(boxtmp5);
        baseBox.add(boxtmp6);
        baseBox.add(boxtmp7);
        showError=new JTextArea(5,35);
        Container con=getContentPane();
        con.setLayout(new FlowLayout());
        con.add(baseBox);
        con.add(confirmBox);
        con.add(showError);
        con.add(new JScrollPane(showError));
        Confirm.addActionListener(this);
        try {
            InetAddress address = InetAddress.getByName("xxx.xxx.xxx.xxx");
            InetSocketAddress socketAddress = new InetSocketAddress(address, 4331);
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
        }
        setSize(400,300);
        setVisible(true);
        //setDefaultCloseOperation(kaihu.EXIT_ON_CLOSE);
    }
}

