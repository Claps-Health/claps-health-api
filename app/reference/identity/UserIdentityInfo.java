package reference.identity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import models.jpa.identity.UserIdentity;

@Data
public class UserIdentityInfo {
//    @ApiModelProperty(value = "Decentralized Identifier (DID)", example = "5DfhGyQdFobKM8NsWvEeAKk5EQQgYe9AydgJ7rMB6E1EqRzV", required= true)
    @ApiModelProperty(value = "Decentralized Identifier (DID)", example = "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a", required= true)
    private String did;

    @ApiModelProperty(value = "User's year of birth", example = "male", required= false)
    private Integer year_of_birth;

    @ApiModelProperty(value = "User's gender", example = "male", required= false)
    private String gender;

    @ApiModelProperty(value = "User's living country", example = "USA", required= false)
    private String living_country;

    @ApiModelProperty(value = "User's living city", example = "San Francisco", required= false)
    private String living_city;

    @ApiModelProperty(value = "Account Level(0-3)", example = "2", required= false)
    private Integer account_level;

    @ApiModelProperty(value = "Account creation timestamp", example = "2", required= true)
    private Long date_created;

    @ApiModelProperty(value = "Account update timestamp", example = "2", required= false)
    private Long date_updated;

    public UserIdentityInfo(UserIdentity ui) {
        if(ui==null) return;

        this.did= ui.getDid();
        this.year_of_birth= ui.getYear_of_birth();
        this.gender= ui.getGender();
        this.living_country= ui.getLiving_country();
        this.living_city= ui.getLiving_city();
        this.account_level= ui.getAccount_level();
        if(ui.getDate_created()!=null) this.date_created= ui.getDate_created().getTime()/1000;
        if(ui.getDate_updated()!=null) this.date_updated= ui.getDate_updated().getTime()/1000;
    }
}
