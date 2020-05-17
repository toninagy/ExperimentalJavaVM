package hu.antalnagy.ejvm.classparser;

public class ClassParser {

    private final byte[] classBytes;
    private final String fileName;

    private int minor;
    private int major;
    private int poolItemCount;
    private int current = 0;

    public ClassParser(byte[] buffer, String fileName) {
        this.classBytes = buffer;
        this.fileName = fileName;
    }

    public int getMinor() {
        return minor;
    }

    public int getMajor() {
        return major;
    }

    public int getPoolItemCount() {
        return poolItemCount;
    }

    public void parse() {
        parseHeader();
        parseConstantPool();
        parseBasicTypeInfo();
        parseFields();
        parseMethods();
        parseAttributes();
    }

    private void parseHeader() {
        if ((classBytes[current++] != (byte) 0xca) || (classBytes[current++] != (byte) 0xfe)
                || (classBytes[current++] != (byte) 0xba) || (classBytes[current++] != (byte) 0xbe)) {
            throw new IllegalArgumentException("Input file does not match magic number 0xcafebabe!");
        }
        minor = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        major = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        poolItemCount = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
    }
    private void parseConstantPool() {
        /*TO-DO*/
    }
    private void parseBasicTypeInfo() {
        /*TO-DO*/
    }
    private void parseFields() {
        /*TO-DO*/
    }
    private void parseMethods() {
        /*TO-DO*/
    }
    private void parseAttributes() {
        /*TO-DO*/
    }
}
