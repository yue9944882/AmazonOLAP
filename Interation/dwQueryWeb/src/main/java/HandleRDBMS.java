import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.hadoop.hive.ql.metadata.Hive;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by guguli on 2015/12/29.
 */

import java.sql.*;

@WebServlet(name = "HandleRDBMS")
public class HandleRDBMS extends HttpServlet {

    private MysqlQuery myq;
    private HiveQuery hvq;
    private HBaseQuery hbq;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s=request.getParameter("textv");
        System.out.println(s);
        RequestDispatcher rd=request.getRequestDispatcher("Report.jsp");
        request.setAttribute("newtextv", s);
        //HiveQuery hq=new HiveQuery();
        //hvq.demo();
        //hq.demo();
        //MysqlQuery mq=new MysqlQuery();
        //myq.demo();
        //HBaseQuery hbq=new HBaseQuery();
        /*try{
            hbq.testGet();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void init(){
        //HiveQuery hq=new HiveQuery();
        //hq.demo();
        hvq=new HiveQuery();
        hbq=new HBaseQuery();
        myq=new MysqlQuery();
        System.out.println("Init Complete");

    }

}
