import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.Date;

import jxl.*;
import jxl.write.*;

public class ExportExcel {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://xxx.xxx.xxx.xxx:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME = "user_info";
    Customer customer=null;
    public  void excelExport(ArrayList<Customer> list, String path) {
        WritableWorkbook book = null;
        try {
            // 创建一个Excel文件对象
            book = Workbook.createWorkbook(new File(path));
            // 创建Excel第一个选项卡对象
            WritableSheet sheet = book.createSheet("第一页", 0);
            // 设置表头，第一行内容
            // Label参数说明：第一个是列，第二个是行，第三个是要写入的数据值，索引值都是从0开始
            Label label1 = new Label(0, 0, "银行卡号");// 对应为第1列第1行的数据
            Label label2 = new Label(1, 0, "用户姓名");// 对应为第2列第1行的数据
            Label label3 = new Label(2, 0, "用户密码");// 对应为第3列第1行的数据
            Label label4 = new Label(3, 0, "用户身份ID");// 对应第4列第1行的数据
            Label label5 = new Label(4, 0, "手机号");// 对应为第5列第1行的数据
            Label label6 = new Label(5, 0, "性别");// 对应为第6列第1行的数据
            Label label7 = new Label(6, 0, "出生日期");// 对应为第7列第1行的数据
            Label label8 = new Label(7, 0, "账户余额");// 对应为第8列第1行的数据
            // 添加单元格到选项卡中
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label4);
            sheet.addCell(label5);
            sheet.addCell(label6);
            sheet.addCell(label7);
            sheet.addCell(label8);
            // 遍历集合并添加数据到行，每行对应一个对象
            for (int i = 0; i < list.size(); i++) {
                // 表头占据第一行，所以下面行数是索引值+1
                // 跟上面添加表头一样添加单元格数据，这里为了方便直接使用链式编程
                sheet.addCell(new Label(0, i + 1, list.get(i).getUid()));
                sheet.addCell(new Label(1, i + 1, list.get(i).getName()));
                sheet.addCell(new Label(2, i + 1, list.get(i).getPasswd()));
                sheet.addCell(new Label(3, i + 1, list.get(i).getAccount()));
                sheet.addCell(new Label(4, i + 1, list.get(i).getPhone()));
                sheet.addCell(new Label(5, i + 1, list.get(i).getGender()));
                sheet.addCell(new Label(6, i + 1, list.get(i).getBirthdate()));
                sheet.addCell(new Label(7, i + 1, list.get(i).getBalance().toString()));
            }
            // 写入数据到目标文件
            book.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭
                book.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Result() {
        ArrayList list = new ArrayList<Customer>();
        Connection con = null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            Class.forName(JDBC_DRIVER);
            //连接数据库
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt= con.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME;
            rs= stmt.executeQuery(sql);
            // 创建数据
            while (rs.next()) {
                String id = rs.getString("user_id");
                String name = rs.getString("user_name");
                String passwd = rs.getString("user_passwd");
                String account = rs.getString("user_account");
                String phone = rs.getString("user_phone");
                String gender = rs.getString("user_gender");
                String birthdate = rs.getString("user_birthdate");
                double balance = Double.parseDouble(rs.getString("user_balance"));
                customer= new Customer();
                customer.setUid(id);
                customer.setName(name);
                customer.setPasswd(passwd);
                customer.setAccount(account);
                customer.setPhone(phone);
                customer.setGender(gender);
                customer.setBirthdate(birthdate);
                customer.setBalance(balance);
                list.add(customer);
            }
            String path = "./customer.xls";
            System.out.println("开始导出...");
            long s1 = System.currentTimeMillis();
            // 开始导出
            excelExport(list, path);
            long s2 = System.currentTimeMillis();
            long time = s2 - s1;
            System.out.println("导出完成！消耗时间：" + time + "毫秒");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(con !=null){
                    con.close();
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
}
class Customer{
    private String uid;
    private String name;
    private String passwd;
    private String account;
    private String phone;
    private String gender;
    private String birthdate;
    private Double balance;
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getPasswd() {
        return passwd;
    }

    public String getAccount() {
        return account;
    }

    public String getPhone() {
        return phone;
    }
    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public Double getBalance() {
        return balance;
    }
}


