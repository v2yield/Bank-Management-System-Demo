import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ImportExcel {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://xxx.xxx.xxx.xxx:3306/bankstorage?useSSL=false&characterEncoding=utf8";
    static final String USER = "admin";
    static final String PASS = "123456";
    static final String TABLE_NAME="user_info";
    public static void excelImport() throws SQLException {
        Connection con=null;
        Statement stmt=null;
        PreparedStatement pstmt=null;
        try {
            Class.forName(JDBC_DRIVER);
            Workbook book = null;
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();
            String sql="INSERT INTO "+TABLE_NAME+" (user_id, user_name, user_passwd, user_account, user_phone, user_gender, user_birthdate, user_balance) VALUES(?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            //读取本地Excel文件内容
            book = Workbook.getWorkbook(new File("./readme.xls"));
            // 获取Excel第一个选项卡对象
            Sheet sheet = book.getSheet(0);
            int cols = sheet.getColumns(); // 取到表格的列数
            int rows = sheet.getRows();// 取到表格的行数
            String[] contents = new String[cols];
            System.out.println("row:\n"+sheet.getRows()+"cols:"+sheet.getColumns());
            for (int i = 1; i < sheet.getRows(); i++) {
                for (int j = 0; j < cols; j++) {
                    contents[j] =sheet.getCell(j, i).getContents();
                    if (j == 7) {
                        System.out.println("1:"+contents[j]);
                        pstmt.setDouble(8, Double.parseDouble(contents[j]));
                        System.out.println("2:"+Double.parseDouble(contents[j]));
                    } else {
                        pstmt.setString(j + 1, contents[j]);
                    }
                    System.out.print(contents[j]+ "\t");
                }
                pstmt.executeUpdate();
                System.out.println();
            }
            System.out.println("共有" + (rows - 1) + "记录导入。");
            stmt.close();
            pstmt.close();
            con.close();
        } catch (java.lang.ClassNotFoundException e) {
            System.err.println("ClassNotFoundException:" + e.getMessage());
        } catch (SQLException ex) {
            System.err.println("SQLException:" + ex.getMessage());
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try{
                if(con !=null){
                    con.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(pstmt!=null){
                    pstmt.close();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

