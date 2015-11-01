package org.oclc.campbelg.marc8.marc8;

import java.util.Arrays;

/**
 * Convert bytes to Marc8 escape sequences
 */
public enum Marc8ByteToCharacter {
    RECORD_TERMINATOR(new Byte[]{0x1d}, "[1D Rec Term]", CharacterSetEscape.NONE),
    FIELD_TERMINATOR(new Byte[]{0x1e}, "[1E Fld Term]", CharacterSetEscape.NONE),
    SUB_FIELD(new Byte[]{0x1f}, "[1F Subfld]", CharacterSetEscape.NONE),
    NON_SORTING_CHARACTERS_BEGIN(new Byte[]{(byte) 0x88}, "[88 Non-sorting Characters Begin]", CharacterSetEscape.NONE),
    NON_SORTING_CHARACTERS_END(new Byte[]{(byte) 0x89}, "[89 Non-sorting Characters Begin]", CharacterSetEscape.NONE),
    JOINER(new Byte[]{(byte) 0x89}, "[8D Joiner]", CharacterSetEscape.NONE),
    NON_JOINER(new Byte[]{(byte) 0x89}, "[8E Non-joiner]", CharacterSetEscape.NONE),
    NONE(new Byte[]{(byte) 0x0}, "", CharacterSetEscape.NONE);

    private Byte[] marc8Bytes;
    private String marc8Character;
    private CharacterSetEscape characterSetEscape;

    Marc8ByteToCharacter(Byte[] marc8Bytes, String marc8Character, CharacterSetEscape characterSetEscape) {
        this.marc8Bytes = marc8Bytes;
        this.marc8Character = marc8Character;
        this.characterSetEscape = characterSetEscape;
    }

    public Byte[] getMarc8Bytes() {
        return marc8Bytes;
    }

    public String getMarc8Character() {
        return marc8Character;
    }

    public CharacterSetEscape getCharacterSetEscape() {
        return characterSetEscape;
    }

    public static Marc8ByteToCharacter getOneByteMatch(Byte marc8Byte) {
        for (Marc8ByteToCharacter marc8ByteToCharacter : Marc8ByteToCharacter.values()) {
            if (marc8ByteToCharacter.getMarc8Bytes().length == 1 && marc8ByteToCharacter.getMarc8Bytes()[0].equals(marc8Byte)) {
                return marc8ByteToCharacter;
            }
        }
        return Marc8ByteToCharacter.NONE;
    }

    public static Marc8ByteToCharacter getTwoByteMatch(Byte[] marc8Bytes) {
        for (Marc8ByteToCharacter marc8ByteToCharacter : Marc8ByteToCharacter.values()) {
            if (marc8ByteToCharacter.getMarc8Bytes().length == 2 &&
                    Arrays.equals(marc8ByteToCharacter.getMarc8Bytes(), marc8Bytes)) {
                return marc8ByteToCharacter;
            }
        }
        return Marc8ByteToCharacter.NONE;
    }

    public static Marc8ByteToCharacter getThreeByteMatch(Byte[] marc8Bytes) {
        for (Marc8ByteToCharacter marc8ByteToCharacter : Marc8ByteToCharacter.values()) {
            if (marc8ByteToCharacter.getMarc8Bytes().length == 3 &&
                    Arrays.equals(marc8ByteToCharacter.getMarc8Bytes(), marc8Bytes)) {
                return marc8ByteToCharacter;
            }
        }
        return Marc8ByteToCharacter.NONE;
    }

    public static Marc8ByteToCharacter getFourByteMatch(Byte[] marc8Bytes) {
        for (Marc8ByteToCharacter marc8ByteToCharacter : Marc8ByteToCharacter.values()) {
            if (marc8ByteToCharacter.getMarc8Bytes().length == 4 &&
                    Arrays.equals(marc8ByteToCharacter.getMarc8Bytes(), marc8Bytes)) {
                return marc8ByteToCharacter;
            }
        }
        return Marc8ByteToCharacter.NONE;
    }

}
