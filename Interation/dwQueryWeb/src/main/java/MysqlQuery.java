import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by guguli on 2015/12/29.
 */
public class MysqlQuery {
    private static Connection conn=null;
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.60.42.63:30000/dw", "root", "mysqlpassword");
            System.out.println("Connecting Mysql Server Success!!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void demo(){
        try{
            PreparedStatement stmt=conn.prepareStatement("select count(*) from dwMovie");
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                System.out.print(rs.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ResultSet doQuery(String sql){
        try{
            PreparedStatement stmt=conn.prepareStatement(sql);
            return stmt.executeQuery();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
