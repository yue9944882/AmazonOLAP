import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * Created by guguli on 2015/12/29.
 */

import org.apache.log4j.Logger;

public class HiveQuery {
    private static Connection conn=null;
    private static final Logger log=Logger.getLogger(HiveQuery.class);
    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            conn = DriverManager.getConnection("jdbc:hive2://10.60.42.63:33214/default", "root", "jinmin");
            System.out.println("Connecting Hive Server Success!!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void demo(){
        try{
            PreparedStatement stmt=conn.prepareStatement("select count(*) from hive_dwMovie");
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                System.out.print(rs.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet doQuery(String sql){
        try{
            PreparedStatement stmt=conn.prepareStatement(sql);
            return stmt.executeQuery();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
