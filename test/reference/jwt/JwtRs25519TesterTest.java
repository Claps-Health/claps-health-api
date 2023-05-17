package reference.jwt;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SecretKey;
import models.form.request.identity.TDidProfileUpdate;
import models.form.response.identity.MethodResponseIdentityInfo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import reference.sr25519.Sr25519Imp;
import utils.HttpUtils;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static play.mvc.Http.Status.OK;

class JwtRs25519TesterTest {

    private PublicKey publicKey= PublicKey.fromBytes(HexConverter.toBytes("0x46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a"));
    private SecretKey secretKey= SecretKey.fromBytes(HexConverter.toBytes("0x28b0ae221c6bb06856b287f60d7ea0d98552ea5a16db16956849aa371db3eb51fd190cce74df356432b410bd64682309d6dedb27c76845daf388557cbac3ca34"));
    private String subject= "0xf407beb5eb86963ce7d2994f46bb162c62e4612c8b58262acc0c5409a075fc3a";

    @Test
    void getUser() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30), Utils.addDaysWithUTC(90));
        assertNotNull(jwt);
        System.out.println("jwt= "+jwt);

        //test with local server
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", "Bearer "+jwt));
        Object res= HttpUtils.httpGet("http://localhost:1111/v1/api/did/user", headers, Object.class);
        assertNotNull(res);
        System.out.println("res= "+Utils.toJson(res));
    }

    @Test
    void updateUserProfile() {
        String jwt= JwtRs25519Tester.generate(new Sr25519Imp(publicKey, secretKey), subject, Utils.addSecondsWithUTC(-30), Utils.addDaysWithUTC(90));
        assertNotNull(jwt);
        System.out.println("jwt= "+jwt);

        //test with local server
        TDidProfileUpdate fUpdate= new TDidProfileUpdate();
        fUpdate.setYear_of_birth(1990);
        fUpdate.setGender("Male");
        fUpdate.setLiving_country("USA");
        fUpdate.setLiving_city("San francisco");

        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", "Bearer "+jwt));
        Object res= HttpUtils.httpPost("http://localhost:1111/v1/api/did/user/profile/update", headers, fUpdate, Object.class);
        assertNotNull(res);
        System.out.println("res= "+Utils.toJson(res));
    }
}