package myactvity.mahyco.utils;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class EncryptDecryptManager {

    public static String encryptData(String value, String password){
        String result="";
        try{
            String dataEncrypted = encrypt(password.getBytes("UTF-16LE"), (value).getBytes("UTF-16LE"));
            result = dataEncrypted;
        }
        catch (Exception e){
            Log.d("Exception","MSG:"+e.getMessage());
        }
        return result;
    }

    public static String decryptData(String value, String password){
        String result="";
        try{
            String dataDecrypted = decrypt(password, Base64.decode(value.getBytes("UTF-16LE"), Base64.DEFAULT));
            result = dataDecrypted;
        }
        catch (Exception e){
            Log.d("Exception","MSG:"+e.getMessage());
        }
        return result;
    }

    public static String makeMD5WithRandom(String data){
        final int min = 10000000;
        final int max = 99999999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String result = md5(data + random);
        return result;
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (Exception ex) {
        }
        return "";
    }

    public static String encrypt(byte[] key, byte[] clear) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key);

        SecretKeySpec skeySpec = new SecretKeySpec(digestOfPassword, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

    public static String decrypt(String key, byte[] encrypted) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key.getBytes("UTF-16LE"));

        SecretKeySpec skeySpec = new SecretKeySpec(digestOfPassword, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-16LE");
    }
/*
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            //Cipher _Cipher = Cipher.getInstance("AES");
            //Cipher _Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            //Cipher _Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
             String key = "MAH2019ENCDECKEY";
             byte[] key_Array = Base64.decode(key,
                     new byte[] { 0x49, 0x76, 0x61, 0x6E, 0x20, 0x4D, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 }) ;
            Key SecretKey = new SecretKeySpec(key_Array, "ECB");

            Cipher _Cipher = Cipher.getInstance("ECB");
            _Cipher.init(Cipher.ENCRYPT_MODE, SecretKey);

            return Base64.encodeToString(_Cipher.doFinal(strToEncrypt.getBytes()),Base64.DEFAULT);
        }
        catch (Exception e)
        {
            System.out.println("[Exception]:"+e.getMessage());
        }
        return null;
    }



    public String Encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException   // ', ByVal pass As String
    {

        final String key = "=MAH2019ENCDECKEY";
        final javax.crypto.spec.SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        final javax.crypto.Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte [] encryptedValue = cipher.doFinal(input.getBytes(),Base64.DEFAULT);
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(encryptedValue));

        Cryptography.RijndaelManaged AES = new Cryptography.RijndaelManaged();
        System.Security.Cryptography.MD5CryptoServiceProvider Hash_AES = new System.Security.Cryptography.MD5CryptoServiceProvider();
        String encrypted = "";
        try
        {
            String  EncryptionKey = "MAH2019ENCDECKEY";
            Rfc2898DeriveBytes pdb = new Rfc2898DeriveBytes(EncryptionKey, new byte[] { 0x49, 0x76, 0x61, 0x6E, 0x20, 0x4D, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 });
            AES.Key = pdb.GetBytes(32);
            AES.Mode = System.Security.Cryptography.CipherMode.ECB;
            System.Security.Cryptography.ICryptoTransform DESEncrypter = AES.CreateEncryptor();
            byte[] Buffer = System.Text.ASCIIEncoding.ASCII.GetBytes(input);
            encrypted = Convert.ToBase64String(DESEncrypter.TransformFinalBlock(Buffer, 0, Buffer.Length));

            //////////////////////convert string to hexa///////////

            byte[] ba = Encoding.Default.GetBytes(encrypted);
            String hexString = BitConverter.ToString(ba);

            return hexString;
        }
        catch (Exception ex)
        {
            return input;
        }// If encryption fails, return the unaltered input.
    } */
}
