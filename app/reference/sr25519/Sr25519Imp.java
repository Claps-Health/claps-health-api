package reference.sr25519;

import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SecretKey;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import lombok.Data;
import reference.sr25519.type.nByte;

@Data
public class Sr25519Imp extends Sr25519NativeCryptoProvider {
    private PublicKey publicKey;
    private SecretKey secretKey;

    public Sr25519Imp(PublicKey publicKey, SecretKey secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public Sr25519Imp(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public SignatureData sign(byte[] message) {
        if(publicKey==null) throw new IllegalArgumentException("publicKey is marked non-null but is null");
        if(secretKey==null) throw new IllegalArgumentException("secretKey is marked non-null but is null");
        return sign(publicKey, secretKey, new nByte(message));
    }

    public boolean verify(SignatureData signature, byte[] message_signed) {
        if(publicKey==null) throw new IllegalArgumentException("publicKey is marked non-null but is null");
        return verify(signature, new nByte(message_signed), publicKey);
    }
}
