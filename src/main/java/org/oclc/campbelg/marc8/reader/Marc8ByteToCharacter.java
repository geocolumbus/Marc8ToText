package org.oclc.campbelg.marc8.reader;

import java.util.Arrays;

/**
 * Convert bytes to Marc8 escape sequences
 * 1. bytes
 * 2. text representation of the escape displayed to the console.
 * 3. the color (if any) to display the escape text in.
 * 4. the escape mode (ie, switch to Cyrillic).
 * 5. the G0, G1 (normal, additional) ISO/IEC 2022 character mode.
 * 6. whether the escape symbol should be wrapped in newlines or not.
 */
public enum Marc8ByteToCharacter {
    RECORD_TERMINATOR(new Byte[]{0x1d}, "[1D Rec Term]", CharacterColor.GREEN, null, null, true),
    FIELD_TERMINATOR(new Byte[]{0x1e}, "[1E Fld Term]", CharacterColor.GREEN, null, null, true),
    SUB_FIELD(new Byte[]{0x1f}, "[1F Subfld]", CharacterColor.GREEN, null, null, true),
    NON_SORTING_CHARACTERS_BEGIN(new Byte[]{(byte) 0x88}, "[88 Non-sorting Characters Begin]", CharacterColor.GREEN, null, null, true),
    NON_SORTING_CHARACTERS_END(new Byte[]{(byte) 0x89}, "[89 Non-sorting Characters Begin]", CharacterColor.GREEN, null, null, true),
    JOINER(new Byte[]{(byte) 0x89}, "[8D Joiner]", CharacterColor.GREEN, null, null, true),
    NON_JOINER(new Byte[]{(byte) 0x89}, "[8E Non-joiner]", CharacterColor.GREEN, null, null, true),

    G0_SET_NORMAL(new Byte[]{0x1b, 0x28, 0x24}, "[ESC]($ g0 set normal", CharacterColor.RED, null, GraphicCharEscapeMode.G0_SET_NORMAL, true),
    G0_SET_ADDITIONAL(new Byte[]{0x1b, 0x2c, 0x24, 0x2c}, "[ESC],$, g0 set additional", CharacterColor.RED, null, GraphicCharEscapeMode.G0_SET_ADDITIONAL, true),
    G1_SET_NORMAL(new Byte[]{0x1b, 0x29, 0x24, 0x29}, "[ESC])$) g1 set normal", CharacterColor.RED, null, GraphicCharEscapeMode.G1_SET_NORMAL, true),
    G1_SET_ADDITIONAL(new Byte[]{0x1b, 0x2d, 0x24, 0x2d}, "[ESC]-$- g1 set additional", CharacterColor.RED, null, GraphicCharEscapeMode.G1_SET_ADDITIONAL, true),
    GRAPHIC_ESCAPE_CLEAR(new Byte[]{0x1b, 0x2f, 0x46, 0}, "[ESC]/F", CharacterColor.RED, null, GraphicCharEscapeMode.NONE, true),

