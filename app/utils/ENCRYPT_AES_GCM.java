package utils;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ENCRYPT_AES_GCM {
	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_TAG_LENGTH = 16;

	public static byte[] aes_gcm_encrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		SecretKeySpec newKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

		cipher.init(Cipher.ENCRYPT_MODE, newKey);
		byte[] iv = cipher.getIV();
		byte[] encrypt = cipher.doFinal(data);
		byte[] message = new byte[GCM_IV_LENGTH + data.length + GCM_TAG_LENGTH];
		System.arraycopy(iv, 0, message, 0, GCM_IV_LENGTH);
		System.arraycopy(encrypt, 0, message, GCM_IV_LENGTH, encrypt.length);
		return  message;
	}

	public static final int SIZE_1K = 1024;
	public static void aes_gcm_encrypt_file(byte[] key, InputStream is, OutputStream os) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
		SecretKeySpec newKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey);

		byte[] iv = cipher.getIV();
		CipherInputStream cipherInputStream = new CipherInputStream(is, cipher);
		os.write(iv, 0, GCM_IV_LENGTH);

		int numRead = 0;
		byte[] buf = new byte[SIZE_1K];
		while ((numRead = cipherInputStream.read(buf)) != -1) {
			os.write(buf, 0, numRead);
		}
		cipherInputStream.close();
		is.close();
		os.close();
	}

	public static byte[] aes_gcm_decrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		SecretKeySpec newKey = new SecretKeySpec(key, "AES");

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec params = new GCMParameterSpec(GCM_TAG_LENGTH * 8, data, 0, GCM_IV_LENGTH);
		cipher.init(Cipher.DECRYPT_MODE, newKey, params);

		return cipher.doFinal(data, GCM_IV_LENGTH, data.length - GCM_IV_LENGTH);
	}

	public static void aes_gcm_decrypt_file(byte[] key, InputStream is, OutputStream os) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IOException {
		byte[] iv= new byte[GCM_IV_LENGTH];
		if(is.read(iv) <= 0) return;

		SecretKeySpec newKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec params = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv, 0, GCM_IV_LENGTH);
		cipher.init(Cipher.DECRYPT_MODE, newKey, params);

		CipherOutputStream cipherOutputStream = new CipherOutputStream(os, cipher);
		int numRead = 0;
		byte[] buf = new byte[SIZE_1K];
		while ((numRead = is.read(buf)) != -1) {
			cipherOutputStream.write(buf, 0, numRead);
		}
		cipherOutputStream.close();
		is.close();
		os.close();
	}
}
