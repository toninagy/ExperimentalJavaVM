package hu.antalnagy.ejvm.classparser.cpool;

public class ConstantPoolEntry {
    private final short index;
    private final ConstantPoolType type;
    private final Number num;
    private final String str;
    private final ConstantPoolRef cref1;

    public short getIndex() {
        return index;
    }

    public ConstantPoolType getType() {
        return type;
    }

    public Number getNum() {
        return num;
    }

    public String getStr() {
        return str;
    }

    public ConstantPoolRef getCref1() {
        return cref1;
    }

    public ConstantPoolRef getCref2() {
        return cref2;
    }

    private final ConstantPoolRef cref2;


    private ConstantPoolEntry(short i, ConstantPoolType t, Number n, String s, ConstantPoolRef cref1, ConstantPoolRef cref2) {
        index = i;
        type = t;
        num = n;
        str = s;
        this.cref1 = cref1;
        this.cref2 = cref2;
    }

    public static ConstantPoolEntry of(short i, ConstantPoolType t, Number num) {
        return new ConstantPoolEntry(i, t, num, num.toString(), null, null);
    }

    public static ConstantPoolEntry of(short i, ConstantPoolType t, String s) {
        return new ConstantPoolEntry(i, t, null, s, null, null);
    }

    public static ConstantPoolEntry of(short i, ConstantPoolType t, ConstantPoolRef r) {
        return new ConstantPoolEntry(i, t, null, null, r, null);
    }

    public static ConstantPoolEntry of(short i, ConstantPoolType t, ConstantPoolRef r, ConstantPoolRef r2) {
        return new ConstantPoolEntry(i, t, null, null, r, r2);
    }
}
