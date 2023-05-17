package models.form.request.identity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import play.data.validation.Constraints;

@Data
public class TDidTwitterVerify {
//    @ApiModelProperty(value = "Address with SS58 format", example = "5DfhGyQdFobKM8NsWvEeAKk5EQQgYe9AydgJ7rMB6E1EqRzV", required= true)
    @ApiModelProperty(value = "Decentralized Identifier (DID)", example = "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a", required= true)
    @Constraints.Required
    private String did;
    @ApiModelProperty(value = "Algorithm for signature (default: RS25519)", example = "RS25519", required= false)
    private String algorithm;   //ED25515/RS25519

    @ApiModelProperty(value = "The verifiable link of the Tweet", example = "https://twitter.com/xxxxx/status/1655472862319493121", required= true)
    @Constraints.Required
    private String link;  //wait for verify
}
