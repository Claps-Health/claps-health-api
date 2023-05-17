package reference.jwt;

import com.google.gson.JsonObject;
import com.strategyobject.substrateclient.crypto.SignatureData;
import org.web3j.utils.Numeric;
import reference.sr25519.Sr25519Imp;
import utils.Utils;

import java.nio.charset.StandardCharsets;

public class JwtRs25519Tester {
    private static final String JWT_HEADER = "{\"alg\":\"RS25519\",\"typ\":\"JWT\"}";

    public static String generate(Sr25519Imp sr25519, String subject, Long notBefore, Long expiresAt) {
        if(sr25519==null) return null;
        JsonObject jwtPayload  = new JsonObject();
        jwtPayload.addProperty("sub", subject);
        jwtPayload.addProperty("nbf", notBefore);
        jwtPayload.addProperty("exp", expiresAt);

        String raw = Utils.convertToBase64String(JWT_HEADER) + "." +  Utils.convertToBase64String(jwtPayload.toString());
        SignatureData signData = sr25519.sign(raw.getBytes(StandardCharsets.UTF_8));
        System.out.println("signData.getBytes()= "+ Numeric.toHexStringNoPrefix(signData.getBytes()));
        if(signData==null) return null;
        return raw + "." + Utils.convertToBase64String(Numeric.toHexStringNoPrefix(signData.getBytes()));
    }
}
