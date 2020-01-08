package org.duniter.elasticsearch.user.model;

public class DocumentReference {

    public static final String PROPERTY_INDEX="index";
    public static final String PROPERTY_TYPE="type";
    public static final String PROPERTY_ID="id";
    public static final String PROPERTY_ANCHOR="anchor";
    public static final String PROPERTY_HASH="hash";

    private String index;

    private String type;

    private String id;

    private String anchor;

    private String hash;

    public DocumentReference() {
    }

    public DocumentReference(String index, String type, String id) {
        this(index, type, id, null);
    }

    public DocumentReference(String index, String type, String id, String anchor) {
        this.index = index;
        this.type = type;
        this.id = id;
        this.anchor = anchor;
    }

    public DocumentReference(DocumentReference another) {
        this.index = another.getIndex();
        this.type = another.getType();
        this.id = another.getId();
        this.hash = another.getHash();
        this.anchor = another.getAnchor();
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}