package reference.twitter;

import com.strategyobject.substrateclient.crypto.PublicKey;

public class TwitterSignVerifier {
    private static boolean isValidateVerifyMessage(String plain) {
        return TwitterUtils.isValidateVerifyMessageForTwitter(plain);
    }

    public static boolean isSignerWithSR25519(String plain, byte[] pubkey) {
        if(plain==null) return false;
        if(pubkey==null) return false;
        if(!isValidateVerifyMessage(plain)) return false;
        return TwitterUtils.verifyTweetSignMessage(TwitterUtils.getTwitterSignFromPlain(plain), PublicKey.fromBytes(pubkey));
    }
}
