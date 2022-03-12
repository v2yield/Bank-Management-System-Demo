import com.mysql.cj.CacheAdapter;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//登录 返回true成功，返回false失败
public class Login implements Callable<Boolean> {
    static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static   String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static   String USER = "admin";
    static   String PASS = "123456";
    String TABLE_NAME;

    String parseId;
    static String parsePasswd="passwd:([\\w]{4,});";

    String Id;
    String Passwd;
    String HostAddress;

    public Login(String str,String HostAddress,String TABLE_NAME,String parseId) {
        Pattern p;
        Matcher m;

        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);
        }
        p=Pattern.compile("\\S*"+parsePasswd+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Passwd = m.group(1);
        }

        this.HostAddress = HostAddress;
        this.TABLE_NAME=TABLE_NAME;
        this.parseId=parseId;
    }
    synchronized public int Check(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 登录中..."+"访问主机ip"+this.HostAddress);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='" + this.Id + "'";
            rs = stmt.executeQuery(sql);
            if(rs.next()==false) {
                return 0;
            }
            else{
                do{
                    // 通过字段检索
                    String passwd = rs.getString("user_passwd");
                    if (passwd.equals(this.Passwd)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }while(rs.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public Boolean call() throws Exception{
        if(this.Check()==1){
            return true;
        }
        else if(this.Check()==0){
            return false;
        }
        return null;
    }
}

//删除账户
class DelACC implements Callable<Boolean> {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";

    static String parseId="id:([0-9]{10});";

    String Id;
    public DelACC(String str){
        Pattern p;
        Matcher m;
        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);
        }
    }
    synchronized public int Delete(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 查找中...");
            String sql;
            sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id + "'";
            stmt=conn.createStatement();
            int k= stmt.executeUpdate(sql);
            if(k>0){
                System.out.println("删除成功");
                return 1;
            }
            else{
                System.out.println("删除失败");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public Boolean call() throws Exception{
        if(this.Delete()==1){
            return true;
        }
        else if(this.Delete()==0){
            return false;
        }
        return null;
    }
}

//查询余额
class CheckBlc implements Callable<String> {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";

    static String parseId="id:([0-9]{10});";

    String Id;
    public CheckBlc(String str){
        Pattern p;
        Matcher m;
        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);
        }
    }
    synchronized public String Check(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 查找余额中...");
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id + "'";
            stmt=conn.createStatement();
            rs=stmt.executeQuery(sql);
            if(rs.next()==false){
                System.out.println("查找余额失败，无此用户");
                return "FALSE";
            }
            else{
                System.out.println("查找余额成功");
                String balance = Double.toString(rs.getDouble("user_balance"));
                return balance;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }finally {
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs !=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String call() throws Exception{
        return this.Check();
    }
}

class DrawMoney implements Callable<String>{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";

    static String parseId="id:([0-9]{10});";
    static String parseDraw="draw:([0-9]+(\\.?[0-9]+)?);";
    String Id;
    double Money;
    public DrawMoney (String str){
        Pattern p;
        Matcher m;
        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);

        }
        p=Pattern.compile("\\S*"+parseDraw+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Money= Double.parseDouble(m.group(1));
        }
    }
    synchronized public String Draw(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 查询余额中...");
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id + "'";
            stmt=conn.createStatement();
            rs=stmt.executeQuery(sql);
            if(rs.next()==false){
                System.out.println("查找余额失败，无此用户");
                return "FALSE1";
            }else if(this.Money==0){
                System.out.println("请输入正确的金额");
                return "FALSE3";
            }
            else{
                String out;
                System.out.println("查找余额成功");
                double balance = rs.getDouble("user_balance");
                if(balance>=Money){
                    sql="UPDATE "+TABLE_NAME+" SET user_balance="+(balance-Money)+" WHERE user_id='"+ this.Id + "'";
                    stmt.executeUpdate(sql);
                    return Double.toString(balance-Money);
                }
                else{
                    System.out.println("余额不足");
                    return "FALSE2";
                    //余额不足
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs !=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String call() throws Exception{
        return this.Draw();
    }
}
class SaveMoney implements Callable<String>{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";

    static String parseId="id:([0-9]{10});";
    static String parseSave="save:([0-9]+(\\.?[0-9]+)?);";
    String Id;
    double Money=0;
    public SaveMoney (String str){
        Pattern p;
        Matcher m;

        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);

        }
        p=Pattern.compile("\\S*"+parseSave+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Money= Double.parseDouble(m.group(1));
        }

    }
    synchronized public String Save(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 查询余额中...");
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id + "'";
            stmt=conn.createStatement();
            rs=stmt.executeQuery(sql);
            if(rs.next()==false){
                System.out.println("查询余额失败，无此用户");
                return "FALSE";
            }else if(this.Money==0){
                return "FALSE1";
            }
            else{
                String out;
                System.out.println("查询余额成功");
                double balance = rs.getDouble("user_balance");
                    sql="UPDATE "+TABLE_NAME+" SET user_balance="+(balance+Money)+" WHERE user_id='"+ this.Id + "'";
                    stmt.executeUpdate(sql);
                    return Double.toString(balance+Money);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs !=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String call() throws Exception{
        return this.Save();
    }
}

class Transfer implements Callable<String> {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";

    static String parseId="id:([0-9]{10});";
    static String parseId2="id2:([0-9]{10});";

    //转账钱
    static String parseTransferm="transferm:([0-9]+(\\.?[0-9]+)?);";
    String Id;
    String Id2;
    double Money=0;
    public Transfer (String str){
        Pattern p;
        Matcher m;
        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id = m.group(1);

        }

        p=Pattern.compile("\\S*"+parseId2+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Id2 = m.group(1);

        }

        p=Pattern.compile("\\S*"+parseTransferm+"\\S*");
        m=p.matcher(str);
        if(m.find()) {
            this.Money= Double.parseDouble(m.group(1));

        }

    }
    synchronized public String Tranlate(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs1=null;
        ResultSet rs2=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 查询余额中...");
            String sql1;
            String sql2;
            //那么从stmt得到的rs1,必须马上操作此rs1后,才能去得到另外的rs2,再对rs2操作.
            //不能互相交替使用,会引起rs已经关闭错误.
            sql1 = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id + "'";
            sql2 = "SELECT * FROM " + TABLE_NAME + " WHERE user_id='"+ this.Id2 + "'";
            stmt=conn.createStatement();
            rs2=stmt.executeQuery(sql2);
            if(rs2.next()==false){
                System.out.println("转账失败，无此被转账用户");
                return "FALSE1";
            }
            else if(this.Money==0){
                System.out.println("请输入正确的金额");
                return "FALSE3";
            }
            else{
                System.out.println("查询余额成功");
                double balance2 =rs2.getDouble("user_balance");
                rs1=stmt.executeQuery(sql1);
                rs1.next();
                double balance1 = rs1.getDouble("user_balance");
                if(balance1>=Money){
                    sql1="UPDATE "+TABLE_NAME+" SET user_balance="+(balance1-Money)+" WHERE user_id='"+ this.Id + "'";
                    sql2="UPDATE "+TABLE_NAME+" SET user_balance="+(balance2+Money)+" WHERE user_id='"+ this.Id2 + "'";
                    stmt.executeUpdate(sql1);
                    stmt.executeUpdate(sql2);
                    return Double.toString(balance1-Money);
                }
                else{
                    System.out.println("余额不足");
                    return "FALSE2";
                    //余额不足
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs1 !=null){
                try {
                    rs1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs2 != null){
                try {
                    rs2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String call() throws Exception{
        return this.Tranlate();
    }
}

class ChangeInfo implements Callable<Boolean> {
    static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static String USER = "admin";
    static String PASS = "123456";
    static String TABLE_NAME = "user_info";

    static String parseId="id:([0-9]{10});";
    static String parseName = "name:([a-zA-Z\\u4e00-\\u9fa5]{1,10});";
    //修改后的密码
    static String parsePasswd = "passwd1:([\\w]{4,});";
    static String parsePhone = "phone:([1-9][0-9]{10});";


    String Id;
    String Name;
    String Passwd;
    String Phone;

    public ChangeInfo(String str) {
        Pattern p;
        Matcher m;

        p = Pattern.compile(parseId);
        m = p.matcher(str);
        if (m.find()) {
            this.Id = m.group(1);
        } else {
            this.Id = null;
        }

        p = Pattern.compile(parseName);
        m = p.matcher(str);
        if (m.find()) {
            this.Name = m.group(1);
        } else {
            this.Name = null;
        }

        p = Pattern.compile(parsePasswd);
        m = p.matcher(str);
        if (m.find()) {
            this.Passwd = m.group(1);
        } else {
            this.Passwd = null;
        }

        p = Pattern.compile(parsePhone);
        m = p.matcher(str);
        if (m.find()) {
            this.Phone = m.group(1);
        } else {
            this.Phone = null;
        }

        p = Pattern.compile(parseName);
        m = p.matcher(str);
        if (m.find()) {
            this.Name = m.group(1);
        } else {
            this.Name = null;
        }
    }

    synchronized public int Change() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 修改信息...");
            String sql;
            int k=0;
            if (this.Name != null) {
                sql = "UPDATE " + TABLE_NAME + " SET user_name =?" + " WHERE user_id='" + this.Id + "'";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.Name);
                stmt.executeUpdate();
                k++;
            }
            if (this.Passwd != null) {
                sql = "UPDATE " + TABLE_NAME + " SET user_passwd =?" + " WHERE user_id='" + this.Id + "'";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.Passwd);
                stmt.executeUpdate();
                k++;
            }
            if (this.Phone != null) {
                sql = "UPDATE " + TABLE_NAME + " SET user_phone =?" + " WHERE user_id='" + this.Id + "'";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.Phone);
                stmt.executeUpdate();
                k++;
            }
            if (k > 0) {
                System.out.println("修改成功");
                return 1;
            } else {
                System.out.println("修改失败");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public Boolean call() throws  Exception{
        if(this.Change()==1){
            return true;
        }
        else if(this.Change()==0){
            return false;
        }
        return null;
    }
}
//输出报表信息
class outPdf implements Callable<String>{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME ="user_info";
    public String Count(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        Integer cnt=0;
        Double amount=0.0;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 计算中...");
            String sql1;
            String sql2;
            sql1="SELECT COUNT(*) FROM "+ TABLE_NAME;
            sql2="SELECT SUM(user_balance) FROM "+TABLE_NAME;
            stmt=conn.prepareStatement(sql1);
            //计算用户数
            rs=stmt.executeQuery();
            if(rs.next()){
                cnt=rs.getInt(1);
            }
            stmt=conn.prepareStatement(sql2);
            rs=stmt.executeQuery();
            if(rs.next()){
                amount=rs.getDouble(1);
            }
            System.out.println("Total number of the current bank accounts: "+cnt.toString()+"\nTotal deposit balance of the current bank accounts "+amount.toString());
            return "Total number of the current bank accounts: "+cnt.toString()+"\nTotal deposit balance of the current bank accounts "+amount.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try{
                if(conn !=null){
                    conn.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(rs!=null){
                    rs.close();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String call() throws Exception{
        return this.Count();
    }
}






