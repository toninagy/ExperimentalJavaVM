package hu.antalnagy.ejvm.tests;

import hu.antalnagy.ejvm.classparser.ClassParser;
import hu.antalnagy.ejvm.tests.resources.sampleclasses.HelloWorld;
import hu.antalnagy.ejvm.utils.Utils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassParserTest {

    private final Class<?> cls = HelloWorld.class; //relative class to other test classes and itself
    public final Path projectLevelPath = Paths.get("");
    public final String clsRelativeString = "\\out\\production\\ExperimentalJavaVM\\hu\\antalnagy\\ejvm\\tests\\resources\\sampleclasses\\HelloWorld.class";

    private byte[] bytesRead;

    @SuppressWarnings("all")
    /**
     * Test for magic number 0xcafebabe, minor, major version and cpool item count
     * @throws IOException
     */
    @Test
    void TEST_HEADER() throws IOException {
        String s = projectLevelPath.toAbsolutePath().toString();

        StringBuilder sb = new StringBuilder(s); // won't replace with String, it's cleaner this way
        sb.append(clsRelativeString);

        bytesRead = Utils.yieldBytesNIO(sb.toString());

        ClassParser clsParser = new ClassParser(bytesRead, "HelloWorld.class");
        clsParser.parse();
        assertEquals(58, clsParser.getMajor(), "Major version should be 58 (Java 14)");
        assertEquals(0, clsParser.getMinor(), "Major version should be 0");
        assertEquals(34, clsParser.getPoolItemCount(), "Pool item count should be 34");

        bytesRead = Utils.yieldBytes("Sum.class", cls);

        clsParser = new ClassParser(bytesRead, "Sum.class");
        clsParser.parse();
        assertEquals(58, clsParser.getMajor(), "Major version should be 58 (Java 14)");
        assertEquals(0, clsParser.getMinor(), "Major version should be 0");
        assertEquals(34, clsParser.getPoolItemCount(), "Pool item count should be 34");
    }

    @Test
    void TEST_CPOOL() throws IOException {
        String s = projectLevelPath.toAbsolutePath().toString();

        StringBuilder sb = new StringBuilder(s); // won't replace with String, it's cleaner this way
        sb.append(clsRelativeString);

        bytesRead = Utils.yieldBytesNIO(sb.toString());
        ClassParser clsParser = new ClassParser(bytesRead, "HelloWorld.class");
        clsParser.parse();
    }
}