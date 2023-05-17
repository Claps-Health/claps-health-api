package models.secure;

import com.typesafe.config.ConfigFactory;
import utils.ENCRYPT_AES_GCM;
import utils.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AppEncryptManager implements EncryptManagerInterface {
    public static final boolean SECURITY_MODE= true;
    private static AppEncryptManager instance;

    private byte[] key;

    private AppEncryptManager() {
        this.key= ConfigFactory.load().getString("secure.key").getBytes();
    }

//    public static AppEncryptManager create() {
//        if(instance==null) instance= new AppEncryptManager();
//        return instance;
//    }

    public static AppEncryptManager getInstance() {
        if(instance==null) instance= new AppEncryptManager();
        return instance;
    }

    private byte[] getKey() { return key; }

    @Override
    public String encrypt(String in) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(in==null || in.isEmpty()) return in;
        if(!SECURITY_MODE) return in;

        byte[] out= ENCRYPT_AES_GCM.aes_gcm_encrypt(getKey(), in.getBytes(StandardCharsets.UTF_8));
        return Utils.encodeBase64String(out);
    }

    @Override
    public String decrypt(String en) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(en==null || en.isEmpty()) return en;
        if(!SECURITY_MODE) return en;

        byte[] raw= Utils.decodeBase64String(en);
        return new String(ENCRYPT_AES_GCM.aes_gcm_decrypt(getKey(), raw), StandardCharsets.UTF_8);
    }

    @Override
    public void encrypt_file(InputStream is, OutputStream os) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        if(is==null) return;
        if(os==null) return;
        if(!SECURITY_MODE) return;

        ENCRYPT_AES_GCM.aes_gcm_encrypt_file(getKey(), is, os);
    }

    @Override
    public void decrypt_file(InputStream is, OutputStream os) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        if(is==null) return;
        if(os==null) return;
        if(!SECURITY_MODE) return;

        ENCRYPT_AES_GCM.aes_gcm_decrypt_file(getKey(), is, os);
    }
}
