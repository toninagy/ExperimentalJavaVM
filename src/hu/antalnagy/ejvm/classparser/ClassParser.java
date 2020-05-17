package hu.antalnagy.ejvm.classparser;

import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolEntry;
import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolType;

public class ClassParser {

    private final byte[] classBytes;
    private final String fileName;

    private final static ConstantPoolType[] constantsTable = new ConstantPoolType[256];
    private ConstantPoolEntry[] entries;

    private int minor;
    private int major;
    private int poolItemCount;
    private int current = 0;

    static {
        for(ConstantPoolType cp: ConstantPoolType.values()) {
            constantsTable[cp.getTagByte()] = cp;
        }
    }

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
        minor = ((int) classBytes[current++] << 8) | (int) classBytes[current++];
        major = ((int) classBytes[current++] << 8) | (int) classBytes[current++];
        poolItemCount = ((int) classBytes[current++] << 8) | (int) classBytes[current++];
    }
    private void parseConstantPool() {
        entries = new ConstantPoolEntry[poolItemCount - 1];
        for(int i = 0; i<poolItemCount; i++) {
            int entry = classBytes[current++] & 0xff; //for transforming int into uint
            ConstantPoolType tag = constantsTable[entry];

            if(tag == null) throw new RuntimeException("Unknown Constant Pool tag byte at position " + current);

            /*To-be continued*/
        }
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
