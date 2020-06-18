package hu.antalnagy.ejvm.classparser;

public class FieldInfo {
    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final int attributesCount;
    private final AttributeInfo[] aInfos;


    public FieldInfo(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
        aInfos = new AttributeInfo[attributesCount];
    }

    public int getAttributesCount() {
        return attributesCount;
    }

    public AttributeInfo[] getaInfos() {
        return aInfos;
    }
}
