package hu.antalnagy.ejvm.classparser.cpool;

public final class ConstantPoolRef {
    private final int referent;

    public ConstantPoolRef(int referent) {
        this.referent = referent;
    }

    public int getReferent() {
        return referent;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(referent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ConstantPoolRef other = (ConstantPoolRef) obj;
        return this.referent == other.referent;
    }
}
