import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ForgeKey {
    public void forge(String drive,byte[] masterKey){

        if(drive == null || drive.isEmpty()){
            System.out.println("No valid Drive detected");
            return;
        }

        try{
            File keyFile = new File(drive + ".vanta");
            try(FileOutputStream fileOutputStream = new FileOutputStream(keyFile)){
                fileOutputStream.write(masterKey);
            }
            Files.setAttribute(Paths.get(keyFile.getAbsolutePath()),"dos:hidden", true);
            System.out.println("Vantablack Key forged and hidden at: " + keyFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Critical Error during Forge: " + e.getMessage());
        }
    }
}
