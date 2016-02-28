package org.oclc.campbelg.marc8.reader;

/**
 * Representation of a Marc8 Character.
 * May be a latin byte, grouped CJK triple, escape sequence or raw Marc8 byte.
 */
public class Marc8Character {

    private String marc8Char;
    private Byte marc8Byte;

    public Marc8Character(String marc8Char) {
        this.marc8Char = marc8Char;
        this.marc8Byte = 0x0;
    }

    public Marc8Character(String representation, Byte marc8Byte) {
        this.marc8Char = representation;
        this.marc8Byte = marc8Byte;
    }

    public String getMarc8Char() {
        return marc8Char;
    }

    public Byte getMarc8Byte() {
        return marc8Byte;
    }
}
