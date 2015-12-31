import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by guguli on 2015/12/29.
 */
@WebServlet(name = "ParseServlet")
public class ParseServlet extends HttpServlet {

    private static MysqlQuery myq=null;
    private static HiveQuery hvq=null;
    private static HBaseQuery hbq=null;
    static{
        myq=new MysqlQuery();
        hvq=new HiveQuery();
        hbq=new HBaseQuery();
    }
    private static Map<String,Integer> styleMap=new HashMap<String,Integer>(0);
    static{
        styleMap.put("Comedy",new Integer(0x000001));
        styleMap.put("Kids & Family",new Integer(0x000002));
        styleMap.put("Science Fiction",new Integer(0x000004));
        styleMap.put("Drama",new Integer(0x000008));
        styleMap.put("Thriller",new Integer(0x000010));
        styleMap.put("Mystery",new Integer(0x000020));
        styleMap.put("Horror",new Integer(0x000040));
        styleMap.put("Adventure",new Integer(0x000080));
        styleMap.put("Action",new Integer(0x000100));
        styleMap.put("Documentary",new Integer(0x000200));
        styleMap.put("Fantasy",new Integer(0x000400));
        styleMap.put("Military & War",new Integer(0x000800));
        styleMap.put("Western",new Integer(0x001000));
        styleMap.put("Romance",new Integer(0x002000));
        styleMap.put("International",new Integer(0x004000));
        styleMap.put("Music",new Integer(0x008000));
        styleMap.put("Reality TV",new Integer(0x010000));
        styleMap.put("Musical",new Integer(0x020000));
        styleMap.put("Sports",new Integer(0x040000));
        styleMap.put("Gay & Lesbian",new Integer(0x080000));
        styleMap.put("TV Game Shows",new Integer(0x100000));
        styleMap.put("TV Talk Shows",new Integer(0x200000));
        styleMap.put("Other",new Integer(0x400000));
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /// Judge And ,Or
        /// O - Not decided , 1 - And , 2 - Or
        int iYearStatus=0;
        int iMonthStatus=0;
        int iDayStatus=0;
        int iStyleStatus=0;
        int iDirStatus=0;
        int iActStatus=0;
        int iMPAAStatus=0;

        String szTrue="true";
        //String szFalse="false";

        String yl=null,yr=null,ml=null,mr=null,dl=null,dr=null;
        Integer iyl=null,iyr=null,iml=null,imr=null,idl=null,idr=null;
        Integer styleSet=0;
        String dname=null;
        String aname=null;
        String mpaaname=null;

        if(szTrue.equals(request.getParameter("YearAnd"))&&null==request.getParameter("YearOr")){
            iYearStatus=1;
        }else if(szTrue.equals(request.getParameter("YearOr"))&&null==request.getParameter("YearAnd")){
            iYearStatus=2;
        }else{;}

        if(szTrue.equals(request.getParameter("MonthAnd"))&&null==request.getParameter("MonthOr")){
            iMonthStatus=1;
        }else if(szTrue.equals(request.getParameter("MonthOr"))&&null==request.getParameter("MonthAnd")){
            iMonthStatus=2;
        }else{;}
        String s=request.getParameter("DayAnd");
        String s2=request.getParameter("DayOr");
        if(szTrue.equals(request.getParameter("DayAnd"))&&null==request.getParameter("DayOr")){
            iDayStatus=1;
        }else if(szTrue.equals(request.getParameter("DayOr"))&&null==request.getParameter("DayAnd")){
            iDayStatus=2;
        }else{;}

        if(szTrue.equals(request.getParameter("DirAnd"))&&null==request.getParameter("DirOr")){
            iDirStatus=1;
        }else if(szTrue.equals(request.getParameter("DirOr"))&&null==request.getParameter("DirAnd")){
            iDirStatus=2;
        }else{;}

        if(szTrue.equals(request.getParameter("ActAnd"))&&null==request.getParameter("ActOr")){
            iActStatus=1;
        }else if(szTrue.equals(request.getParameter("ActOr"))&&null==request.getParameter("ActAnd")){
            iActStatus=2;
        }else{;}

        if(szTrue.equals(request.getParameter("MpaaAnd"))&&null==request.getParameter("MpaaOr")){
            iMPAAStatus=1;
        }else if(szTrue.equals(request.getParameter("MpaaOr"))&&null==request.getParameter("MpaaAnd")){
            iMPAAStatus=2;
        }else{;}

        if(szTrue.equals(request.getParameter("StyleAnd"))&&null==request.getParameter("StyleOr")){
            iStyleStatus=1;
        }else if(szTrue.equals(request.getParameter("StyleOr"))&&null==request.getParameter("StyleAnd")){
            iStyleStatus=2;
        }else{;}
        /// Judge And ,Or Finished - Related Flag enabled

        /// Parse Param

        if(iYearStatus!=0){
            yl=request.getParameter("YearL");
            yr=request.getParameter("YearR");
            if(yl!="")iyl=Integer.parseInt(yl);
            if(yr!="")iyr=Integer.parseInt(yr);
        }

        if(iMonthStatus!=0){
            ml=request.getParameter("MonthL");
            mr=request.getParameter("MonthR");
            if(ml!="")iml=Integer.parseInt(ml);
            if(mr!="")imr=Integer.parseInt(mr);
        }

        if(iDayStatus!=0){
            dl=request.getParameter("DayL");
            dr=request.getParameter("DayR");
            if(dl!="")idl=Integer.parseInt(dl);
            if(dr!="")idr=Integer.parseInt(dr);
        }

        if(iStyleStatus!=0){
            String[] styles=request.getParameterValues("Style");
            if(styles==null)styles=new String[0];
            for(String style:styles) {
                styleSet = styleSet | (styleMap.get(style));
            }
        }

        if(iDirStatus!=0){
            dname=request.getParameter("Director");
        }

        if(iActStatus!=0){
            aname=request.getParameter("Actor");
        }

        if(iMPAAStatus!=0){
            mpaaname=request.getParameter("MPAA");
        }
        /// Parse Param Finished

        /// Generating SQL phase
        /// - MYSQL BEGIN -
        long ms1=System.currentTimeMillis();
        String mysql_select="select distinct(dwMovie.movieId),dwMovie.movieName,dwMovie.movieStyle,dwMovie.movieDuration,dwMovie.movieStudio,dwMovie.movieTime,dwMovie.movieMPAA ";
        String mysql_from=" from dwMovie,dwFactTable";
        String mysql_where_join=" where dwMovie.movieId=dwFactTable.factMovieRef";
        String mysql_where_cond=" dwMovie.movieId=dwMovie.movieId";

        if(iYearStatus!=0||iMonthStatus!=0||iDayStatus!=0){
            mysql_from+=",dwDimDate";
            mysql_where_join+=" and dwFactTable.factDimDate=dwDimDate.dimDateId and dwDimDate.dimDateId!='n/a' ";
        }
        if(iYearStatus==1){
            if(iyl!=null&&iyr!=null){
                mysql_where_cond=" (dwDimDate.dimDateYear>="+iyl+" and dwDimDate.dimDateYear<="+iyr+") and "+mysql_where_cond;
            }else if(iyl!=null){
                mysql_where_cond=" dwDimDate.dimDateYear>="+iyl+" and "+mysql_where_cond;
            }else if(iyr!=null){
                mysql_where_cond=" dwDimDate.dimDateYear<="+iyr+" and "+mysql_where_cond;
            }else{;}
        }else if(iYearStatus==2){
            if(iyl!=null&&iyr!=null){
                mysql_where_cond+=" or (dwDimDate.dimDateYear>="+iyl+" and dwDimDate.dimDateYear<="+iyr+")";
            }else if(iyl!=null){
                mysql_where_cond+=" or dwDimDate.dimDateYear>="+iyl;
            }else if(iyr!=null){
                mysql_where_cond+=" or dwDimDate.dimDateYear<="+iyr;
            }else{;}
        }


        if(iMonthStatus==1){
            if(iml!=null&&imr!=null){
                mysql_where_cond=" (dwDimDate.dimDateMonth>="+iml+" and dwDimDate.dimDateMonth<="+imr+") and "+mysql_where_cond;
            }else if(iml!=null){
                mysql_where_cond=" dwDimDate.dimDateMonth>="+iml+" and "+mysql_where_cond;
            }else if(imr!=null){
                mysql_where_cond=" dwDimDate.dimDateMonth<="+imr+" and "+mysql_where_cond;
            }else{;}
        }else if(iMonthStatus==2){
            if(iml!=null&&imr!=null){
                mysql_where_cond+=" or (dwDimDate.dimDateMonth>="+iml+" and dwDimDate.dimDateMonth<="+imr+")";
            }else if(iml!=null){
                mysql_where_cond+=" or dwDimDate.dimDateMonth>="+iml;
            }else if(imr!=null){
                mysql_where_cond+=" or dwDimDate.dimDateMonth<="+imr;
            }else{;}
        }

        if(iDayStatus==1){
            if(idl!=null&&idr!=null){
                mysql_where_cond=" (dwDimDate.dimDateDay>="+idl+" and dwDimDate.dimDateDay<="+idr+") and "+mysql_where_cond;
            }else if(idl!=null){
                mysql_where_cond=" dwDimDate.dimDateDay>="+idl+" and "+mysql_where_cond;
            }else if(idr!=null){
                mysql_where_cond=" dwDimDate.dimDateDay<="+idr+" and "+mysql_where_cond;
            }else{;}
        }else if(iDayStatus==2){
            if(idl!=null&&idr!=null){
                mysql_where_cond+=" or (dwDimDate.dimDateDay>="+idl+" and dwDimDate.dimDateDay<="+idr+")";
            }else if(idl!=null){
                mysql_where_cond+=" or dwDimDate.dimDateDay>="+idl;
            }else if(idr!=null){
                mysql_where_cond+=" or dwDimDate.dimDateDay<="+idr;
            }else{;}
        }

        if(iDirStatus==1){
            mysql_from+=",dwDimDirectorBridge,dwDimDirector";
            mysql_where_join+=" and dwDimDirectorBridge.dimDBId=dwFactTable.factDimDirectorBridge and dwDimDirectorBridge.dimDBDId=dwDimDirector.dimDirectorId";
            mysql_where_cond="dwDimDirector.dimDirectorName='"+dname+"' and "+mysql_where_cond;
        }else if(iDirStatus==2){
            mysql_from+=",dwDimDirectorBridge,dwDimDirector";
            mysql_where_join+=" and dwDimDirectorBridge.dimDBId=dwFactTable.factDimDirectorBridge and dwDimDirectorBridge.dimDBDId=dwDimDirector.dimDirectorId";
            mysql_where_cond+=" or dwDimDirector.dimDirectorName='"+dname+"' ";
        }

        if(iActStatus==1){
            mysql_from+=",dwDimStarBridge,dwDimActor";
            mysql_where_join+=" and dwDimStarBridge.dimSBId=dwFactTable.factDimStarBridge and dwDimStarBridge.dimSBAId=dwDimActor.dimActorId";
            mysql_where_cond="dwDimActor.dimActorName='"+aname+"' and "+mysql_where_cond;
        }else if(iActStatus==2){
            mysql_from+=",dwDimStarBridge,dwDimActor";
            mysql_where_join+="and dwDimStarBridge.dimSBId=dwFactTable.factDimStarBridge and dwDimStarBridge.dimSBAId=dwDimActor.dimActorId";
            mysql_where_cond+=" or dwDimActor.dimActorName='"+aname+"'";
        }

        if(iMPAAStatus==1){
            mysql_from+=",dwDimMPAA";
            mysql_where_join+=" and dwDimMPAA.mpaaId=dwFactTable.factDimMPAA";
            mysql_where_cond="dwDimMPAA.mpaaName='"+mpaaname+"' and "+mysql_where_cond;
        }else if(iMPAAStatus==2){
            mysql_from+=",dwDimMPAA";
            mysql_where_join+=" and dwDimMPAA.mpaaId=dwFactTable.factDimMPAA";
            mysql_where_cond=mysql_where_cond+" or dwDimMPAA.mpaaName='"+mpaaname+"' ";
        }

        if(iStyleStatus==1){
            mysql_where_cond="(dwFactTable.factDimStyle/"+styleSet+")%2=1 and "+mysql_where_cond;
        }else if(iStyleStatus==2){
            mysql_where_cond+=" or (dwFactTable.factDimStyle/"+styleSet+")%2=1";
        }

        String sql=mysql_select+" "+mysql_from+mysql_where_join+" and "+mysql_where_cond;

        System.out.println(sql);

        List<MovieBean> list=new ArrayList<MovieBean>(0);
        long ms2=ms1;
        try{
            ResultSet rs=myq.doQuery(sql);
            ms2=System.currentTimeMillis();
            while(rs.next()){
                MovieBean mb=new MovieBean();
                mb.setMovieId(rs.getString(1));
                mb.setMovieName(rs.getString(2));
                mb.setMovieStyle(rs.getString(3));
                mb.setMovieDuration(rs.getString(4));
                mb.setMovieStudio(rs.getString(5));
                mb.setMovieTime(rs.getString(6));
                mb.setMovieMPAA(rs.getString(7));
                list.add(mb);
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
        request.setAttribute("list",list);
        System.out.println("MYSQL QUERY ELAPSE TIME:"+(ms2-ms1)/1000+" sec");
        /// - MYSQL FINISHED -

        /// - HBASE BEGIN -
        long bms1=System.currentTimeMillis();
        String dateTable="hbase_dwHBaseDateIndex";
        String movieTable="hbase_dwMovie";
        String filter="";
        if(iYearStatus==1){
            if(iyl!=null&&iyr!=null){
                filter+="(RowFilter(>=,'binary:"+iyl+"')) AND (RowFilter(<=,'binary:"+iyr+"'))";
            }else if(iyl!=null){
                filter+="(RowFilter(>=,binary:"+iyl+"))";
            }else if(iyr!=null){
                filter+="(RowFilter(<=,binary:"+iyr+"))";
            }else{;}
        }

        System.out.println(filter);

        hbq.scanWithFilter(dateTable, filter);
        long bms2=System.currentTimeMillis();
        System.out.println("HBASE QUERY ELAPSE TIME:"+(bms2-bms1)/1000+" sec");
        /// - HBASE FINISHED -

        /// - HIVEQL BEGIN -

        long hms1=System.currentTimeMillis();
        String hmysql_select="select distinct(hive_dwMovie.movieId),hive_dwMovie.movieName,hive_dwMovie.movieStyle,hive_dwMovie.movieDuration,hive_dwMovie.movieStudio,hive_dwMovie.movieTime,hive_dwMovie.movieMPAA ";
        String hmysql_from=" from hive_dwMovie,hive_dwFactTable";
        String hmysql_where_join=" where hive_dwMovie.movieId=hive_dwFactTable.factMovieRef";
        String hmysql_where_cond=" hive_dwMovie.movieId=hive_dwMovie.movieId";

        if(iYearStatus!=0||iMonthStatus!=0||iDayStatus!=0){
            hmysql_from+=",hive_dwDimDate";
            hmysql_where_join+=" and hive_dwFactTable.factDimDate=hive_dwDimDate.dimDateId and hive_dwDimDate.dimDateId!='n/a' ";
        }
        if(iYearStatus==1){
            if(iyl!=null&&iyr!=null){
                hmysql_where_cond=" (hive_dwDimDate.dimDateYear>="+iyl+" and hive_dwDimDate.dimDateYear<="+iyr+") and "+hmysql_where_cond;
            }else if(iyl!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateYear>="+iyl+" and "+hmysql_where_cond;
            }else if(iyr!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateYear<="+iyr+" and "+hmysql_where_cond;
            }else{;}
        }else if(iYearStatus==2){
            if(iyl!=null&&iyr!=null){
                hmysql_where_cond+=" or (hive_dwDimDate.dimDateYear>="+iyl+" and hive_dwDimDate.dimDateYear<="+iyr+")";
            }else if(iyl!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateYear>="+iyl;
            }else if(iyr!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateYear<="+iyr;
            }else{;}
        }


        if(iMonthStatus==1){
            if(iml!=null&&imr!=null){
                hmysql_where_cond=" (hive_dwDimDate.dimDateMonth>="+iml+" and hive_dwDimDate.dimDateMonth<="+imr+") and "+hmysql_where_cond;
            }else if(iml!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateMonth>="+iml+" and "+hmysql_where_cond;
            }else if(imr!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateMonth<="+imr+" and "+hmysql_where_cond;
            }else{;}
        }else if(iMonthStatus==2){
            if(iml!=null&&imr!=null){
                hmysql_where_cond+=" or (hive_dwDimDate.dimDateMonth>="+iml+" and hive_dwDimDate.dimDateMonth<="+imr+")";
            }else if(iml!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateMonth>="+iml;
            }else if(imr!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateMonth<="+imr;
            }else{;}
        }

        if(iDayStatus==1){
            if(idl!=null&&idr!=null){
                hmysql_where_cond=" (hive_dwDimDate.dimDateDay>="+idl+" and hive_dwDimDate.dimDateDay<="+idr+") and "+hmysql_where_cond;
            }else if(idl!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateDay>="+idl+" and "+hmysql_where_cond;
            }else if(idr!=null){
                hmysql_where_cond=" hive_dwDimDate.dimDateDay<="+idr+" and "+hmysql_where_cond;
            }else{;}
        }else if(iDayStatus==2){
            if(idl!=null&&idr!=null){
                hmysql_where_cond+=" or (hive_dwDimDate.dimDateDay>="+idl+" and hive_dwDimDate.dimDateDay<="+idr+")";
            }else if(idl!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateDay>="+idl;
            }else if(idr!=null){
                hmysql_where_cond+=" or hive_dwDimDate.dimDateDay<="+idr;
            }else{;}
        }

        if(iDirStatus==1){
            hmysql_from+=",hive_dwDimDirectorBridge,hive_dwDimDirector";
            hmysql_where_join+=" and hive_dwDimDirectorBridge.dimDBId=hive_dwFactTable.factDimDirectorBridge and hive_dwDimDirectorBridge.dimDBDId=hive_dwDimDirector.dimDirectorId";
            hmysql_where_cond="hive_dwDimDirector.dimDirectorName='"+dname+"' and "+hmysql_where_cond;
        }else if(iDirStatus==2){
            hmysql_from+=",hive_dwDimDirectorBridge,hive_dwDimDirector";
            hmysql_where_join+=" and hive_dwDimDirectorBridge.dimDBId=hive_dwFactTable.factDimDirectorBridge and hive_dwDimDirectorBridge.dimDBDId=hive_dwDimDirector.dimDirectorId";
            hmysql_where_cond+=" or hive_dwDimDirector.dimDirectorName='"+dname+"' ";
        }

        if(iActStatus==1){
            hmysql_from+=",hive_dwDimStarBridge,dwDimActor";
            hmysql_where_join+=" and hive_dwDimStarBridge.dimSBId=hive_dwFactTable.factDimStarBridge and hive_dwDimStarBridge.dimSBAId=hive_dwDimActor.dimActorId";
            hmysql_where_cond="hive_dwDimActor.dimActorName='"+aname+"' and "+hmysql_where_cond;
        }else if(iActStatus==2){
            hmysql_from+=",hive_dwDimStarBridge,dwDimActor";
            hmysql_where_join+="and hive_dwDimStarBridge.dimSBId=hive_dwFactTable.factDimStarBridge and hive_dwDimStarBridge.dimSBAId=hive_dwDimActor.dimActorId";
            hmysql_where_cond+=" or hive_dwDimActor.dimActorName='"+aname+"'";
        }

        if(iMPAAStatus==1){
            hmysql_from+=",hive_dwDimMPAA";
            hmysql_where_join+=" and hive_dwDimMPAA.mpaaId=hive_dwFactTable.factDimMPAA";
            hmysql_where_cond="hive_dwDimMPAA.mpaaName='"+mpaaname+"' and "+hmysql_where_cond;
        }else if(iMPAAStatus==2){
            hmysql_from+=",hive_dwDimMPAA";
            hmysql_where_join+=" and hive_dwDimMPAA.mpaaId=hive_dwFactTable.factDimMPAA";
            hmysql_where_cond=hmysql_where_cond+" or hive_dwDimMPAA.mpaaName='"+mpaaname+"' ";
        }

        if(iStyleStatus==1){
            hmysql_where_cond="(hive_dwFactTable.factDimStyle/"+styleSet+")%2=1 and "+hmysql_where_cond;
        }else if(iStyleStatus==2){
            hmysql_where_cond+=" or (hive_dwFactTable.factDimStyle/"+styleSet+")%2=1";
        }

        String hsql=hmysql_select+" "+hmysql_from+hmysql_where_join+" and "+hmysql_where_cond;

        System.out.println(hsql);
        long hms2=hms1;
        List<MovieBean> hlist=new ArrayList<MovieBean>(0);
        /*try{
            ResultSet rs=hvq.doQuery(hsql);
            hms2=System.currentTimeMillis();
        }catch(Exception sqle){
            sqle.printStackTrace();
        }*/
        System.out.println("HIVEQL QUERY ELAPSE TIME:"+(hms2-hms1)/1000+" sec");

        /// - HIVEQL END -

        /// hbq.scanWithFilter("hbase_dwMovie","RowFilter(=,'regexstring:003-1*')");
        ///
        RequestDispatcher rd=request.getRequestDispatcher("report.jsp");
        rd.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


}
