/**
 * Created by guguli on 2015/12/29.
 */

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.thrift.generated.IOError;
import org.apache.hadoop.hbase.thrift.generated.Mutation;
import org.apache.hadoop.hbase.thrift.generated.TCell;
import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.thrift.TException;


public class HBaseThriftClient extends AbstractHBaseThriftService {

    public HBaseThriftClient() {
        super();
    }

    public HBaseThriftClient(String host, int port) {
        super(host, port);
    }

    @Override
    public List<String> getTables() throws TException {
        List<String> list = new ArrayList<String>(0);
        for (ByteBuffer buf : client.getTableNames()) {
            byte[] name = decode(buf);
            list.add(new String(name));
        }
        return list;
    }

    static ByteBuffer wrap(String value) {
        ByteBuffer bb = null;
        try {
            bb = ByteBuffer.wrap(value.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bb;
    }

    protected byte[] decode(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        for (int i = 0; i < buffer.limit(); i++) {
            bytes[i] = buffer.get();
        }
        return bytes;
    }

    @Override
    public void update(String table, String rowKey, boolean writeToWal,
                       Map<String, String> fieldNameValues, Map<String, String> attributes) throws TException {
        List<Mutation> mutations = new ArrayList<Mutation>();
        for(Map.Entry<String, String> entry : fieldNameValues.entrySet()) {
            mutations.add(new Mutation(false, wrap(entry.getKey()), wrap(entry.getValue()), writeToWal));
        }
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        ByteBuffer tableName = wrap(table);
        ByteBuffer row = wrap(rowKey);
        client.mutateRow(tableName, row, mutations, wrappedAttributes);
    }

    @Override
    public void update(String table, String rowKey, boolean writeToWal,
                       String fieldName, String fieldValue, Map<String, String> attributes) throws IOError, TException {
        Map<String, String> fieldNameValues = new HashMap<String, String>();
        fieldNameValues.put(fieldName, fieldValue);
        update(table, rowKey, writeToWal, fieldNameValues, attributes);
    }


    @Override
    public void deleteCells(String table, String rowKey, boolean writeToWal,
                            List<String> columns, Map<String, String> attributes) throws TException {
        List<Mutation> mutations = new ArrayList<Mutation>();
        for(String column : columns) {
            mutations.add(new Mutation(false, wrap(column), null, writeToWal));
        }
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        ByteBuffer tableName = wrap(table);
        ByteBuffer row = wrap(rowKey);
        client.mutateRow(tableName, row, mutations, wrappedAttributes);
    }

    @Override
    public void deleteCell(String table, String rowKey, boolean writeToWal,
                           String column, Map<String, String> attributes) throws TException {
        List<String> columns = new ArrayList<String>(1);
        columns.add(column);
        deleteCells(table, rowKey, writeToWal, columns, attributes);
    }


    @Override
    public void deleteRow(String table, String rowKey, Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        ByteBuffer row = wrap(rowKey);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        client.deleteAllRow(tableName, row, wrappedAttributes);
    }


    @Override
    public int scannerOpen(String table, String startRow, List<String> columns,
                           Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> fl = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.scannerOpen(tableName, wrap(startRow), fl, wrappedAttributes);
    }

    @Override
    public int scannerOpen(String table, String startRow, String stopRow, List<String> columns,
                           Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> fl = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.scannerOpenWithStop(tableName, wrap(startRow), wrap(stopRow), fl, wrappedAttributes);
    }

    @Override
    public int scannerOpenWithPrefix(String table, String startAndPrefix, List<String> columns,
                                     Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> fl = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.scannerOpenWithPrefix(tableName, wrap(startAndPrefix), fl, wrappedAttributes);
    }

    @Override
    public int scannerOpenTs(String table, String startRow, List<String> columns,
                             long timestamp, Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> fl = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.scannerOpenTs(tableName, wrap(startRow), fl, timestamp, wrappedAttributes);
    }

    @Override
    public int scannerOpenTs(String table, String startRow, String stopRow, List<String> columns,
                             long timestamp, Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> fl = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.scannerOpenWithStopTs(tableName, wrap(startRow), wrap(stopRow), fl, timestamp, wrappedAttributes);
    }

    @Override
    public List<TRowResult> scannerGetList(int id, int nbRows) throws TException {
        return client.scannerGetList(id, nbRows);
    }

    @Override
    public List<TRowResult> scannerGet(int id) throws TException {
        return client.scannerGetList(id, 1);
    }

    @Override
    public List<TRowResult> getRow(String table, String row,
                                   Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.getRow(tableName, wrap(row), wrappedAttributes);
    }

    @Override
    public List<TRowResult> getRows(String table, List<String> rows,
                                    Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        List<ByteBuffer> wrappedRows = encodeRows(rows);
        return client.getRows(tableName, wrappedRows, wrappedAttributes);
    }

    @Override
    public List<TRowResult> getRowsWithColumns(String table, List<String> rows,
                                               List<String> columns, Map<String, String> attributes) throws TException {
        ByteBuffer tableName = wrap(table);
        List<ByteBuffer> wrappedRows = encodeRows(rows);
        List<ByteBuffer> wrappedColumns = encodeColumns(columns);
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = encodeAttributes(attributes);
        return client.getRowsWithColumns(tableName, wrappedRows, wrappedColumns, wrappedAttributes);
    }

    private List<ByteBuffer> encodeColumns(List<String> columns) {
        List<ByteBuffer> fl = new ArrayList<ByteBuffer>(0);
        for(String column : columns) {
            fl.add(wrap(column));
        }
        return fl;
    }

    private Map<ByteBuffer, ByteBuffer> encodeAttributes(Map<String, String> attributes) {
        Map<ByteBuffer, ByteBuffer> wrappedAttributes = null;
        if(attributes != null && !attributes.isEmpty()) {
            wrappedAttributes = new HashMap<ByteBuffer, ByteBuffer>(1);
            for(Map.Entry<String, String> entry : attributes.entrySet()) {
                wrappedAttributes.put(wrap(entry.getKey()), wrap(entry.getValue()));
            }
        }
        return wrappedAttributes;
    }

    private List<ByteBuffer> encodeRows(List<String> rows) {
        List<ByteBuffer> list = new ArrayList<ByteBuffer>(0);
        for(String row : rows) {
            list.add(wrap(row));
        }
        return list;
    }

    @Override
    public void iterateResults(TRowResult result) {
        Iterator<Entry<ByteBuffer, TCell>> iter = result.columns.entrySet().iterator();
        System.out.println("RowKey=" + new String(result.getRow()));
        while (iter.hasNext()) {
            Entry<ByteBuffer, TCell> entry = iter.next();
            System.out.println("\tCol=" + new String(decode(entry.getKey())) + ", Value=" + new String(entry.getValue().getValue()));
        }
    }

    @Override
    public void scannerClose(int id) throws TException {
        client.scannerClose(id);
    }
}