package controllers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jwt.JwtControllerHelper;
import models.form.request.challenge.TChallengeRecordAdd;
import models.form.request.identity.TDidProfileUpdate;
import models.form.request.identity.TDidRegister;
import models.form.request.identity.TDidTwitterVerify;
import models.form.response.MethodResponseError;
import models.form.response.MethodResponseNoData;
import models.form.response.challenge.MethodResponseChallengeRecords;
import models.form.response.identity.MethodResponseIdentityInfo;
import models.form.response.register.MethodResponseRegisterDid;
import models.form.response.register.MethodResponseVerifyTwitter;
import models.jpa.identity.UserIdentity;
import models.leveldb.LevelDbRecorder;
import models.leveldb.RecordSet;
import models.twitter.TwitterApiWrapper;
import models.twitter.response.TweetInfo;
import models.twitter.response.TwitterUserInfo;
import modules.EbeanServerProvider;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import reference.error.ERROR_ENUM;
import reference.identity.IdentityUtils;
import reference.identity.UserIdentityInfo;
import reference.sr25519.type.nByte;
import reference.twitter.TwitterSignVerifier;
import utils.Utils;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Api(value = "claps-health api v1.1.0", produces = "application/json")
public class ServiceController extends Controller {
    @Inject
    FormFactory formFactory;

    private static EbeanServerProvider ebeanServer;

    @Inject
    private JwtControllerHelper jwtControllerHelper;

    @Inject
    private Config config;

    private TwitterApiWrapper twitterApiWrapper;
    private static LevelDbRecorder recorder;

    @Inject
    public ServiceController(EbeanServerProvider ebeanServer) throws IOException {
        Logger.debug("ServiceController Constructor");
        this.config= ConfigFactory.load();

        initSql(ebeanServer);
        initRecorder();
        initTwitterApiWrapper();
    }

