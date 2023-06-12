package reference.jwt;

import com.google.gson.JsonObject;
import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SecretKey;
import models.form.request.challenge.TChallengeRecordAdd;
import models.form.request.identity.TDidProfileUpdate;
import models.form.response.MethodResponse;
import models.form.response.MethodResponseNoData;
import models.form.response.challenge.MethodResponseChallengeRecords;
import models.form.response.identity.MethodResponseIdentityInfo;
import models.leveldb.RecordAppData;
import models.leveldb.RecordBlockchainData;
import models.leveldb.RecordData;
import org.junit.jupiter.api.Test;
import reference.error.ERROR_ENUM;
import reference.sr25519.Sr25519Imp;
import utils.HttpUtils;
import utils.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

class JwtRs25519TesterTest {

    private PublicKey publicKey= PublicKey.fromBytes(HexConverter.toBytes("0x46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a"));
    private SecretKey secretKey= SecretKey.fromBytes(HexConverter.toBytes("0x28b0ae221c6bb06856b287f60d7ea0d98552ea5a16db16956849aa371db3eb51fd190cce74df356432b410bd64682309d6dedb27c76845daf388557cbac3ca34"));
    private String subject= "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a";

    @Test
    void getUser() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30, true), Utils.addDaysWithUTC(90, true));
        assertNotNull(jwt);

        //test with local server
        MethodResponseIdentityInfo res= HttpUtils.httpGetWithJwt("http://localhost:1111/v1/api/did/user", null, jwt, MethodResponseIdentityInfo.class);
        assertNotNull(res);
        assertEquals(((MethodResponse)res).getError(), ERROR_ENUM.ERR_NOERROR.getId());
        System.out.println("res= "+Utils.toJson(res));
    }

    @Test
    void updateUserProfile() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30, true), Utils.addDaysWithUTC(90, true));
        assertNotNull(jwt);

        //test with local server
        TDidProfileUpdate fUpdate= new TDidProfileUpdate();
        fUpdate.setYear_of_birth(1990);
        fUpdate.setGender("Male");
        fUpdate.setLiving_country("USA");
        fUpdate.setLiving_city("San francisco");

        MethodResponseIdentityInfo res= HttpUtils.httpPostWithJwt("http://localhost:1111/v1/api/did/user/profile/update", null, jwt, fUpdate, MethodResponseIdentityInfo.class);
        assertNotNull(res);
        assertEquals(((MethodResponse)res).getError(), ERROR_ENUM.ERR_NOERROR.getId());
        System.out.println("res= "+Utils.toJson(res));
    }

    @Test
    void addRecord() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30, true), Utils.addDaysWithUTC(90, true));
        assertNotNull(jwt);

        //test with local server
        JsonObject jo= new JsonObject();
        jo.addProperty("mood", "happy");

//        JsonNode jo = new ObjectMapper().createObjectNode();
//        ((ObjectNode) jo).put("mood", "happy");

        TChallengeRecordAdd fAdd= new TChallengeRecordAdd();
        fAdd.setApp_id("mood_jouraling");
        fAdd.setData(new RecordData(new RecordAppData(jo, Utils.getNowTimeUtcLong(false), null), new RecordBlockchainData("0x92354d95c16c860057cf82835a09403d6f21fce1bab1b28ea2182ec4e61c9aa6")));

        //test
//        TChallengeRecordAdd rAdd = Json.fromJson(Json.parse(Utils.toJson(fAdd)), TChallengeRecordAdd.class);
//        Logger.debug("rAdd = " + Utils.toJson(rAdd));

        MethodResponseNoData res= HttpUtils.httpPostWithJwt("http://localhost:1111/v1/api/app/challenge/record/add", null, jwt, fAdd, MethodResponseNoData.class);
        assertNotNull(res);
        assertEquals(res.getError(), ERROR_ENUM.ERR_NOERROR.getId());
    }

    @Test
    void getRecords() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30, true), Utils.addDaysWithUTC(90, true));
        assertNotNull(jwt);

        String app_id= "mood_jouraling";
        MethodResponseChallengeRecords res= HttpUtils.httpGetWithJwt("http://localhost:1111/v1/api/app/challenge/records/time/"+app_id+"/"+Utils.addDaysWithUTC(-30, false), null, jwt, MethodResponseChallengeRecords.class);
        assertNotNull(res);
        assertEquals(((MethodResponse)res).getError(), ERROR_ENUM.ERR_NOERROR.getId());
    }

}