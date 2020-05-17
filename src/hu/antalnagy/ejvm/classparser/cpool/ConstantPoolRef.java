package hu.antalnagy.ejvm.classparser.cpool;

public final class ConstantPoolRef {
    private final int other;

    public ConstantPoolRef(int other) {
        this.other = other;
    }

    public int getOther() {
        return other;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ConstantPoolRef other = (ConstantPoolRef) obj;
        return this.other == other.other;
    }
}
