package models.form.response.challenge;

import models.form.response.MethodResponse;
import models.leveldb.RecordSet;

import java.util.ArrayList;
import java.util.List;

public class MethodResponseChallengeRecords extends MethodResponse {
    public MethodResponseChallengeRecords(ArrayList<RecordSet> data) {
        this.data= data;
    }
    private ArrayList<RecordSet> data;

    public ArrayList<RecordSet> getData() {
        return data;
    }
}