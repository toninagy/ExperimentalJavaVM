package hu.antalnagy.ejvm.classparser.cpool;

public enum ConstantPoolType {

    UTF8(1),
    INTEGER(3),
    FLOAT(4),
    LONG(5),
    DOUBLE(6),
    CLASS(7),
    STRING(8),
    FIELDREF(9) {
        @Override
        public String separator() {
            return ".";
        }
    },
    METHODREF(10) {
        @Override
        public String separator() {
            return ".";
        }
    },
    INTERFACE_METHODREF(11),
    NAMEANDTYPE(12) {
        @Override
        public String separator() {
            return ":";
        }
    },
    METHODHANDLE(15),
    METHODTYPE(16),
    DYNAMIC(17),
    INVOKEDYNAMIC(18),
    MODULE(19),
    PACKAGE(20);

    private final int tagByte;

    public int getTagByte() {
        return tagByte;
    }

    public byte B() {
        return (byte) tagByte;
    }

    ConstantPoolType(final int b) {
        tagByte = b;
    }

    public String separator() {
        return "";
    }

}
