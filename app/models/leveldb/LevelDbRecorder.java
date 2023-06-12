package models.leveldb;

import com.google.gson.reflect.TypeToken;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteOptions;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LevelDbRecorder implements DbRecorderInterface {
    private static final String DB_ROOT = "chdb";
    private DB db;
    private Object lock = new Object();
    private DBFactory factory = Iq80DBFactory.factory;
    private WriteOptions writeOptions = new WriteOptions().sync(false);

    public LevelDbRecorder(final String dbFolder) throws IOException {
        File dir = new File(DB_ROOT);
        if (!dir.exists()) dir.mkdir();
        Options options = new Options().createIfMissing(true);
        db= factory.open(new File(DB_ROOT + ((dbFolder!=null)?("/"+dbFolder):"")), options);
    }

    public static byte[] deriveKey(String did, String app_type) {
        return Hash.sha256(did.concat(app_type).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void addRecord(String did, String app_id, RecordSet data) {
        synchronized (lock) {
            if (data == null) return;
            ArrayList<RecordSet> list = getRecords(did, app_id);
            list.add(data);
            db.put(deriveKey(did, app_id), Utils.toJson(list).getBytes(StandardCharsets.UTF_8), writeOptions);
        }
    }

    @Override
    public ArrayList<RecordSet> getRecords(String did, String app_id) {
        byte[] bs= db.get(deriveKey(did, app_id));
        if(bs==null || bs.length==0) return new ArrayList();
        ArrayList<RecordSet> list= Utils.getClassObjFromJsonString(new String(bs, StandardCharsets.UTF_8), new TypeToken<List<RecordSet>>(){}.getType());
        if(list==null) list= new ArrayList();
        return list;
    }

    @Override
    public RecordSet getLastRecord(String did, String app_id) {
        byte[] bs= db.get(deriveKey(did, app_id));
        if(bs==null || bs.length==0) return null;
        ArrayList<RecordSet> list= Utils.getClassObjFromJsonString(new String(bs, StandardCharsets.UTF_8), new TypeToken<List<RecordSet>>(){}.getType());
        if(list==null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }

    public static void destroy(final String dbFolder) throws IOException {
        Options options = new Options();
        Iq80DBFactory.factory.destroy(new File(DB_ROOT + ((dbFolder!=null)?("/"+dbFolder):"")), options);
    }

    public void close() {
        try {
            if(db!=null) db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

