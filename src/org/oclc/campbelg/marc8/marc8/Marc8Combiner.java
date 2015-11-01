package org.oclc.campbelg.marc8.marc8;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Read serial marc8 bytes and combine them into escape sequences, byte groupings and and NCR's.
 */
public class Marc8Combiner {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

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

    /**
     * Enumerations for the character set mode
     */
    private enum CharacterSetEscapeMode {
        NONE,
        CJK,
        BASIC_HEBREW,
        BASIC_ARABIC,
        EXTENDED_ARABIC,
        BASIC_LATIN,
        EXTENDED_LATIN,
        BASIC_CYRILLIC,
        EXTENDED_CYRILLIC,
        BASIC_GREEK,
        SUBSCRIPT,
        SUPERSCRIPT,
        GREEK_SYMBOL
    }

    /**
     * Load the marc8 bytes and combine any escapes.
     *
     * @param marc8Bytes
     * @return
     */
    public static ArrayList<Marc8Character> combine(ArrayList<Byte> marc8Bytes) {
        ArrayList<Marc8Character> marc8Characters = new ArrayList<>();

        GraphicCharEscapeMode graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
        CharacterSetEscapeMode characterSetEscapeMode = CharacterSetEscapeMode.NONE;

        for (int i = 0; i < marc8Bytes.size(); i++) {
            byte b1 = marc8Bytes.get(i);
            byte b2 = i + 1 < marc8Bytes.size() ? marc8Bytes.get(i + 1) : 0;
            byte b3 = i + 2 < marc8Bytes.size() ? marc8Bytes.get(i + 2) : 0;
            byte b4 = i + 3 < marc8Bytes.size() ? marc8Bytes.get(i + 3) : 0;

            if (getMarc21EscapeCharacter(b1) != null) {
                marc8Characters.add(getMarc21EscapeCharacter(b1));
                continue;
            } else if (getGraphicCharacterEscapeClear(b1, b2, b3) != null) {
                marc8Characters.add(getGraphicCharacterEscapeClear(b1, b2, b3));
                graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x24) { // esc($
                graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_NORMAL;
                marc8Characters.add(new Marc8Character("\n[ESC]($  g0 set normal\n"));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x2C && b3 == 0x24 && b4 == 0x2C) { // esc,$,
                graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_ADDITIONAL;
                marc8Characters.add(new Marc8Character("\n[ESC],$, g0 set additional\n"));
                i += 3;
                continue;
            } else if (b1 == 0x1b && b2 == 0x29 && b3 == 0x24 && b4 == 0x29) { // esc)$)
                graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_NORMAL;
                marc8Characters.add(new Marc8Character("\n[ESC])$) g1 set normal\n"));
                i += 3;
                continue;
            } else if (b1 == 0x1b && b2 == 0x2D && b3 == 0x24 && b4 == 0x2D) { // esc-$-
                graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_ADDITIONAL;
                marc8Characters.add(new Marc8Character("\n[ESC]-$- g1 set additional\n"));
                i += 3;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x31) { // esc(1
                characterSetEscapeMode = CharacterSetEscapeMode.CJK;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(1 CJK]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x32) { // esc(2
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_HEBREW;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(2 BASIC HEBREW]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x33) { // (3
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_ARABIC;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(3 BASIC ARABIC]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b2 == 0x28 && b3 == 0x34) { // esc(4
                characterSetEscapeMode = CharacterSetEscapeMode.EXTENDED_ARABIC;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(4 EXTENDED ARABIC]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x42) { // esc(B
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_LATIN;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(B BASIC LATIN]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x21 && b4 == 0x45) { // esc(!E
                characterSetEscapeMode = CharacterSetEscapeMode.EXTENDED_LATIN;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(!E EXTENDED LATIN]" + ANSI_RESET));
                i += 3;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x4E) { // esc(N
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_CYRILLIC;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(N BASIC CYRILLIC]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x51) { // esc(Q
                characterSetEscapeMode = CharacterSetEscapeMode.EXTENDED_CYRILLIC;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(Q EXTENDED CYRILLIC]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x28 && b3 == 0x53) { // esc(S
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_GREEK;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[esc(S BASIC GREEK]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x62) { // escb
                characterSetEscapeMode = CharacterSetEscapeMode.SUBSCRIPT;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[escb SUBSCRIPT]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x67) { // escg
                characterSetEscapeMode = CharacterSetEscapeMode.GREEK_SYMBOL;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[escg GREEK SYMBOL]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x70) { // escp
                characterSetEscapeMode = CharacterSetEscapeMode.SUPERSCRIPT;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[escp SUPERSCRIPT]" + ANSI_RESET));
                i += 2;
                continue;
            } else if (b1 == 0x1b && b2 == 0x73) { // escs
                characterSetEscapeMode = CharacterSetEscapeMode.BASIC_LATIN;
                marc8Characters.add(new Marc8Character(ANSI_YELLOW + "[ESCs BASIC LATIN]" + ANSI_RESET));
                i += 2;
                continue;
            }

            // Handle characters
            if (b1 != 0x1b) {
                switch (graphicCharGraphicCharEscapeMode) {
                    case NONE:
                    case G0_SET_NORMAL:
                        switch (characterSetEscapeMode) {
                            case NONE:
                            case BASIC_LATIN:
                                marc8Characters.add(getBasicLatinChar(b1));
                                continue;
                            case CJK:
                                // TODO - group CJK, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case BASIC_HEBREW:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case BASIC_ARABIC:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case EXTENDED_ARABIC:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case EXTENDED_LATIN:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case BASIC_CYRILLIC:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case EXTENDED_CYRILLIC:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                            case BASIC_GREEK:
                                // TODO - handle diacritics & NCRs, for now just display bytes
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                                continue;
                        }
                        break;
                    case G0_SET_ADDITIONAL:
                        // TODO
                        break;
                    case G1_SET_NORMAL:
                        // TODO
                        break;
                    case G1_SET_ADDITIONAL:
                        // TODO
                        break;
                }
            }
        }
        return marc8Characters;
    }

    /**
     * Determines if a group of bytes represents an graphic set escape group.
     *
     * @param b1
     * @param b2
     * @param b3
     * @return
     */

    private static boolean isGraphicCharacterEscapeClear(Byte b1, Byte b2, Byte b3) {
        return b1 == 0x1b && b2 == 0x2F && b3 == 0x46;
    }

    /**
     * Return a Marc21 Escape character, or null if it isn't.
     *
     * @param b
     * @return
     */
    private static Marc8Character getMarc21EscapeCharacter(Byte b) {
        Marc8Character marc8Character;
        if (b == 0x1D) {
            marc8Character = new Marc8Character("\n" + ANSI_GREEN + "[1D Rec Term]" + ANSI_RESET + "\n");
        } else if (b == 0x1E) {
            marc8Character = new Marc8Character(ANSI_GREEN + "[1E Fld Term]" + ANSI_RESET + "\n");
        } else if (b == 0x1F) {
            marc8Character = new Marc8Character("\n" + ANSI_GREEN + "[1F Subfld]" + ANSI_RESET);
        } else if (b == 0x88) {
            marc8Character = new Marc8Character("[88 Non-sorting Characters Begin]");
        } else if (b == 0x89) {
            marc8Character = new Marc8Character("[89 Non-sorting Characters End]");
        } else if (b == 0x8D) {
            marc8Character = new Marc8Character("[8D Joiner]");
        } else if (b == 0x8E) {
            marc8Character = new Marc8Character("[8E Non-joiner]");
        } else {
            marc8Character = null;
        }
        return marc8Character;
    }

    /**
     * Return a Graphic Escape Clear character, or null if it isn't.
     *
     * @param b1
     * @param b2
     * @param b3
     * @return
     */
    private static Marc8Character getGraphicCharacterEscapeClear(Byte b1, Byte b2, Byte b3) {
        Marc8Character marc8Character;
        if (b1 == 0x1b && b2 == 0x2F && b3 == 0x46) {
            marc8Character = new Marc8Character("[esc/F]");
        } else {
            marc8Character = null;
        }
        return marc8Character;

    }

    /**
     * Return a latin character
     *
     * @param b
     * @return
     */
    private static Marc8Character getBasicLatinChar(Byte b) {
        Marc8Character marc8Character = new Marc8Character("");
        if (b >= 0x20 && b <= 0x7E) {
            byte[] bytes = new byte[1];
            bytes[0] = b;
            try {
                marc8Character = new Marc8Character(new String(bytes, "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            }
        }
        return marc8Character;
    }
}
