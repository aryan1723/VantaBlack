import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

public class OpticEngine {

    // Updated with mouse and hardware entropy parameters
    public byte[] generateHashWithCheck(String filePath, byte[] mouseEntropy, byte[] hardwareEntropy) throws Exception {
        BufferedImage image = ImageIO.read(new File(filePath));
        if (image == null) throw new Exception("Invalid Image");

        Set<Integer> uniqueColors = new HashSet<>();
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        // 1. Visual Entropy (The Image)
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                uniqueColors.add(pixel);
                md.update((byte) ((pixel >> 16) & 0xFF));
                md.update((byte) ((pixel >> 8) & 0xFF));
                md.update((byte) (pixel & 0xFF));
            }
        }

        if (uniqueColors.size() < 1000) {
            throw new Exception("Image too simple! Choose a more detailed photo for the Master Key.");
        }

        // 2. Kinetic Entropy (Mouse Jitter)
        if (mouseEntropy != null && mouseEntropy.length > 0) {
            md.update(mouseEntropy);
        }

        // 3. Hardware Entropy (MAC Address / CPU ID / Voice)
        if (hardwareEntropy != null && hardwareEntropy.length > 0) {
            md.update(hardwareEntropy);
        }

        return md.digest(); // The Forge: Returns the final 512-bit blended key
    }

    // Updated direct generation method
    public byte[] generateHashDirect(String filePath, byte[] mouseEntropy, byte[] hardwareEntropy) throws Exception {
        BufferedImage image = ImageIO.read(new File(filePath));
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        // 1. Visual Entropy
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                md.update((byte) ((pixel >> 16) & 0xFF));
                md.update((byte) ((pixel >> 8) & 0xFF));
                md.update((byte) (pixel & 0xFF));
            }
        }

        // 2. Kinetic Entropy
        if (mouseEntropy != null && mouseEntropy.length > 0) {
            md.update(mouseEntropy);
        }

        // 3. Hardware Entropy
        if (hardwareEntropy != null && hardwareEntropy.length > 0) {
            md.update(hardwareEntropy);
        }

        return md.digest();
    }
}