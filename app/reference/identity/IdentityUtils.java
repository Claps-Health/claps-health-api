package reference.identity;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

public class IdentityUtils {
    private IdentityUtils() {
        throw new IllegalStateException("IdentityConstants class");
    }
    public static String NETWORK_DEFAULT= "substrate";
    public static String ALGORITHM_DEFAULT= "RS25519";
    public static final byte[] DID_REGISTER_MESSAGE= "register for @ClapsHealth".getBytes(StandardCharsets.UTF_8);

    public enum DID_ACCOUNT_LEVEL {
        LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3
    }

    public static String generateDid(byte[] pubkey) {
//        return SS58Codec.encode(pubkey, SS58AddressFormat.of((short) 42));
        return Numeric.toHexString(Hash.sha256(pubkey));
    }



}
