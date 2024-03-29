package hu.antalnagy.ejvm.classparser;

import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolEntry;
import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolRef;
import hu.antalnagy.ejvm.classparser.cpool.ConstantPoolType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClassParser {

    private final byte[] classBytes;
    private final String fileName;

    private final static ConstantPoolType[] constantsTable = new ConstantPoolType[256];
    private ConstantPoolEntry[] entries;

    private int minor;
    private int major;
    private int poolItemCount;
    private int current = 0;

    private int accessFlags = 0;
    private int thisReference = 0;
    private int superReference = 0;

    private int iCount = 0;
    private List<ConstantPoolRef> interfaces = new ArrayList<>();

    private int fCount = 0;
    private List<FieldInfo> fields = new ArrayList<>();

    private int mCount = 0;
    private List<MethodInfo> methods = new ArrayList<>();

    private int aCount = 0;
    private List<AttributeInfo> attributes = new ArrayList<>();

    public int getAccessFlags() {
        return accessFlags;
    }

    public int getThisReference() {
        return thisReference;
    }

    public int getSuperReference() {
        return superReference;
    }

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

    public void parse() throws ClassNotFoundException {
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
    private void parseConstantPool() throws ClassNotFoundException {
        entries = new ConstantPoolEntry[poolItemCount-1];
        for (short i = 1; i < poolItemCount; i++) {
//            int entry = classBytes[current++] & 0xff;
//            var entry = Byte.toUnsignedInt(classBytes[current++]);
            var entry = classBytes[current++];
            ConstantPoolType tag = constantsTable[entry];
            if (tag == null) {
                throw new ClassNotFoundException("Unrecognised tag byte: " + entry + " encountered at position " + current + ". Stopping the parse.");
            }

            ConstantPoolEntry item;
            switch (tag) {
                case UTF8: // String prefixed by a uint16 indicating the number of bytes in the encoded string which immediately follows
                    int len = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    String str = new String(classBytes, current, len, StandardCharsets.UTF_8);
                    item = ConstantPoolEntry.of(i, tag, str);
                    current += len;
                    break;
                case INTEGER: // Integer: a signed 32-bit two's complement number in big-endian format
                    int i2 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    item = ConstantPoolEntry.of(i, tag, i2);
                    break;
                case FLOAT: // Float: a 32-bit single-precision IEEE 754 floating-point number
                    int i3 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    float f = Float.intBitsToFloat(i3);
                    item = ConstantPoolEntry.of(i, tag, f);
                    break;
                case LONG: // Long: a signed 64-bit two's complement number in big-endian format (takes two slots in the constant pool table)
                    int i4 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    int i5 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    long l = ((long) i4 << 32) + (long) i5;
                    item = ConstantPoolEntry.of(i, tag, l);
                    break;
                case DOUBLE: // Double: a 64-bit double-precision IEEE 754 floating-point number (takes two slots in the constant pool table)
                    i4 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    i5 = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    l = ((long) i4 << 32) + (long) i5;
                    item = ConstantPoolEntry.of(i, tag, Double.longBitsToDouble(l));
                    break;
                case CLASS: // Class reference: an uint16 within the constant pool to a UTF-8 string containing the fully qualified class name
                case STRING: // String reference: an uint16 within the constant pool to a UTF-8 string
                    int ref = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    item = ConstantPoolEntry.of(i, tag, new ConstantPoolRef(ref));
                    break;
                case FIELDREF: // Field reference: two uint16 within the pool, 1st pointing to a Class reference, 2nd to a Name and Type descriptor
                case METHODREF: // Method reference: two uint16s within the pool, 1st pointing to a Class reference, 2nd to a Name and Type descriptor
                case INTERFACE_METHODREF: // Interface method reference: 2 uint16 within the pool, 1st pointing to a Class reference, 2nd to a Name and Type descriptor
                    int classIndex = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    int nameAndTypeIndex = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    item = ConstantPoolEntry.of(i, tag, new ConstantPoolRef(classIndex), new ConstantPoolRef(nameAndTypeIndex));
                    break;
                case NAMEANDTYPE: // Name and type descriptor: 2 uint16 to UTF-8 strings, 1st representing a name (identifier), 2nd a specially encoded type descriptor
                    int nameRef = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    int typeRef = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
                    item = ConstantPoolEntry.of(i, tag, new ConstantPoolRef(nameRef), new ConstantPoolRef(typeRef));
                    break;
                default:
                    throw new ClassNotFoundException("Reached impossible Constant Pool Tag.");
            }
            entries[i-1] = item;
        }
    }
    private void parseBasicTypeInfo() {
        accessFlags = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        thisReference = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        superReference = ((int) classBytes[current++] << 8) + (int) classBytes[current++];

        iCount = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        for(short i=0;i<iCount;i++) {
            interfaces.add(new ConstantPoolRef(((int) classBytes[current++] << 8) + (int) classBytes[current++]));
        }
    }
    private void parseFields() {
        fCount = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        for(short i=0;i<fCount;i++) {
            fields.add(new FieldInfo(
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++]
            ));
            var attrCount = fields.get(i).getAttributesCount();
            for(int j=0;j<attrCount;j++) {
                fields.get(i).getaInfos()[j] = parseAttribute();
            }
        }

    }
    private void parseMethods() {
        mCount = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        for(short i=0;i<mCount;i++) {
            methods.add(new MethodInfo(
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++],
                    ((int) classBytes[current++] << 8) + (int) classBytes[current++]
            ));
            var attrCount = methods.get(i).getAttributesCount();
            for(int j=0;j<attrCount;j++) {
                methods.get(i).getaInfos()[j] = parseAttribute();
            }
        }
    }
    //helper
    private AttributeInfo parseAttribute() {
        var attrNameIndex = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        var len = ((int) classBytes[current++] << 24) + ((int) classBytes[current++] << 16) + ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        AttributeInfo a = new AttributeInfo(attrNameIndex, len);
        for(int i=0;i<len;i++) {
            a.getInfo()[i] = classBytes[current++];
        }
        return a;
    }

    private void parseAttributes() {
        aCount = ((int) classBytes[current++] << 8) + (int) classBytes[current++];
        for(short i=0;i<aCount; i++) {
            attributes.add(parseAttribute());
        }
    }

    public ConstantPoolEntry[] getEntries() {
        return entries;
    }
}
