package models.form.request.identity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import play.data.validation.Constraints;

@Data
public class TDidRegister {
    @ApiModelProperty(value = "Network (default: substrate)", example = "substrate", required= false)
    private String network;

    @ApiModelProperty(value = "Public key", example = "0x46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a", required= true)
    @Constraints.Required
    private String pubkey;

//    @ApiModelProperty(value = "Address with SS58 format", example = "5DfhGyQdFobKM8NsWvEeAKk5EQQgYe9AydgJ7rMB6E1EqRzV", required= true)
    @ApiModelProperty(value = "Decentralized Identifier (DID)", example = "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a", required= true)
    @Constraints.Required
    private String did;

    @ApiModelProperty(value = "Algorithm for signature (default: RS25519)", example = "RS25519", required= false)
    private String algorithm;   //ED25515/RS25519

    @ApiModelProperty(value = "Signature", example = "0x44bb3d0ee1abb64806ba1ce6ffc05413688d3d249a760e81ab51a8025bd8b83b5d33ff8c6d965c105d9ad2f72b52466cef3612e6359f3f368323050fa174cb81", required= true)
    @Constraints.Required
    private String signature;
}
