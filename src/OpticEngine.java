import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

public class OpticEngine {
    // USE THIS FOR FORGING ONLY (Checks complexity)
    public byte[] generateHashWithCheck(String filePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(filePath));
        if (image == null) throw new Exception("Invalid Image");

        Set<Integer> uniqueColors = new HashSet<>();
        MessageDigest md = MessageDigest.getInstance("SHA-512");

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
        return md.digest();
    }

    public byte[] generateHashDirect(String filePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(filePath));
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                md.update((byte) ((pixel >> 16) & 0xFF));
                md.update((byte) ((pixel >> 8) & 0xFF));
                md.update((byte) (pixel & 0xFF));
            }
        }
        return md.digest();
    }
}