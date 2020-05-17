package hu.antalnagy.ejvm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Utils {

    public static byte[] yieldBytesNIO(String fName) throws IOException {
        try(final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            File file = new File(fName);
            byte[] buffer = Files.readAllBytes(file.toPath());
            for(byte b: buffer) {
                bos.write(b);
            }
            return bos.toByteArray();
        }
    }

    public static byte[] yieldBytes(String fName, Class<?> relativeClass) throws IOException {
        try (final InputStream is = relativeClass.getResourceAsStream(fName); final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            if(is == null) throw new NullPointerException("InputStream was null, possibly erroneous relative class precised.");
            int bytesRead = is.read(buffer);
            while(bytesRead > 0) {
                bos.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
            return bos.toByteArray();
        }
    }
}
