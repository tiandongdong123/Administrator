package com.utils;

import org.neo4j.cypher.internal.compiler.v2_2.functions.Str;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by yeshusheng on 2018/5/2.
 */
public class Des {

    static Des instance;
    static Key key;
    static Cipher encryptCipher;
    static Cipher decryptCipher;

    protected Des() {
    }

    protected Des(String strKey) {
        key = setKey(strKey);
        try {
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    public static Des getInstance() {
        if (instance == null) {
            instance = new Des("2018");
        }

        return instance;
    }

    //  根据参数生成KEY
    private Key setKey(String strKey) {
        try {
//            KeyGenerator generator = KeyGenerator.getInstance("DES");
//            generator.init(new SecureRandom(strKey.getBytes()));
//            return generator.generateKey();

            KeyGenerator generator = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //  加密String明文输入,String密文输出
    public String setEncString(String strMing) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] byteMing = strMing.getBytes("UTF-8");
            byte[] byteMi = this.getEncCode(byteMing);
            return base64en.encode(byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //加密以byte[]明文输入,byte[]密文输出
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        try {
            byteFina = encryptCipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    //   解密:以String密文输入,String明文输出
    public String setDesString(String strMi) {
        BASE64Decoder base64De = new BASE64Decoder();
        try {
            byte[] byteMi = base64De.decodeBuffer(strMi);
            byte[] byteMing = this.getDesCode(byteMi);
            return new String(byteMing, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密以byte[]密文输入,以byte[]明文输出
    private byte[] getDesCode(byte[] byteD) {
        byte[] byteFina = null;
        try {
            byteFina = decryptCipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    // 加密用户名和密码
    public static String enDes(String username, String password) {
        if (username != null && password != null) {
            String s = username + "*" + password;
            Des des = Des.getInstance();
            s = des.setEncString(s);
            try {
                s = URLEncoder.encode(s, "UTF-8");
                return s;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    // 解密用户名密码
    public static String deDes(String s) {
        if (s != null) {
            try {
                String decode = URLDecoder.decode(s, "UTF-8");
                Des des = Des.getInstance();
                decode = des.setDesString(decode);
                return decode;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static void main(String[] args) {
//        String s1 = Des.enDes("spstsg123", "spstsg123");
//        System.out.println(s1);
//        String s2 = Des.deDes(s1);
//        System.out.println(s2);
        String s3 = Des.enDes("zqstsg2019", "123456");
        System.out.println("http://m.wanfangdata.com.cn/search/jgSearch.html?key=" + s3);
//        String s4 = Des.deDes(s3);
//        System.out.println(s4);
    }
}