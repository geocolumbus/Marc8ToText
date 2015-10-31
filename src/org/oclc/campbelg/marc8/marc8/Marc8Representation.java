package org.oclc.campbelg.marc8.marc8;

/**
 * Representation of a Marc8 Character
 */
public class Marc8Representation {

    private String representation;
    private Byte marc8Byte;

    public Marc8Representation(String representation) {
        this.representation = representation;
        this.marc8Byte = 0x0;
    }

    public Marc8Representation(String representation, Byte marc8Byte) {
        this.representation = representation;
        this.marc8Byte = marc8Byte;
    }

    public String getRepresentation() {
        return representation;
    }

    public Byte getMarc8Byte() {
        return marc8Byte;
    }
}
