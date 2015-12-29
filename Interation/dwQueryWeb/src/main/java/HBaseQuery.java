
import org.apache.hadoop.hbase.thrift.generated.*;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import java.sql.ResultSet;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class HBaseQuery{
    private static final String CHARSET="UTF-8";
    static DecimalFormat formatter=new DecimalFormat("00");
    private final AbstractHBaseThriftService client;

    public HBaseQuery(String host,int port){
        client=new HBaseThriftClient(host,port);
        try{
            client.open();
        }catch (TTransportException te){
            te.printStackTrace();
        }
    }
    public HBaseQuery(){
        this("10.60.42.63",33215);
    }

    public void testGet() throws TException{
        Map<String,String> attr=new HashMap<String, String>(0);
        String table="test";
        String startRow="row1";
        String stopRow="row3";
        List<String> columns=new ArrayList<String>(0);
        columns.add("cf:col1");
        int id=client.scannerOpen(table,startRow,stopRow,columns,attr);
        int nbRow=2;
        List<TRowResult> results=client.scannerGetList(id, nbRow);
        while(results!=null&&!results.isEmpty()){
            for(TRowResult result:results){
                client.iterateResults(result);
            }
            results=client.scannerGetList(id,nbRow);
        }
        client.scannerClose(id);
    }

}