    BASIC_CYRILLIC(new Byte[]{0x1b, 0x28, 0x4e}, "[ESC(N BASIC CYRILLIC]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_CYRILLIC, null, false),
    CJK_LOC(new Byte[]{0x1b, 0x28, 0x31}, "[ESC(1 CJK]", CharacterColor.YELLOW, CharacterSetEscape.CJK, null, false),
    CJK(new Byte[]{0x1b, 0x24, 0x31}, "[ESC$1 CJK]", CharacterColor.YELLOW, CharacterSetEscape.CJK, null, false),
    BASIC_HEBREW(new Byte[]{0x1b, 0x28, 0x32}, "[ESC(2 BASIC HEBREW]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_HEBREW, null, false),
    BASIC_ARABIC(new Byte[]{0x1b, 0x28, 0x33}, "[ESC(3 BASIC ARABIC]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_ARABIC, null, false),
    EXTENDED_ARABIC(new Byte[]{0x1b, 0x28, 0x34}, "[ESC(4 EXTENDED ARABIC]\"", CharacterColor.YELLOW, CharacterSetEscape.EXTENDED_ARABIC, null, false),
    BASIC_LATIN_3BYTE(new Byte[]{0x1b, 0x28, 0x42}, "[ESC(B BASIC LATIN]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_LATIN, null, false),
    EXTENDED_LATIN(new Byte[]{0x1b, 0x21, 0x45}, "[ESC(!E EXTENDED LATIN]", CharacterColor.YELLOW, CharacterSetEscape.EXTENDED_LATIN, null, false),
    EXTENDED_CYRILLIC(new Byte[]{0x1b, 0x28, 0x51}, "[ESC(N EXTENDED CYRILLIC]", CharacterColor.YELLOW, CharacterSetEscape.EXTENDED_CYRILLIC, null, false),
    BASIC_GREEK(new Byte[]{0x1b, 0x28, 0x53}, "[ESC(N BASIC GREEK]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_GREEK, null, false),

    SUBSCRIPT(new Byte[]{0x1b, 0x62}, "[ESCb SUBSCRIPT]", CharacterColor.YELLOW, CharacterSetEscape.SUBSCRIPT, null, false),
    GREEK_SYMBOL(new Byte[]{0x1b, 0x67}, "[ESCg GREEK SYMBOL]", CharacterColor.YELLOW, CharacterSetEscape.GREEK_SYMBOL, null, false),
    SUPERSCRIPT(new Byte[]{0x1b, 0x70}, "[ESCp SUPERSCRIPT]", CharacterColor.YELLOW, CharacterSetEscape.SUPERSCRIPT, null, false),
    BASIC_LATIN_2BYTE(new Byte[]{0x1b, 0x73}, "[ESCs BASIC LATIN]", CharacterColor.YELLOW, CharacterSetEscape.BASIC_LATIN, null, false),

    NONE(new Byte[]{(byte) 0}, "", null, CharacterSetEscape.NONE, null, false);

    private Byte[] marc8Bytes;
    private String marc8Character;
    private CharacterColor characterColor;
    private CharacterSetEscape characterSetEscape;
    private GraphicCharEscapeMode graphicCharEscapeMode;
    private boolean carriageReturn;

    Marc8ByteToCharacter(Byte[] marc8Bytes,
                         String marc8Character,
                         CharacterColor characterColor,
                         CharacterSetEscape characterSetEscape,
                         GraphicCharEscapeMode graphicCharEscapeMode,
                         boolean carriageReturn) {
        this.marc8Bytes = marc8Bytes;
        this.marc8Character = marc8Character;
        this.characterColor = characterColor;
        this.characterSetEscape = characterSetEscape;
        this.graphicCharEscapeMode = graphicCharEscapeMode;
        this.carriageReturn = carriageReturn;
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

    public GraphicCharEscapeMode getGraphicCharEscapeMode() {
        return graphicCharEscapeMode;
    }

    public CharacterColor getCharacterColor() {
        return characterColor;
    }

    public boolean isCarriageReturn() {
        return carriageReturn;
    }

    // Try to match bytes to the above enumerations.
    // This routing can match a byte sequence of 1 to 4 bytes.
    public static Marc8ByteToCharacter getByteMatch(Byte[] fourBytes) {
        // Loop through the enumerations.
        for (Marc8ByteToCharacter marc8ByteToCharacter : Marc8ByteToCharacter.values()) {
            // Get the number of bytes from the enumeration, pare down the 4 submitted bytes to the same
            // number and compare them.
            Byte[] compareBytes = Arrays.copyOf(fourBytes, marc8ByteToCharacter.getMarc8Bytes().length);
            if (Arrays.equals(marc8ByteToCharacter.getMarc8Bytes(), compareBytes)) {
                return marc8ByteToCharacter;
            }
        }
        return Marc8ByteToCharacter.NONE;
    }
}
