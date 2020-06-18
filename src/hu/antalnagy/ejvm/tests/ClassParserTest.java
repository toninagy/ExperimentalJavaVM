package hu.antalnagy.ejvm.tests;

import hu.antalnagy.ejvm.classparser.ClassParser;
import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolType;
import hu.antalnagy.ejvm.tests.resources.sampleclasses.HelloWorld;
import hu.antalnagy.ejvm.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ClassParserTest {

    private final Class<?> cls = HelloWorld.class; //relative class to other test classes and itself
    public final Path projectLevelPath = Paths.get("");
    public final String clsRelativeString = "\\out\\production\\ExperimentalJavaVM\\hu\\antalnagy\\ejvm\\tests\\resources\\sampleclasses\\";
    private StringBuilder sb;

    private byte[] bytesRead;

    @BeforeEach
    void setup() {
        String s = projectLevelPath.toAbsolutePath().toString();
        sb = new StringBuilder(s); // won't replace with String, it's cleaner this way
        sb.append(clsRelativeString);
    }

    /**
     * Test for magic number 0xcafebabe, minor, major version and cpool item count
     * @throws IOException If file not found
     */
    @Test
    void TEST_HEADER() throws IOException, ClassNotFoundException {

        sb.append("HelloWorld.class");

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
    void TEST_CPOOL() throws IOException, ClassNotFoundException {
        sb.append("Println.class");

        bytesRead = Utils.yieldBytesNIO(sb.toString());
        ClassParser clsParser = new ClassParser(bytesRead, "Println.class");
        clsParser.parse();

        assertEquals(clsParser.getEntries()[0].getType(), ConstantPoolType.METHODREF, "First CPEntry must be of Method Reference");

        assertTrue(clsParser.getEntries()[0].getCref1().toString().endsWith("2"));
        assertTrue(clsParser.getEntries()[0].getCref2().toString().endsWith("3"));

        assertNotNull(clsParser.getEntries()[1].getCref1());
        assertNull(clsParser.getEntries()[1].getCref2());

        assertEquals(ConstantPoolType.UTF8, clsParser.getEntries()[13].getType());
        assertEquals("Hello World", clsParser.getEntries()[13].getStr());

        assertEquals("this",clsParser.getEntries()[25].getStr());
        assertEquals("Println.java",clsParser.getEntries()[clsParser.getEntries().length-1].getStr(), "Last entry should be the class's name");
    }
}