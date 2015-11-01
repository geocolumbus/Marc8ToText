package org.oclc.campbelg.marc8.marc8;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Tool for combining Marc8 bytes into a human readable representation
 */
public class Marc8Combiner {

    private String green;
    private String yellow;
    private String reset;
    private String newLine;

    /**
     * Initialize the combiner for the type of output required.
     *
     * @param outputMode
     */
    public Marc8Combiner(OutputMode outputMode) {

        switch (outputMode) {
            case ANSI:
                newLine = "\n";
                green = CharacterColor.GREEN.getAnsiColor();
                yellow = CharacterColor.YELLOW.getAnsiColor();
                reset = CharacterColor.RESET.getAnsiColor();
                break;
            case HTML:
                newLine = "<br>";
                green = CharacterColor.GREEN.getWebColor();
                yellow = CharacterColor.YELLOW.getWebColor();
                reset = CharacterColor.RESET.getWebColor();
                break;
        }
    }

    /**
     * Enumerations for the graphic character mode
     */
    private enum GraphicCharEscapeMode {
        NONE,
        G0_SET_NORMAL,
        G0_SET_ADDITIONAL,
        G1_SET_NORMAL,
        G1_SET_ADDITIONAL
    }

    public ArrayList<Marc8Character> combine(ArrayList<Byte> marc8Bytes) {
        ArrayList<Marc8Character> marc8Characters = load(marc8Bytes);
        return marc8Characters;
    }

