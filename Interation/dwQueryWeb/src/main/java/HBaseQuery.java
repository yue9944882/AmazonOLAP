
import org.apache.hadoop.hbase.thrift.generated.*;
import org.apache.hadoop.hive.ql.exec.TableScanOperator;
import org.apache.hadoop.hive.serde2.thrift.TBinarySortableProtocol;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


import java.sql.ResultSet;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class HBaseQuery{
    private byte[] decode(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        for (int i = 0; i < buffer.limit(); i++) {
            bytes[i] = buffer.get();
        }
        return bytes;
    }
    /*private static final String CHARSET="UTF-8";
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
        int id = client.scannerOpen(table, startRow, stopRow, columns, attr);
        int nbRow=2;
        List<TRowResult> results=client.scannerGetList(id, nbRow);
        while(results!=null&&!results.isEmpty()){
            for(TRowResult result:results){
                client.iterateResults(result);
            }
            results=client.scannerGetList(id,nbRow);
        }
        client.scannerClose(id);
    }*/
    private final TTransport transport;
    private final Hbase.Client client;

    public HBaseQuery(){
        transport=new TSocket("10.60.42.63",33215);
        TProtocol tProtocol=new TBinaryProtocol(transport,true,true);
        client=new Hbase.Client(tProtocol);
    }

    public void scanWithFilter(String tableName,String filter){
        try{
            transport.open();
            TScan tscan=new TScan();
            Map<ByteBuffer,ByteBuffer> attr=new HashMap<ByteBuffer,ByteBuffer>(0);
            tscan.setFilterString(ByteBuffer.wrap(filter.getBytes()));
            int id=client.scannerOpenWithScan(ByteBuffer.wrap(tableName.getBytes()),tscan,attr);
            List<TRowResult> results=client.scannerGetList(id,2);
            while(results!=null&&!results.isEmpty()){
                for(TRowResult result:results){
                    Iterator<Map.Entry<ByteBuffer, TCell>> iter = result.columns.entrySet().iterator();
                    System.out.println("RowKey=" + new String(result.getRow()));
                    while (iter.hasNext()) {
                        Map.Entry<ByteBuffer, TCell> entry = iter.next();
                        System.out.println("\tCol=" + new String(decode(entry.getKey())) + ", Value=" + new String(entry.getValue().getValue()));
                    }
                }
                results=client.scannerGetList(id,2);
            }
            client.scannerClose(id);
            transport.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
