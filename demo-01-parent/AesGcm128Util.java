import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.badPaddingException;
import javax.crypto.*;
import javax.crypto.spec.*;

public class AesGcm128Util {
    private static final String ALGORITHM = "AES";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String KEY_GCM_AES = "AES/GCM/PKCS5Padding";
    private static final int AES_KEY_SIZE = 128;
    
    public static String createIV(int length) {
        SecureRandom random = null;
        
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[length];
            random.nextBytes(salt);
            return encryptBASE64(salt);
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return null;
        }
    }
    
    public static String initKey() {
        try {
            return initKey((String)null);
        } catch (Exception var1) {
            var1.printStackTrace();
            return null;
        }
    }
    
    // jiami
    public static String encryptByInitKeyAndIV(String data, String rootKey, String iv) {
        Cipher cipher = null;
        
        try {
            cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            byte[] ivStr = decryptBASE64(iv);
            Key keySpec = toKey(decryptBASE64(rootKey));
            cipher.init(1, keySpec, new GCMParameterSpec(128, ivStr));
            byte[] content = data.getBytes("UTF-8");
            byte[] result = cipher.doFinal(content);
            
            return encryptBASE64(result);
        } catch () {
            // TODO: exception
        }
        
        return null;
    }
    
    // jiemi
    public static String decryptByInitKeyAndIV(String data, String key, String iv) {
        Cipher cipher = null;
        
        try {
            cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            byte[] ivStr = decryptBASE64(iv);
            Key keySpec = toKey(decryptBASE64(key));
            cipher.init(2, keySpec, new GCMParameterSpec(128, ivStr));
            byte[] content = decryptBASE64(data);
            byte[] result = cipher.doFinal(content);
            return new String(result, "UTF-8");
        } catch () {
            // TODO: exception
        }
        
        return null;
    }
    
    
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return new byte[0];
        } else {
            byte[] result = new byte[hexStr.length() /2];
            
            for (int index = 0; index < hexStr.length() / 2; ++index) {
                int high = Integer.parseInt(hexStr.substring(index * 2, index * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(index * 2 + 1, index * 2 + 2), 16);
                
                retult[index] = (byte)(high * 16 + low);
            }
            
            return result;
        }
    }
        
    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    private static byte[] decryptBASE64(String data) {
        return Base64.getDecoder().decode(data.replace("\r\n", ""));
    }

    private static String encryptBASE64(byte[] data) {
        return Base64.getencoder().encodeToString(data);
    }

    private static String initKey(String seed) throws Exception {
        SecureRandom secureRandom = null;
        if (seed != null) {
            secureRandom = new SecureRandom(decryptBASE64(seed));
        } else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, secureRandom);
        SecretKey secretKey = kg.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }
    
    // use
    public void test() {
        String iv = AesGcm128Util.createIV(16);
        String rootKey = AesGcm128Util.initKey();
        
        String encryptData01 = AesGcm128Util.encryptByInitKeyAndIV(sourceData01, rootKey, iv);
        String encryptData02 = AesGcm128Util.encryptByInitKeyAndIV(sourceData02, rootKey, iv);
        
        String decryptData01 = AesGcm128Util.decryptByInitKeyAndIV(encryptData01, rootKey, iv);
        String decryptData02 = AesGcm128Util.decryptByInitKeyAndIV(encryptData02, rootKey, iv);
        
        Assert.assertEquals(sourceData01, decryptData01);
        Assert.assertEquals(sourceData02, decryptData0);
    }
};
