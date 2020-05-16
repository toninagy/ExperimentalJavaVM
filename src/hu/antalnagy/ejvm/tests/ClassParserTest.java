package hu.antalnagy.ejvm.tests;

import hu.antalnagy.ejvm.classparser.ClassParser;
import hu.antalnagy.ejvm.tests.resources.sampleclasses.HelloWorld;
import hu.antalnagy.ejvm.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ClassParserTest {

    private Class<?> cls;
    private ClassParser clsParser;

    @BeforeEach
    void setUp() {
        cls = HelloWorld.class;
    }

    @Test
    void TEST_FOR_0xCAFEBABE() throws IOException {
        byte[] bytesRead = Utils.yieldBytes("HelloWorld.class", cls);
        byte[] bytesRead2 = Utils.yieldBytes("Sum.class", cls);

        clsParser = new ClassParser(bytesRead, "HelloWorld.class");
        clsParser = new ClassParser(bytesRead2, "Sum.class");
    }
}