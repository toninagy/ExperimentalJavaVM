package hu.antalnagy.ejvm.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static byte[] yieldBytes(String fName, Class<?> relativeClass) throws IOException {
        try (final InputStream is = relativeClass.getResourceAsStream(fName); final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            if(is == null) throw new NullPointerException("InputStream was null, possibly bad class file precised.");
            int bytesRead = is.read(buffer);
            while(bytesRead > 0) {
                bos.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
            return bos.toByteArray();
        }
    }
}
