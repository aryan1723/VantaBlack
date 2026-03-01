import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class KeyExtractor {
    public byte[] readKey(String drivePath) throws Exception{
        File keyFile = new File(drivePath + ".vanta");
        if(!keyFile.exists()){
            throw new FileNotFoundException("Master key not found");
        }
        if (!keyFile.canRead()) {
            throw new IOException("Access Denied: Cannot read the key file.");
        }
        return Files.readAllBytes(keyFile.toPath());
    }
}
