package org.oclc.campbelg.marc8.marc8;

/**
 * Represent a marc8 byte as a byte and as an interpreted representation
 */
public class Marc8Byte {
    private byte marc8Byte;

    public Marc8Byte(byte b) {
        this.marc8Byte = b;
    }

    public byte getMarc8Byte() {
        return marc8Byte;
    }
}
