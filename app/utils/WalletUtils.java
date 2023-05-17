package utils;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

public class WalletUtils {
    private WalletUtils() {
        throw new IllegalStateException("WalletUtils class");
    }

    public static String generateWalletFile(String password, String private_key) {
        String fileName= null;
        ECKeyPair kp= ECKeyPair.create(Hex.decode(private_key));
        try {
            fileName= org.web3j.crypto.WalletUtils.generateWalletFile(password, kp, new File("."), true);
            System.out.println("fileName= " + fileName);
        } catch (CipherException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return fileName;
    }

    public static String generateWalletFile(String password, String private_key, File destinationDirectory) {
        if(!destinationDirectory.exists()) destinationDirectory.mkdir();

        String fileName= null;
        ECKeyPair kp= ECKeyPair.create(Hex.decode(private_key));
        try {
            fileName= org.web3j.crypto.WalletUtils.generateWalletFile(password, kp, destinationDirectory, false);
        } catch (CipherException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * Generates a BIP-39 compatible Ethereum wallet. The private key for the wallet can
     * be calculated using following algorithm:
     * <pre>
     *     Key = SHA-256(BIP_39_SEED(mnemonic, password))
     * </pre>
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip39Wallet(String bip39Passphrase, String walletPassword, File destinationDirectory) throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, bip39Passphrase);
        ECKeyPair privateKey = ECKeyPair.create(Hash.sha256(seed));
        String walletFile = org.web3j.crypto.WalletUtils.generateWalletFile(walletPassword, privateKey, destinationDirectory, false);
        return new Bip39Wallet(walletFile, mnemonic);
    }

//    public static Bip39Wallet generateBip39WalletED25519(String bip39Passphrase, String walletPassword, File destinationDirectory) throws CipherException, IOException {
//        byte[] initialEntropy = new byte[16];
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(initialEntropy);
//        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, bip39Passphrase);
//        ECKeyPair privateKey = ECKeyPair.create(Hash.sha256(seed));
//        KeyPair keyPair = KeyPair.fromSecretSeed(seed, ExpansionMode.Ed25519);
//
//        System.out.println("keyPair.getPrivateKey().getKey()= "+ HexUtils.bytesToHex(keyPair.getPrivateKey().getKey()));
//
//
//        String walletFile = org.web3j.crypto.WalletUtils.generateWalletFile(walletPassword, privateKey, destinationDirectory, false);
//        return new Bip39Wallet(walletFile, mnemonic);
//    }

    /**
     * Generates a BIP-44 compatible Ethereum wallet on top of BIP-39 generated seed.
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @param testNet should use the testNet derive path
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip44Wallet(String bip39Passphrase, String walletPassword, File destinationDirectory, boolean testNet) throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, bip39Passphrase);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = org.web3j.crypto.Bip44WalletUtils.generateBip44KeyPair(masterKeypair, testNet);
        String walletFile = org.web3j.crypto.Bip44WalletUtils.generateWalletFile(walletPassword, bip44Keypair, destinationDirectory, false);
        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static boolean equalAddress(String a1, String a2) {
        if(!Numeric.cleanHexPrefix(a1).equalsIgnoreCase(Numeric.cleanHexPrefix(a2))) return false;
        return true;
    }
}
