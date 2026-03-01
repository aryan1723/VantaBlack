import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class VantaBlack {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        File root = new File("VantaBlack");
        if (!root.exists()) root.mkdir();

        File vault = new File(root, "VantaVault");
        File encDir = new File(root, "EncryptedFiles");

        if (!vault.exists()) vault.mkdir();
        if (!encDir.exists()) encDir.mkdir();

        showMsg("VANTABLACK SYSTEM ONLINE");

        try {
            while (true) {
                ExternalDriveDetection detector = new ExternalDriveDetection();
                String usb = detector.detectUSB();
                String status = usb.isEmpty() ? "USB DISCONNECTED" : "TOKEN DETECTED: " + usb;

                String[] options = {"Forge Key", "Encrypt Vault", "Decrypt & Restore", "Exit"};
                int choice = JOptionPane.showOptionDialog(null,
                        status + "\nSelect Operation:",
                        "Vantablack Control Terminal",
                        0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 3 || choice == -1) break;

                switch (choice) {
                    case 0: handleForge(usb); break;
                    case 1: handleEncrypt(vault, encDir, usb); break;
                    case 2: handleDecryptAndRestore(encDir, vault, usb); break;
                }
            }
        } catch (Exception e) {
            showMsg("Critical Error: " + e.getMessage());
        }
    }

    private static void handleEncrypt(File vault, File encDir, String usb) throws Exception {
        if (usb.isEmpty()) {
            showMsg("Error: Please insert the Physical USB Key to encrypt.");
            return;
        }

        KeyExtractor extractor = new KeyExtractor();
        byte[] key = extractor.readKey(usb);

        File[] files = vault.listFiles();
        if (files != null && files.length > 0) {
            VaultProcessor vp = new VaultProcessor();
            for (File f : files) {
                if (f.isFile()) {
                    vp.encrypt(f, new File(encDir, f.getName() + ".vanta"), key);
                }
            }
            handleSecureWipe(vault, "Encryption successful. Wipe originals from Vault?");
        } else {
            showMsg("VantaVault is empty!");
        }
        Arrays.fill(key, (byte)0);
    }

    private static void handleDecryptAndRestore(File encDir, File vault, String usb) throws Exception {
        if (usb.isEmpty()) {
            showMsg("Access Denied: Physical USB Token missing.");
            return;
        }

        KeyExtractor extractor = new KeyExtractor();
        byte[] key = extractor.readKey(usb);

        File[] encFiles = encDir.listFiles();
        if (encFiles != null && encFiles.length > 0) {
            VaultProcessor vp = new VaultProcessor();
            for (File f : encFiles) {
                if (f.getName().endsWith(".vanta")) {
                    // Restore back to the VantaVault (removing .vanta extension)
                    File restoredFile = new File(vault, f.getName().replace(".vanta", ""));
                    vp.decrypt(f, restoredFile, key);

                    // AUTOMATICALLY REMOVE ENCRYPTED FILE AFTER SUCCESSFUL DECRYPTION
                    if (restoredFile.exists()) {
                        f.delete();
                    }
                }
            }
            showMsg("Restoration Complete. Files moved back to 'VantaVault'.");
        } else {
            showMsg("No encrypted files found to restore.");
        }
        Arrays.fill(key, (byte)0);
    }

    private static void handleForge(String usb) throws Exception {
        String path = getPath("Select Image for Master Key", JFileChooser.FILES_ONLY);
        if (path == null) return;

        OpticEngine engine = new OpticEngine();
        byte[] masterKey = engine.generateHashWithCheck(path);

        if (usb.isEmpty()) {
            usb = getPath("Select USB Root manually", JFileChooser.DIRECTORIES_ONLY);
        }

        if (usb != null) {
            new ForgeKey().forge(usb, masterKey);
            showMsg("Physical Key successfully forged.");
        }
        Arrays.fill(masterKey, (byte)0);
    }

    private static String getPath(String title, int mode) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(title);
        jfc.setFileSelectionMode(mode);
        return (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) ? jfc.getSelectedFile().getAbsolutePath() : null;
    }

    private static void showMsg(String text) {
        JOptionPane.showMessageDialog(null, text, "Vantablack", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void handleSecureWipe(File folder, String msg) {
        int res = JOptionPane.showConfirmDialog(null, msg, "Security", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            for (File f : folder.listFiles()) if (f.isFile()) f.delete();
        }
    }
}