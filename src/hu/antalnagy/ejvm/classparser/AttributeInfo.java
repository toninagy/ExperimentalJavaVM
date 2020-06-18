package hu.antalnagy.ejvm.classparser;

public class AttributeInfo {
    private final int nameIndex;
    private final int len;
    private final int[] info;

    public AttributeInfo(int nameIndex, int len) {
        this.nameIndex = nameIndex;
        this.len = len;
        this.info = new int[len];
    }

    protected int[] getInfo() {
        return info;
    }
}
