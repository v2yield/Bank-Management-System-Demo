import javax.security.auth.login.AccountLockedException;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//注册
public class Register implements Callable<Boolean> {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME ="user_info";
    static String parseId="id:([0-9]{10});";
    static String parseName="name:([a-zA-Z\\u4e00-\\u9fa5]{1,10});";
    static String parsePasswd="passwd:([\\w]{4,});";
    static String parseAccount="account:([0-9]{12});";
    static String parsePhone= "phone:([1-9][0-9]{10});";
    static String parseGender ="gender:([M,F]{1});";
    static String parseBirthdate ="birthdate:(\\d{4}-[0-1][1-9]-[0-3][0-9]);";
    String Id;
    String Name;
    String Passwd;
    String Account;
    String Phone;
    String Gender;
    String Birthdate;
    static double Balance=2000.0;
    public Register(String str){
        Pattern p;
        Matcher m;

        p=Pattern.compile("\\S*"+parseId+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Id=m.group(1);
        }

        p=Pattern.compile("\\S*"+parseName+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Name=m.group(1);
        }

        p=Pattern.compile("\\S*"+parsePasswd+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Passwd=m.group(1);
        }

        p=Pattern.compile("\\S*"+parseAccount+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Account=m.group(1);
        }

        p=Pattern.compile("\\S*"+parsePhone+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Phone=m.group(1);
        }

        p=Pattern.compile("\\S*"+parseGender+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Gender=m.group(1);
        }

        p=Pattern.compile("\\S*"+parseBirthdate+"\\S*");
        m=p.matcher(str);
        if(m.find()){
            this.Birthdate=m.group(1);
        }
    }
    public int Insert(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 添加新用户...");
            String sql;
            sql="SELECT COUNT(*) FROM "+ TABLE_NAME + " WHERE user_account='"+this.Account+"'";
            stmt=conn.prepareStatement(sql);
            //判断卡数
            rs=stmt.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if (count >= 10) {
                    return 0;
                }
            }
            sql = "INSERT INTO "+TABLE_NAME+" (user_id, user_name, user_passwd, user_account, user_phone, user_gender, user_birthdate, user_balance)"
                    +" VALUES(?,?,?,?,?,?,?,?)";
            stmt=conn.prepareStatement(sql);
            stmt.setString(1,this.Id);
            stmt.setString(2,this.Name);
            stmt.setString(3,this.Passwd);
            stmt.setString(4,this.Account);
            stmt.setString(5,this.Phone);
            stmt.setString(6,this.Gender);
            stmt.setString(7,this.Birthdate);
            stmt.setDouble(8,Balance);
            int k =stmt.executeUpdate();
            if(k>0){
                System.out.println("注册成功");
                return 1;
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
        return -1;
    }
    @Override
    public Boolean call() throws Exception{
        if(this.Insert()==1) {
            return true;
        } else if(this.Insert()==0) {
            return false;
        }
        return null;
    }
}
