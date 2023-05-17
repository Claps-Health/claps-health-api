package controllers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SecretKey;
import com.strategyobject.substrateclient.crypto.Seed;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;
import reference.identity.IdentityUtils;
import reference.twitter.TwitterUtils;
import reference.sr25519.type.nByte;
import utils.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ServiceControllerTest {

    @Test
    @DisplayName("test Bip39")
    public void testBip39() throws CipherException, IOException {
        Bip39Wallet bip39= WalletUtils.generateBip39Wallet(null, "1234", new File("."));
        System.out.println("bip39= "+bip39);

        //decrypt credentials with walletPassword and keyStore
        Credentials credBip39= org.web3j.crypto.WalletUtils.loadCredentials("1234", bip39.getFilename());
        System.out.println("credBip39.getAddress()= "+credBip39.getAddress());

        //decrypt credentials with bip39Passphrase and Mnemonic
        Credentials credBip39Mnemonic= org.web3j.crypto.WalletUtils.loadBip39Credentials(null, bip39.getMnemonic());
        System.out.println("credBip39Mnemonic.getAddress()= "+credBip39Mnemonic.getAddress());
    }

    @Test
    @DisplayName("test Bip44")
    public void testBip44() throws CipherException, IOException {   //bip39 + bip44
        Bip39Wallet bip44= WalletUtils.generateBip44Wallet(null, "1234", new File("."), false);
        System.out.println("bip44= "+bip44);

        //decrypt credentials with walletPassword and keyStore
        Credentials credBip44= org.web3j.crypto.Bip44WalletUtils.loadCredentials("1234", bip44.getFilename());
        System.out.println("credBip44.getAddress()= "+credBip44.getAddress());

        //decrypt credentials with bip39Passphrase and Mnemonic
        Credentials credBip44Mnemonic= org.web3j.crypto.Bip44WalletUtils.loadBip44Credentials(null, bip44.getMnemonic(), false);
        System.out.println("credBip44Mnemonic.getAddress()= "+credBip44Mnemonic.getAddress());
    }

    @Test
    @DisplayName("test generate DID from pubkey with sha256")
    public void testGenerateDidWithSha256() {
        byte[] pubkey= Numeric.hexStringToByteArray("0x46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a");
        String did= IdentityUtils.generateDid(pubkey);
        assertNotNull(did);
    }

//    @Test
//    @DisplayName("test key from seed with SR25519 use 'org.polkadot'")
//    public void testKeyFromSeedSR25519() {
//        SR25519 sr = new SR25519();
//
//        byte[] seed = org.web3j.utils.Numeric.hexStringToByteArray("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
//        String expected_keypair = "28b0ae221c6bb06856b287f60d7ea0d98552ea5a16db16956849aa371db3eb51fd190cce74df356432b410bd64682309d6dedb27c76845daf388557cbac3ca3446ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a";
//
//        byte[] kp = new byte[SR25519.SR25519_KEYPAIR_SIZE];
//        sr.sr25519_keypair_from_seed(kp, seed);
//        String actual_keypair = org.web3j.utils.Numeric.toHexString(kp);
//        System.out.println(
//                "testKeyFromSeed:\n"
//                        + "actual_keypair=   " + actual_keypair + "\n"
//                        + "expected_keypair= " + expected_keypair + "\n"
//        );
//    }

//    @Test
//    @DisplayName("test key from seed with ED25519 use 'com.debuggor.schnorrkel'")
//    public void testKeyFromSeedED25519() throws Exception {
//        final String DEFAULT_LABEL = "substrate";
//
////        Bip39Wallet bip39= WalletUtils.generateBip39WalletED25519(null, "1234", new File("."));
////        System.out.println("bip39= "+bip39);
//
////        byte[] initialEntropy = new byte[16];
////        SecureRandom secureRandom = new SecureRandom();
////        secureRandom.nextBytes(initialEntropy);
////        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
//////        System.out.println("mnemonic= "+ mnemonic);
////        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
////        System.out.println("seed= "+ HexUtils.bytesToHex(seed));
//
//        byte[] seed = HexUtils.hexToBytes("0xe5be9a5092b81bca64be81d212e7f2f9eba183bb7a90954f7b76361f6edb5c0a");
//
//        KeyPair keyPair = KeyPair.fromSecretSeed(seed, ExpansionMode.Ed25519);
//        System.out.println("keyPair.getPrivateKey().getKey()= "+ HexUtils.bytesToHex(keyPair.getPrivateKey().getKey()));
//        System.out.println("keyPair.getPublicKey().toPublicKey()= "+ HexUtils.bytesToHex(keyPair.getPublicKey().toPublicKey()));
//
//        byte[] message = "test message".getBytes();
//        SigningContext ctx = SigningContext.createSigningContext(DEFAULT_LABEL.getBytes());
//        SigningTranscript t = ctx.bytes(message);
//        Signature signature = keyPair.sign(t);
//        byte[] sign = signature.to_bytes();
//        System.out.println("sign= "+HexUtils.bytesToHex(sign));
//
//        SigningContext ctx2 = SigningContext.createSigningContext(DEFAULT_LABEL.getBytes());
//        SigningTranscript t2 = ctx2.bytes(message);
//        KeyPair fromPublicKey = KeyPair.fromPublicKey(keyPair.getPublicKey().toPublicKey());
//        boolean verify = fromPublicKey.verify(t2, sign);
//        System.out.println(verify);
//    }

    @Test
    @DisplayName("test key from mnemonic with SR25519")
    public void testKeyFromMnemonicSR25519_2() {
        String mnemonic= "life custom dinosaur water reform ice install drip rice loyal amused argue";

        boolean valid= MnemonicUtils.validateMnemonic(mnemonic);
        System.out.println("valid= "+valid);

        //wrong
        byte[] seedBytes = MnemonicUtils.generateSeed(mnemonic, "");
//        byte[] seedBytes = MnemonicUtils.generateSeed(mnemonic, "Substrate");
        byte[] seed = Arrays.copyOfRange(seedBytes, 0, 32);
        System.out.println("seed= "+HexConverter.toHex(seed));

        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        com.strategyobject.substrateclient.crypto.KeyPair keyPair = sr25599.createPairFromSeed(Seed.fromBytes(seed));
        System.out.println("keyPair.asSecretKey().getBytes()= "+ HexConverter.toHex(keyPair.asSecretKey().getBytes()));
        System.out.println("keyPair.asPublicKey().getBytes()= "+ HexConverter.toHex(keyPair.asPublicKey().getBytes()));

        String did= IdentityUtils.generateDid(keyPair.asPublicKey().getBytes());
        System.out.println("did= "+ did);
        assertNotNull(did);
    }

    @Test
    @DisplayName("test key from seed with SR25519 use 'com.strategyobject.substrateclient.'")
    public void testKeyFromSeedSR25519_2() {
        byte[] seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
        byte[] expected = HexConverter.toBytes("46ebddef8cd9bb167dc30878d7113b7e168e6f0646beffd77d69d39bad76b47a");
        String address_expected= "5DfhGyQdFobKM8NsWvEeAKk5EQQgYe9AydgJ7rMB6E1EqRzV";

        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        com.strategyobject.substrateclient.crypto.KeyPair keyPair = sr25599.createPairFromSeed(Seed.fromBytes(seed));
        System.out.println("keyPair.asSecretKey().getBytes()= "+ HexConverter.toHex(keyPair.asSecretKey().getBytes()));
        System.out.println("keyPair.asPublicKey().getBytes()= "+ HexConverter.toHex(keyPair.asPublicKey().getBytes()));
        assertArrayEquals(expected, keyPair.asPublicKey().getBytes());

        String did= IdentityUtils.generateDid(keyPair.asPublicKey().getBytes());
        System.out.println("did= "+ did);
        assertNotNull(did);

        //The SS58 registry states that:
        //Polkadot has an address type of 00000000b (0 in decimal).
        //Kusama (Polkadot Canary) has an address type of 00000010b (2 in decimal).
        //Generic Substrate has 00101010b as the address type (42 in decimal).

        String address_58 = SS58Codec.encode(keyPair.asPublicKey().getBytes(), SS58AddressFormat.of((short) 42));
        System.out.println("address_58= "+address_58);
        assertEquals(address_expected, address_58);
    }

    @Test
    public void testSignFromSeedSR25519() {
        byte[] message= IdentityUtils.DID_REGISTER_MESSAGE;
        byte[] seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
//        byte[] seed = HexConverter.toBytes("0x4e3259a9d6ded1cafcf4e6b783b05417feddc3f3bde8f01d0886aefb55e07c85");
        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        com.strategyobject.substrateclient.crypto.KeyPair keyPair = sr25599.createPairFromSeed(Seed.fromBytes(seed));
        System.out.println("keyPair.asSecretKey().getBytes()= "+ HexConverter.toHex(keyPair.asSecretKey().getBytes()));
        System.out.println("keyPair.asPublicKey()= "+HexConverter.toHex(keyPair.asPublicKey().getBytes()));
//        String address_actual = SS58Codec.encode(keyPair.asPublicKey().getBytes(), SS58AddressFormat.of((short) 42));
//        System.out.println("address_actual= "+address_actual);

        String did= IdentityUtils.generateDid(keyPair.asPublicKey().getBytes());
        System.out.println("did= "+ did);
        assertNotNull(did);

        SignatureData signData= sr25599.sign(keyPair.asPublicKey(), keyPair.asSecretKey(), new nByte(message));
        System.out.println("signData.getBytes()= "+HexConverter.toHex(signData.getBytes()));

        SignatureData signData2= SignatureData.fromBytes(signData.getBytes());
        assertTrue(sr25599.verify(signData2, new nByte(message), keyPair.asPublicKey()));
    }

    @Test
    public void testSignFromSeedSR25519_tmp() {
        byte[] message= IdentityUtils.DID_REGISTER_MESSAGE;

        PublicKey publicKey= PublicKey.fromBytes(HexConverter.toBytes("06cc4d78f8b24773763e47ab24d908dff6fe1050db98c156d8d16f903340b003"));
        SecretKey privateKey= SecretKey.fromBytes(HexConverter.toBytes("70385d1c7fcb413b7b49bec3654d5977a95196c1020e0f1c63f745a325e35550298d4844f1a11a1f53d601023692d8d3e6a3996d33143e9f4aa5aee2c38e8491"));

        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        SignatureData signData= sr25599.sign(publicKey, privateKey, new nByte(message));
        System.out.println("signData.getBytes()= "+HexConverter.toHex(signData.getBytes()));

        SignatureData signData2= SignatureData.fromBytes(signData.getBytes());
        assertTrue(sr25599.verify(signData2, new nByte(message), publicKey));
    }

    @Test
    public void testSign() {
        byte[] message= "hello".getBytes(StandardCharsets.UTF_8);
        byte[] seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");
        System.out.println("message(hex)= "+HexConverter.toHex(message));

        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        com.strategyobject.substrateclient.crypto.KeyPair keyPair = sr25599.createPairFromSeed(Seed.fromBytes(seed));
        System.out.println("keyPair.asPublicKey()= "+HexConverter.toHex(keyPair.asPublicKey().getBytes()));

        SignatureData signData= sr25599.sign(keyPair.asPublicKey(), keyPair.asSecretKey(), new nByte(message));
        System.out.println("signData.getBytes()= "+HexConverter.toHex(signData.getBytes()));

        SignatureData signData2= SignatureData.fromBytes(signData.getBytes());
//        SignatureData signData2= SignatureData.fromBytes(HexConverter.toBytes("0xe2805566ddc411ccf35fc71a277774454abd5ef29f1bf03332913227dd73f6597908c8205aa76895e4e989daeec76ea200960d00bd6bc8b78685104a73e7d28d"));
        assertTrue(sr25599.verify(signData2, new nByte(message), keyPair.asPublicKey()));
    }

    @Test
    public void testVerifyFromSeedSR25519_tmp() {
//        byte[] message= IdentityUtils.DID_REGISTER_MESSAGE;
        byte[] message= "verify the own of tweet for @ClapHealth #ClapHealth".getBytes(StandardCharsets.UTF_8);
        System.out.println("message(hex)= "+HexConverter.toHex(message));
        PublicKey publicKey= PublicKey.fromBytes(HexConverter.toBytes("06cc4d78f8b24773763e47ab24d908dff6fe1050db98c156d8d16f903340b003"));
        Sr25519NativeCryptoProvider sr25599= new Sr25519NativeCryptoProvider();
        SignatureData signData2= SignatureData.fromBytes(HexConverter.toBytes("32a0177f59094a5b18a341d2a9f1bf58eb91764ff889ba54127d11f4a576e600e700fc9dfb1905ce430de694c0d96e5da7523ab480dec9f4992e3b9e1f886881"));
        assertTrue(sr25599.verify(signData2, new nByte(message), publicKey));
    }

    @Test
    public void testVerifyFromSeedSR25519() {
        byte[] seed = HexConverter.toBytes("fac7959dbfe72f052e5a0c3c8d6530f202b02fd8f9f5ca3580ec8deb7797479e");

        com.strategyobject.substrateclient.crypto.KeyPair keyPair = new Sr25519NativeCryptoProvider().createPairFromSeed(Seed.fromBytes(seed));
        System.out.println("keyPair.asPublicKey()= "+HexConverter.toHex(keyPair.asPublicKey().getBytes()));

        SignatureData signData= TwitterUtils.signTweetVerifyMessage(keyPair.asPublicKey(), keyPair.asSecretKey());
        System.out.println("signData.getBytes()= "+HexConverter.toHex(signData.getBytes()));

        String plain= TwitterUtils.generateTweetPlainForSign(signData.getBytes());
        System.out.println("plain= "+plain);

        //unit verify
//        byte[] sign= TwitterUtils.getTwitterSignFromPlain(plain);
//        assertNotNull(sign);
//        assertTrue(TwitterUtils.verifyTweetSignMessage(sign, keyPair.asPublicKey()));
//        System.out.println("unit verify pass");
//
//        //verify via tweet of twitter
//        String twitter_apikey= "AAAAAAAAAAAAAAAAAAAAAJvxbAEAAAAATQv0PLBw%2BEr1cSrJqdxQDVKztH8%3DGTuzHK3gEXyUyDtKFwymSQkQpXLVaJHFvyl9HTgSaW8Bhp2KRp";
//        String tweet_link= "https://twitter.com/forgetlove0614/status/1655472862319493121";
//        TweetInfo ti= new TwitterApiWrapper(twitter_apikey).createTweetParser(tweet_link).getTweetInfo();
//        assertNotNull(ti);
//        assertTrue(TwitterSignVerifier.isSignerWithSR25519(ti.getData().getText(), keyPair.asPublicKey().getBytes()));
//        System.out.println("verify pass via tweet of twitter");
    }

    @Test
    public void testJwtSR25519() {

    }
}