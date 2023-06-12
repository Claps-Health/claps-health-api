package models.leveldb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordBlockchainData {
    @ApiModelProperty(value = "tx /extrinsics of blockchain", example = "0x92354d95c16c860057cf82835a09403d6f21fce1bab1b28ea2182ec4e61c9aa6", required= false)
    private String txid;

    public RecordBlockchainData(String txid) {
        this.txid = txid;
    }
}
