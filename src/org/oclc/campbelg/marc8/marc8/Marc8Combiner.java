package org.oclc.campbelg.marc8.marc8;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Tool for combining Marc8 bytes into a human readable representation
 */
public class Marc8Combiner {

    //private String reset;
    //private String newLine;
    private OutputMode outputMode;

    /**
     * Initialize the combiner for the type of output required.
     *
     * @param outputMode
     */
    public Marc8Combiner(OutputMode outputMode) {

        this.outputMode = outputMode;
/*
        switch (outputMode) {
            case ANSI:
                newLine = "\n";
                reset = CharacterColor.RESET.getAnsiColor();
                break;
            case HTML:
                newLine = "<br>";
                reset = CharacterColor.RESET.getWebColor();
                break;
        }*/
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

            // Handle byte escapes
            Marc8ByteToCharacter match = Marc8ByteToCharacter.getByteMatch(new Byte[]{b1, b2, b3, b4});
            if (match != Marc8ByteToCharacter.NONE) {

                String newLine = match.isCarriageReturn() ? outputMode == OutputMode.ANSI ? "\n" : "<br>" : "";
                String color = match.getCharacterColor().getColor(outputMode);
                String resetColor = CharacterColor.RESET.getColor(outputMode);

                marc8Characters.add(new Marc8Character(newLine
                        + color
                        + match.getMarc8Character()
                        + resetColor
                        + newLine, b1));
                if (match.getCharacterSetEscape() != null) {
                    characterSetEscapeMode = match.getCharacterSetEscape();
                }
                characterSetEscapeMode = match.getCharacterSetEscape() == null ?
                        characterSetEscapeMode : match.getCharacterSetEscape();
                graphicCharGraphicCharEscapeMode = match.getGraphicCharEscapeMode() == null ?
                        graphicCharGraphicCharEscapeMode : match.getGraphicCharEscapeMode();
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
                                        marc8Characters.add(new Marc8Character(new String(bytes, "iso-8859-1"), b1));
                                    } catch (UnsupportedEncodingException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } else {
                                    marc8Characters.add(new Marc8Character("", b1));
                                }
                                continue;
                            case CJK:
                                //TODO
                            case BASIC_HEBREW:
                                //TODO
                            case BASIC_ARABIC:
                                //TODO
                            case EXTENDED_ARABIC:
                                //TODO
                            case EXTENDED_LATIN:
                                //TODO
                            case BASIC_CYRILLIC:
                                //TODO
                            case EXTENDED_CYRILLIC:
                                //TODO
                            case BASIC_GREEK:
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                                continue;
                        }
                        break;
                    case G0_SET_ADDITIONAL:
                        //TODO
                        break;
                    case G1_SET_NORMAL:
                        //TODO
                        break;
                    case G1_SET_ADDITIONAL:
                        //TODO
                        break;
                }
            }
        }
        return marc8Characters;
    }
}