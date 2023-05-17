package models.jpa.identity;

import io.ebean.Finder;
import io.ebean.Model;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
public class UserIdentityVerification extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String did;

//    @Column(length = 32, nullable = false)
//    private String algorithm;

    @Column(length = 32, nullable = false)
    private String verification_type;   //twitter/fb/email

    @Column(length = 1024, nullable = true)
    private String link;

    @Column(length = 255, nullable = true)
    private String account;

    @Column(length = 255, nullable = true)
    private String account_hash;

//    @Column(length = 1024, nullable = false)
//    private String profile_image_url;

    private Boolean verified= false;

    public static final Finder<Long, UserIdentityVerification> finder = new Finder<>(UserIdentityVerification.class);

    public UserIdentityVerification(String did, String verification_type, String link, String account) {
        this.did = did;
        this.verification_type = verification_type;
        this.link = link;
        this.account= account;
        this.account_hash= Numeric.toHexString(Hash.sha256(account.getBytes(StandardCharsets.UTF_8)));
        this.verified= true;
    }

    public static UserIdentityVerification addUserIdentityVerification(String did, String verification_type, String link, String account) {
        UserIdentityVerification identity= UserIdentityVerification.getUserIdentityVerificationByDid(did);
        if(identity!=null) return identity;

        identity= new UserIdentityVerification(did, verification_type, link, account);
        identity.save();
        return identity;
    }

    public static UserIdentityVerification getUserIdentityVerificationByDid(String did) {
        return finder.query().where().eq("did", did).findOne();
    }

    public static List<UserIdentityVerification> getAll() {
        return finder.query().findList();
    }

    public Long getId() {
        return id;
    }

    public String getDid() {
        return did;
    }

    public String getVerification_type() {
        return verification_type;
    }

    public String getLink() {
        return link;
    }

    public String getAccount() {
        return account;
    }

    public String getAccount_hash() {
        return account_hash;
    }

    public Boolean getVerified() {
        return verified;
    }
}
