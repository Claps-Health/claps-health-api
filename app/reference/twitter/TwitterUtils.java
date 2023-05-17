package reference.twitter;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SecretKey;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import reference.sr25519.type.nByte;

import java.nio.charset.StandardCharsets;

public class TwitterUtils {
    private TwitterUtils() {
        throw new IllegalStateException("TwitterUtils class");
    }

    private static final String DID_VERIFICATION_MESSAGE_TWITTER = "verify the own of tweet for @ClapHealth #ClapHealth";

    public static boolean isValidateVerifyMessageForTwitter(String tweet) {
        if(tweet==null) return false;
        return (tweet==null || tweet.indexOf(DID_VERIFICATION_MESSAGE_TWITTER)!=0)?false:true;
    }

    public static SignatureData signTweetVerifyMessage(PublicKey publicKey, SecretKey secretKey) {
        return new Sr25519NativeCryptoProvider().sign(publicKey, secretKey, new nByte(DID_VERIFICATION_MESSAGE_TWITTER.getBytes(StandardCharsets.UTF_8)));
    }

    public static boolean verifyTweetSignMessage(byte[] sign, PublicKey publicKey) {
        if(sign==null || sign.length==0) return false;
        return new Sr25519NativeCryptoProvider().verify(SignatureData.fromBytes(sign), new nByte(DID_VERIFICATION_MESSAGE_TWITTER.getBytes(StandardCharsets.UTF_8)), publicKey);
    }

    public static String generateTweetPlainForSign(byte[] sign) {
        if(sign==null) return null;
        return DID_VERIFICATION_MESSAGE_TWITTER + " sig:" + HexConverter.toHex(sign);
    }

    public static byte[] getTwitterSignFromPlain(String plain) {
        if(plain==null || plain.isEmpty()) return null;
        String prefix= DID_VERIFICATION_MESSAGE_TWITTER + " sig:";
        int idx= plain.indexOf(prefix);
        String sig= plain.substring(idx+prefix.length());
        return (sig!=null && !sig.isEmpty())?HexConverter.toBytes(sig):null;
    }
}
