package models.leveldb;

import java.util.ArrayList;

public interface DbRecorderInterface {
    void addRecord(String did, String app_id, RecordSet data);
    ArrayList<RecordSet> getRecords(String did, String app_id);
    RecordSet getLastRecord(String did, String app_id);
}
