import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class VaultProcessor {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public void encrypt(File inputFile, File outputFile, byte[] masterKey) throws Exception{

        byte[] keyBytes = Arrays.copyOfRange(masterKey, 0, 32);
        byte[] ivBytes = Arrays.copyOfRange(masterKey, 32, 48)  ;

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[8192]; // 8KB chunks
            int count;
            while ((count = fis.read(buffer)) > 0) {
                cos.write(buffer, 0, count);
            }
        }finally {
            Arrays.fill(keyBytes, (byte) 0);
            Arrays.fill(ivBytes, (byte) 0);
        }
    }

    public void decrypt(File inputFile, File outputFile, byte[] masterKey) throws Exception {

        byte[] keyBytes = Arrays.copyOfRange(masterKey, 0, 32);
        byte[] ivBytes = Arrays.copyOfRange(masterKey, 32, 48);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes,"AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivParameterSpec);

        try(FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            CipherOutputStream cos = new CipherOutputStream(fos,cipher);
        ){
            byte[] buffer = new byte[8192];
            int count;
            while ((count = fis.read(buffer))>0){
                cos.write(buffer, 0, count);
            }
        }finally {
            Arrays.fill(keyBytes, (byte) 0);
            Arrays.fill(ivBytes, (byte) 0);
        }
    }
}
