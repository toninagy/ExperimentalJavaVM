package hu.antalnagy.ejvm.classparser.cpool;

public class ConstantPoolEntry {
    private final ConstantPoolRef cref1;
    private final ConstantPoolRef cref2;

    private ConstantPoolEntry(ConstantPoolRef cref1, ConstantPoolRef cref2) {
        this.cref1 = cref1;
        this.cref2 = cref2;
    }

    public ConstantPoolEntry of() {
        return new ConstantPoolEntry(null,null);
    }
}
