package models.leveldb;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordData {
    private RecordAppData app;
    private RecordBlockchainData blockchain;

    public RecordData(RecordAppData app, RecordBlockchainData blockchain) {
        this.app = app;
        this.blockchain = blockchain;
    }
}
