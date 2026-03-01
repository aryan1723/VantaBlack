import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ExternalDriveDetection {
    public String detectUSB(){
        String drive = "";
        File[] roots = File.listRoots();
        for(File root:roots){
            String type = FileSystemView.getFileSystemView().getSystemTypeDescription(root);
            if(type!=null && type.contains("USB") || type.contains("Removable")){
                String drivePath = root.getAbsolutePath();

                File keyFile = new File(drivePath + ".vanta");

                if(keyFile.exists()){
                    System.out.println("Vantablack Key Detected on: " + drivePath);
                    return drivePath;
                }
            }
        }
        return "";
    }
}
