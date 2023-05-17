package models.jpa.identity;

import io.ebean.Finder;
import io.ebean.Model;
import reference.identity.IdentityUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class UserIdentity extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String did;

    @Column(length = 255, nullable = false)
    private String pubkey;

    private Integer year_of_birth;

    @Column(length = 20, nullable = true)
    private String gender;

    @Column(length = 255, nullable = true)
    private String living_country;

    @Column(length = 255, nullable = true)
    private String living_city;

    @Column(length = 255, nullable = true)
    private String email_hash;

    private Boolean email_verified= false;

    @Column(length = 255, nullable = true)
    private String twitter_account_hash;

    private Boolean twitter_verified= false;

    private Integer account_level= IdentityUtils.DID_ACCOUNT_LEVEL.LEVEL_0.ordinal();

    @Column(columnDefinition = "datetime", nullable = false)
    protected Date date_created;

    @Column(columnDefinition = "datetime", nullable = true)
    protected Date date_updated;

    @OneToOne(targetEntity = UserIdentityVerification.class, fetch = FetchType.LAZY)
    private UserIdentityVerification verification;

    public static final Finder<Long, UserIdentity> finder = new Finder<>(UserIdentity.class);

    public UserIdentity(String did, String pubkey, Integer year_of_birth, String gender, String living_country, String living_city, Integer account_level) {
        this.did = did;
        this.pubkey= pubkey;
        this.year_of_birth = year_of_birth;
        this.gender = gender;
        this.living_country = living_country;
        this.living_city = living_city;
        this.email_verified= false;
        this.twitter_verified= false;
        this.account_level = account_level;
        this.date_created= new Date();
    }

    public static UserIdentity addUserIdentity(String did, String pubkey, Integer year_of_birth, String gender, String living_country, String living_city, Integer account_level) {
        UserIdentity identity= UserIdentity.findByDid(did);
        if(identity!=null) return identity;

        identity= new UserIdentity(did, pubkey, year_of_birth, gender, living_country, living_city, account_level);
        identity.save();
        return identity;
    }

    public static UserIdentity findByDid(String did) {
        return finder.query().where().eq("did", did).findOne();
    }

    public static List<UserIdentity> getAll() {
        return finder.query().findList();
    }

    public Long getId() {
        return id;
    }

    public String getDid() {
        return did;
    }

    public String getPubkey() {
        return pubkey;
    }

    public Integer getYear_of_birth() {
        return year_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public String getLiving_country() {
        return living_country;
    }

    public String getLiving_city() {
        return living_city;
    }

    public String getEmail_hash() {
        return email_hash;
    }

    public Boolean getEmail_verified() {
        return email_verified;
    }

    public String getTwitter_account_hash() {
        return twitter_account_hash;
    }

    public Boolean getTwitter_verified() {
        return twitter_verified;
    }

    public Integer getAccount_level() {
        return account_level;
    }

    public Date getDate_created() {
        return date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public UserIdentityVerification getVerification() {
        return verification;
    }

    public void setYear_of_birth(Integer year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLiving_country(String living_country) {
        this.living_country = living_country;
    }

    public void setLiving_city(String living_city) {
        this.living_city = living_city;
    }

    public void setEmail_hash(String email_hash) {
        this.email_hash = email_hash;
    }

    public void setEmail_verified(Boolean email_verified) {
        this.email_verified = email_verified;
        this.account_level= 0;
        if(this.email_verified!=null && this.email_verified) this.account_level ++;
        if(this.twitter_verified!=null && this.twitter_verified) this.account_level ++;
    }

    public void setTwitter_account_hash(String twitter_account_hash) {
        this.twitter_account_hash = twitter_account_hash;
    }

    public void setTwitter_verified(Boolean twitter_verified) {
        this.twitter_verified = twitter_verified;
        this.account_level= 0;
        if(this.email_verified!=null && this.email_verified) this.account_level ++;
        if(this.twitter_verified!=null && this.twitter_verified) this.account_level ++;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public void setVerification(UserIdentityVerification verification) {
        this.verification = verification;
    }

    @Override
    public void update() {
        setDate_updated(new Date());
        super.update();
    }
}
