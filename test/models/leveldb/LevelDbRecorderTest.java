package models.leveldb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import play.libs.Json;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LevelDbRecorderTest {
    private LevelDbRecorder recorder;

    private final String did= "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a";
    private final String app_id= "mood_jouraling";

//    @Before
//    public void before() {
//    }

    @Test
    void addRecord() throws IOException {
        int loop= 30;
        LevelDbRecorder.destroy("challenge");
        recorder= new LevelDbRecorder("challenge");

        ArrayList<RecordSet> rss1= recorder.getRecords(did, app_id);

        for(int i=1;i<=loop;i++) {
            JsonObject jo= new JsonObject();
            jo.addProperty("mood", "happy");

//            JsonNode jo = new ObjectMapper().createObjectNode();
//            ((ObjectNode) jo).put("mood", "happy");
            RecordSet rs= new RecordSet(new RecordData(new RecordAppData(jo, Utils.addDaysWithUTC(i, false), null), new RecordBlockchainData("0x92354d95c16c860057cf82835a09403d6f21fce1bab1b28ea2182ec4e61c9aa6")));
            recorder.addRecord(did, app_id, rs);
        }

        ArrayList<RecordSet> rss2= recorder.getRecords(did, app_id);
        assertEquals(rss1.size()+loop, rss2.size());

        ArrayList<RecordSet> rss_search1= RecordSet.searchListByTime(rss2, Utils.addDaysWithUTC(loop/2, false), null);
        assertEquals((loop-loop/2), rss_search1.size());

        ArrayList<RecordSet> rss_search2= RecordSet.searchListByTime(rss2, Utils.addDaysWithUTC(loop/3, false), null);
        assertEquals((loop-loop/3), rss_search2.size());
    }

    @Test
    void getRecords() throws IOException {
        recorder= new LevelDbRecorder("challenge");

        ArrayList<RecordSet> rss1= recorder.getRecords(did, app_id);
        System.out.println("rss1.size()= "+ rss1.size());
    }

    @Test
    void destory() throws IOException {
        LevelDbRecorder.destroy("challenge");
    }

}