package org.oclc.campbelg.marc8.marc8;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Tool for combining Marc8 bytes into a human readable representation
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
        BASIC_GREEK
    }

    public static ArrayList<Marc8Representation> combine(ArrayList<Marc8Byte> marc8Bytes) {
        ArrayList<Marc8Representation> marc8Representations = load(marc8Bytes);
        return marc8Representations;
    }

    /**
     * Load the marc8 bytes and combine any escapes
     *
     * @param marc8Bytes
     * @return
     */
    private static ArrayList<Marc8Representation> load(ArrayList<Marc8Byte> marc8Bytes) {
        ArrayList<Marc8Representation> marc8Representations = new ArrayList<>();

        GraphicCharEscapeMode graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
        CharacterSetEscapeMode characterSetEscapeMode = CharacterSetEscapeMode.NONE;

        for (int i = 0; i < marc8Bytes.size(); i++) {
            byte b1 = marc8Bytes.get(i).getMarc8Byte();
            byte b2 = i + 1 < marc8Bytes.size() ? marc8Bytes.get(i + 1).getMarc8Byte() : 0;
            byte b3 = i + 2 < marc8Bytes.size() ? marc8Bytes.get(i + 2).getMarc8Byte() : 0;
            byte b4 = i + 3 < marc8Bytes.size() ? marc8Bytes.get(i + 3).getMarc8Byte() : 0;

            // Handle Marc21 Escape characters
            if (b1 == 0x1D) {
                marc8Representations.add(new Marc8Representation("\n" + ANSI_RED + "[1D Rec Term]" + ANSI_RESET + "\n", b1));
                continue;
            } else if (b1 == 0x1E) {
                marc8Representations.add(new Marc8Representation(ANSI_GREEN + "[1E Fld Term]" + ANSI_RESET + "\n", b1));
                continue;
            } else if (b1 == 0x1F) {
                marc8Representations.add(new Marc8Representation("\n" + ANSI_YELLOW + "[1F Subfld]" + (char) b2+ ANSI_RESET, b1));
                i++;
                continue;
            } else if (b1 == 0x88) {
                marc8Representations.add(new Marc8Representation("[88 Non-sorting Characters Begin]", b1));
                continue;
            } else if (b1 == 0x89) {
                marc8Representations.add(new Marc8Representation("[89 Non-sorting Characters End]", b1));
                continue;
            } else if (b1 == 0x8D) {
                marc8Representations.add(new Marc8Representation("[8D Joiner]", b1));
                continue;
            } else if (b1 == 0x8E) {
                marc8Representations.add(new Marc8Representation("[8E Non-joiner]", b1));
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
                                if (b1 >= 0x20 && b1 <= 0x7E) {
                                    byte[] bytes = new byte[1];
                                    bytes[0] = b1;
                                    try {
                                        marc8Representations.add(new Marc8Representation(new String(bytes, "iso-8859-1"), b1));
                                    } catch (UnsupportedEncodingException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } else {
                                    marc8Representations.add(new Marc8Representation("", b1));
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
                                marc8Representations.add(new Marc8Representation(String.format("%2x ", b1)));
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

            // Check for raphic character escape clear
            if (b1 == 0x1b) {
                if (b2 == 0x2F && b3 == 0x46) {
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
                    i += 2;
                    marc8Representations.add(new Marc8Representation("[esc/F]"));
                    continue;
                }
            }

            // Check for graphic character escapes
            if (b1 == 0x1b) { // esc
                if (b2 == 0x28 && b3 == 0x24) { // ($
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_NORMAL;
                    marc8Representations.add(new Marc8Representation("\n[ESC]($  g0 set normal\n"));
                    i += 2;
                    continue;
                } else if (b2 == 0x2C && b3 == 0x24 && b4 == 0x2C) { // ,$,
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G0_SET_ADDITIONAL;
                    marc8Representations.add(new Marc8Representation("\n[ESC],$, g0 set additional\n"));
                    i += 3;
                    continue;
                } else if (b2 == 0x29 && b3 == 0x24 && b4 == 0x29) { // )$)
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_NORMAL;
                    marc8Representations.add(new Marc8Representation("\n[ESC])$) g1 set normal\n"));
                    i += 3;
                    continue;
                } else if (b2 == 0x2D && b3 == 0x24 && b4 == 0x2D) { // -$-
                    graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.G1_SET_ADDITIONAL;
                    marc8Representations.add(new Marc8Representation("\n[ESC]-$- g1 set additional\n"));
                    i += 3;
                    continue;
                }
            }

            // Check for character set escapes
            if (b1 == 0x1b) { // esc
                if (b2 == 0x28 && b3 == 0x31) { // (1
                    characterSetEscapeMode = CharacterSetEscapeMode.CJK;
                    //marc8Representations.add(new Marc8Representation(" [ESC](1[Chinese, Japanese & Korean] "));
                    marc8Representations.add(new Marc8Representation(" [esc(1] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x32) { // (2
                    characterSetEscapeMode = CharacterSetEscapeMode.BASIC_HEBREW;
                    //marc8Representations.add(new Marc8Representation(" [ESC](2[Basic Hebrew] "));
                    marc8Representations.add(new Marc8Representation("[esc(2] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x33) { // (3
                    characterSetEscapeMode = CharacterSetEscapeMode.BASIC_ARABIC;
                    //marc8Representations.add(new Marc8Representation(" [ESC](3[Basic Arabic] "));
                    marc8Representations.add(new Marc8Representation("[esc(3] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x34) { // (4
                    characterSetEscapeMode = CharacterSetEscapeMode.EXTENDED_ARABIC;
                    //marc8Representations.add(new Marc8Representation(" [esc(4][Extended Arabic] "));
                    marc8Representations.add(new Marc8Representation("[esc(4] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x42) { // (B
                    characterSetEscapeMode = CharacterSetEscapeMode.BASIC_LATIN;
                    //marc8Representations.add(new Marc8Representation(" [ESC](B[Basic Latin] "));
                    marc8Representations.add(new Marc8Representation("[esc(B] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x21 && b4 == 0x45) { // (!E
                    characterSetEscapeMode = CharacterSetEscapeMode.EXTENDED_LATIN;
                    //marc8Representations.add(new Marc8Representation(" [esc(!E][Extended Latin] "));
                    marc8Representations.add(new Marc8Representation("[esc(!E] "));
                    i += 3;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x4E) { // (N
                    characterSetEscapeMode = CharacterSetEscapeMode.CJK;
                    //marc8Representations.add(new Marc8Representation(" [esc(N][Basic Cyrillic] "));
                    marc8Representations.add(new Marc8Representation("[esc(N] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x31) { // (Q
                    characterSetEscapeMode = CharacterSetEscapeMode.CJK;
                    //marc8Representations.add(new Marc8Representation(" [esc(Q][Extended Cyrillic] "));
                    marc8Representations.add(new Marc8Representation("[esc(Q] "));
                    i += 2;
                    continue;
                } else if (b2 == 0x28 && b3 == 0x31) { // (S
                    characterSetEscapeMode = CharacterSetEscapeMode.CJK;
                    //marc8Representations.add(new Marc8Representation(" [esc(S][Basic Greek] "));
                    marc8Representations.add(new Marc8Representation("[esc(S] "));
                    i += 2;
                    continue;
                }
            }
        }
        return marc8Representations;
    }

}