    /**
     * Load the marc8 bytes and combine any escapes
     *
     * @param marc8Bytes
     * @return
     */
    private ArrayList<Marc8Character> load(ArrayList<Byte> marc8Bytes) {
        ArrayList<Marc8Character> marc8Characters = new ArrayList<>();

        GraphicCharEscapeMode graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
        CharacterSetEscape characterSetEscapeMode = CharacterSetEscape.NONE;

        for (int i = 0; i < marc8Bytes.size(); i++) {
            byte b1 = marc8Bytes.get(i);
            byte b2 = i + 1 < marc8Bytes.size() ? marc8Bytes.get(i + 1) : 0;
            byte b3 = i + 2 < marc8Bytes.size() ? marc8Bytes.get(i + 2) : 0;
            byte b4 = i + 3 < marc8Bytes.size() ? marc8Bytes.get(i + 3) : 0;

            // Handle single byte escapes
            if (Marc8ByteToCharacter.getOneByteMatch(b1) != Marc8ByteToCharacter.NONE) {
                marc8Characters.add(new Marc8Character(newLine + green
                        + Marc8ByteToCharacter.getOneByteMatch(b1).getMarc8Character()
                        + reset + newLine, b1));
                continue;
            }

            // Check for graphic character escape clear
            if (b1 == 0x1b) {
                if (b2 == 0x2F && b3 == 0x46) {
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
                    i += 2;
                    marc8Characters.add(new Marc8Character("[esc/F]"));
                    continue;
                }
            }

            // Check for graphic character escapes
            if (b1 == 0x1b) { // esc
                if (b2 == 0x28 && b3 == 0x24) { // ($
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_NORMAL;
                    marc8Characters.add(new Marc8Character(newLine + green + "[ESC]($  g0 set normal" + reset + newLine));
                    i += 2;
                    continue;
                } else if (b2 == 0x2C && b3 == 0x24 && b4 == 0x2C) { // ,$,
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_ADDITIONAL;
                    marc8Characters.add(new Marc8Character(newLine + green + "[ESC],$, g0 set additional" + reset + newLine));
                    i += 3;
                    continue;
                } else if (b2 == 0x29 && b3 == 0x24 && b4 == 0x29) { // )$)
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_NORMAL;
                    marc8Characters.add(new Marc8Character(newLine + green + "[ESC])$) g1 set normal" + reset + newLine));
                    i += 3;
                    continue;
                } else if (b2 == 0x2D && b3 == 0x24 && b4 == 0x2D) { // -$-
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_ADDITIONAL;
                    marc8Characters.add(new Marc8Character(newLine + green + "[ESC]-$- g1 set additional" + reset + newLine));
                    i += 3;
                    continue;
                }
            }

            // Check for character set escapes
            if (b1 == 0x1b) { // esc
                if (b2 == 0x28 && b3 == 0x31) { // (1
                    characterSetEscapeMode = CharacterSetEscape.CJK;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(1 CJK]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x32) { // (2
                    characterSetEscapeMode = CharacterSetEscape.BASIC_HEBREW;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(2 BASIC HEBREW]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x33) { // (3
                    characterSetEscapeMode = CharacterSetEscape.BASIC_ARABIC;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(3 BASIC ARABIC]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x34) { // (4
                    characterSetEscapeMode = CharacterSetEscape.EXTENDED_ARABIC;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(4 EXTENDED ARABIC]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x42) { // (B
                    characterSetEscapeMode = CharacterSetEscape.BASIC_LATIN;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(B BASIC LATIN]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x21 && b4 == 0x45) { // (!E
                    characterSetEscapeMode = CharacterSetEscape.EXTENDED_LATIN;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(!E EXTENDED LATIN]" + reset));
                    i += 3;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x4E) { // (N
                    characterSetEscapeMode = CharacterSetEscape.BASIC_CYRILLIC;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(N BASIC CYRILLIC]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x51) { // (Q
                    characterSetEscapeMode = CharacterSetEscape.EXTENDED_CYRILLIC;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(N EXTENDED CYRILLIC]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x53) { // (S
                    characterSetEscapeMode = CharacterSetEscape.BASIC_GREEK;
                    marc8Characters.add(new Marc8Character(yellow + "[esc(N BASIC GREEK]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x62) { // b
                    characterSetEscapeMode = CharacterSetEscape.SUBSCRIPT;
                    marc8Characters.add(new Marc8Character(yellow + "[escb SUBSCRIPT]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x67) { // g
                    characterSetEscapeMode = CharacterSetEscape.GREEK_SYMBOL;
                    marc8Characters.add(new Marc8Character(yellow + "[escg GREEK SYMBOL]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x70) { // p
                    characterSetEscapeMode = CharacterSetEscape.SUPERSCRIPT;
                    marc8Characters.add(new Marc8Character(yellow + "[escp SUPERSCRIPT]" + reset));
                    i += 2;
                    continue;
                } else if (b2 == 0x73) { // s
                    characterSetEscapeMode = CharacterSetEscape.BASIC_LATIN;
                    marc8Characters.add(new Marc8Character(yellow + "[ESCs BASIC LATIN]" + reset));
                    i += 2;
                    continue;
                }
            }

            // Handle characters
            if (b1 != 0x1b) {
                switch (graphicCharGraphicCharEscapeMode) {
                    case NONE:
                    case G0_SET_NORMAL:
                        switch (characterSetEscapeMode) {
                            case NONE:
                            case BASIC_LATIN:
                                if (b1 >= 0x20 && b1 <= 0x7E) {
                                    byte[] bytes = new byte[1];
                                    bytes[0] = b1;
                                    try {
                                        marc8Characters.add(new Marc8Character(new String(bytes, "iso-8859-1"), b1));
                                    } catch (UnsupportedEncodingException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } else {
                                    marc8Characters.add(new Marc8Character("", b1));
                                }
                                continue;
                            case CJK:
                            case BASIC_HEBREW:
                            case BASIC_ARABIC:
                            case EXTENDED_ARABIC:
                            case EXTENDED_LATIN:
                            case BASIC_CYRILLIC:
                            case EXTENDED_CYRILLIC:
                            case BASIC_GREEK:
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                                continue;
                        }
                        break;
                    case G0_SET_ADDITIONAL:
                        break;
                    case G1_SET_NORMAL:
                        break;
                    case G1_SET_ADDITIONAL:
                        break;
                }
            }
        }
        return marc8Characters;
    }
}