    void initSql(EbeanServerProvider ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    void initRecorder() throws IOException {
        recorder= new LevelDbRecorder("challenge");
    }

    private String toJson(Object obj) {
        return Utils.toJson(obj);
    }

    void initTwitterApiWrapper() {
        twitterApiWrapper= new TwitterApiWrapper(config.getString("twitter.apikey"));
    }

    /**
     * Register a Decentralized Identifier (DID).
     *
     * @param body The body data containing the details.
     *                 Expected fields:
     *                 - network: Network (default: substrate).
     *                 - pubkey: Public key.
     *                 - did: Decentralized Identifier (DID).
     *                 - algorithm: Algorithm for signature (default: RS25519).
     *                 - signature: Signature.
     * @return The user did if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Register a Decentralized Identifier (DID)", notes = "", response = MethodResponseRegisterDid.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(dataType = "models.form.request.identity.TDidRegister", required = true, paramType = "body")
            }
    )
    @BodyParser.Of(value = BodyParser.Json.class)
    public Result registerDid() {
        TDidRegister fRegister = Json.fromJson(request().body().asJson(), TDidRegister.class);
        Logger.debug("fRegister = " + Utils.toJson(fRegister));

        //check existed user
        UserIdentity ui= UserIdentity.findByDid(fRegister.getDid());
        if(ui!=null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_EXISTED)));

        if(fRegister.getNetwork()!=null && !fRegister.getNetwork().equals(IdentityUtils.NETWORK_DEFAULT)) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_NETWORK_NOT_SUPPORT)));
        if(fRegister.getAlgorithm()!=null && !fRegister.getAlgorithm().equals(IdentityUtils.ALGORITHM_DEFAULT)) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_ALGORITHM_NOT_SUPPORT)));

        String address_expected = IdentityUtils.generateDid(HexConverter.toBytes(fRegister.getPubkey()));
        if(!fRegister.getDid().equals(address_expected)) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_ADDRESS_WRONG)));

        SignatureData signData= SignatureData.fromBytes(HexConverter.toBytes(fRegister.getSignature()));
        boolean verify= new Sr25519NativeCryptoProvider().verify(signData, new nByte(IdentityUtils.DID_REGISTER_MESSAGE), PublicKey.fromBytes(HexConverter.toBytes(fRegister.getPubkey())));
        if(!verify) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_SIGNATURE_WRONG)));

        UserIdentity.addUserIdentity(fRegister.getDid(), fRegister.getPubkey(), null, null, null, null, IdentityUtils.DID_ACCOUNT_LEVEL.LEVEL_0.ordinal());
        return ok(toJson(new MethodResponseRegisterDid(fRegister.getDid())));
    }

    /**
     * Verify Twitter Account for DID.
     *
     * @param body The body data containing the details.
     *                 Expected fields:
     *                 - did: Decentralized Identifier (DID).
     *                 - algorithm: Algorithm for signature (default: RS25519).
     *                 - link: The verifiable link of the Tweet.
     * @return The user did if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Verify Twitter Account for DID", notes = "", response = MethodResponseVerifyTwitter.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(dataType = "models.form.request.identity.TDidTwitterVerify", required = true, paramType = "body")
            }
    )
    @BodyParser.Of(value = BodyParser.Json.class)
    public Result verifyTwitter() {
        TDidTwitterVerify fVerify = Json.fromJson(request().body().asJson(), TDidTwitterVerify.class);
        Logger.debug("fVerify = " + Utils.toJson(fVerify));

        if(fVerify.getAlgorithm()!=null && !fVerify.getAlgorithm().equals(IdentityUtils.ALGORITHM_DEFAULT)) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_ALGORITHM_NOT_SUPPORT)));
        //check existed user
        UserIdentity ui= UserIdentity.findByDid(fVerify.getDid());
        if(ui==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_NOT_FOUND)));
        if(ui.getTwitter_verified()) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_TWITTER_VERIFIED)));

        TweetInfo ti= twitterApiWrapper.createTweetParser(fVerify.getLink()).getTweetInfo();
        if(ti==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_TWITTER_API_ERROR)));
        if(!TwitterSignVerifier.isSignerWithSR25519(ti.getData().getText(), HexConverter.toBytes(ui.getPubkey()))) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_SIGNATURE_WRONG)));
        TwitterUserInfo tui= twitterApiWrapper.getUserInfo(ti.getData().getAuthor_id());
        if(tui==null || tui.getData()==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_TWITTER_API_ERROR)));

//        record to other table ???
//        UserIdentityVerification uiv= UserIdentityVerification.addUserIdentityVerification(user.getDid(), "twitter", fVerify.getLink(), ti.getData().getAuthor_id());
//        user.setVerification(uiv);
//        user.update();

        ui.setTwitter_account_hash(Numeric.toHexString(Hash.sha256(ti.getData().getAuthor_id().getBytes(StandardCharsets.UTF_8))));
        ui.setTwitter_verified(true);
        ui.update();

        return ok(toJson(new MethodResponseVerifyTwitter(fVerify.getDid())));
    }

    @ApiOperation(value = "Get User Info (without authentication for testing)", notes = "", response = MethodResponseIdentityInfo.class)
    public Result getIdentityInfoWithoutJwt(String did) {
        UserIdentity user= UserIdentity.findByDid(did);
        if(user==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_NOT_FOUND)));
        return ok(toJson(new MethodResponseIdentityInfo(new UserIdentityInfo(user))));
    }

    /**
     * Get user profile.
     *
     * @return The user info if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Get User Info", notes = "", response = MethodResponseIdentityInfo.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "Authorization", value = "JWT", dataType="string", required = true, paramType = "header")
            }
    )
    public Result getIdentityInfo() {
        return jwtControllerHelper.verify(request(), res -> {
            if (res.left.isPresent()) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_AUTH_FAIL)));
            return ok(toJson(new MethodResponseIdentityInfo(new UserIdentityInfo(UserIdentity.findByDid(res.right.get().getSubject())))));
        });
    }

    /**
     * Update user profile.
     *
     * @param body The body data containing the details.
     *                 Expected fields:
     *                 - year_of_birth: Year of birthday.
     *                 - gender: Gender.
     *                 - living_country: Country of living.
     *                 - living_city: City of living.
     * @return error equals 0 if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Update user profile", notes = "", response = MethodResponseNoData.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "Authorization", value = "JWT", dataType="string", required = true, paramType = "header"),
                    @ApiImplicitParam(dataType = "models.form.request.identity.TDidProfileUpdate", required = true, paramType = "body")
            }
    )
    @BodyParser.Of(value = BodyParser.Json.class)
    public Result updateIdentityProfile() {
        return jwtControllerHelper.verify(request(), res -> {
            if (res.left.isPresent()) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_AUTH_FAIL)));

            TDidProfileUpdate fUpdate = Json.fromJson(request().body().asJson(), TDidProfileUpdate.class);
            Logger.debug("fUpdate = " + Utils.toJson(fUpdate));

            UserIdentity ui= UserIdentity.findByDid(res.right.get().getSubject());
            if(ui==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_NOT_FOUND)));

            ui.setYear_of_birth(fUpdate.getYear_of_birth());
            ui.setGender(fUpdate.getGender());
            ui.setLiving_country(fUpdate.getLiving_country());
            ui.setLiving_city(fUpdate.getLiving_city());
            ui.update();

            return ok(toJson(new MethodResponseNoData()));
        });
    }

    /**
     * Add a record for challenge.
     *
     * @param body The body data containing the details.
     *                 Expected fields:
     *                 - app_id: The application ID for which to retrieve the challenge records, e.g: mood_jouraling/ reduce_alcohol/ body_pain etc.
     *                 - data: The application structured data
     * @return error equals 0 if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Add a record for challenge", notes = "", response = MethodResponseNoData.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "Authorization", value = "JWT", dataType="string", required = true, paramType = "header"),
                    @ApiImplicitParam(dataType = "models.form.request.challenge.TChallengeRecordAdd", required = true, paramType = "body")
            }
    )
    @BodyParser.Of(value = BodyParser.Json.class)
    public Result addChallengeRecord() {
        return jwtControllerHelper.verify(request(), res -> {
            if (res.left.isPresent()) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_AUTH_FAIL)));

            TChallengeRecordAdd fAdd = Json.fromJson(request().body().asJson(), TChallengeRecordAdd.class);
            Logger.debug("fAdd = " + Utils.toJson(fAdd));

            UserIdentity ui= UserIdentity.findByDid(res.right.get().getSubject());
            if(ui==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_NOT_FOUND)));

            //check on day limitaion with last record??
//            RecordSet rs= recorder.getLastRecord(ui.getDid(), fAdd.getApp_id());
//            if(rs!=null && ....)

            RecordSet rs= new RecordSet(fAdd.getData());
            recorder.addRecord(ui.getDid(), fAdd.getApp_id(), rs);

            return ok(toJson(new MethodResponseNoData()));
        });
    }

    /**
     * Get the challenge records by time.
     *
     * @param app_id       The application ID for which to retrieve the challenge records, e.g: mood_jouraling/ reduce_alcohol/ body_pain etc.
     * @param time_start     The starting time of the time range, represented as a Unix timestamp.
     * @return            The records info if success, or give a specific error if failure.
     */
    @ApiOperation(value = "Get the challenge records by time", notes = "app_id: mood_jouraling/ reduce_alcohol/ body_pain\r\ntime_start: timestamp(ms)", response = MethodResponseChallengeRecords.class)
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "Authorization", value = "JWT", dataType="string", required = true, paramType = "header"),
                    @ApiImplicitParam(name = "app_id", value = "app_id", dataType="string", required = true, paramType = "path"),
                    @ApiImplicitParam(name = "time_start", value = "time_start", dataType="long", required = false, paramType = "path"),
            }
    )
    public Result getChallengeRecordsByTime(String app_id, Long time_start) {
        return jwtControllerHelper.verify(request(), res -> {
            if (res.left.isPresent()) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_AUTH_FAIL)));

            UserIdentity ui= UserIdentity.findByDid(res.right.get().getSubject());
            if(ui==null) return ok(toJson(new MethodResponseError(ERROR_ENUM.ERR_USER_NOT_FOUND)));
            return ok(toJson(new MethodResponseChallengeRecords(RecordSet.searchListByTime(recorder.getRecords(ui.getDid(), app_id), time_start, null))));
        });
    }

    public static void terminate() {
        if(ebeanServer !=null) ebeanServer.dispose();
        if(recorder!=null) recorder.close();
    }
}
