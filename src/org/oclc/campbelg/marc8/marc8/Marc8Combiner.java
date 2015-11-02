package org.oclc.campbelg.marc8.marc8;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Class for combining Marc8 bytes into a human readable representation.
 * Marc8 bytes must be read sequentially, as they contain numerous escape sequences, of one to four bytes in length.
 */
public class Marc8Combiner {

    /**
     * The output mode is either "ANSI" or "HTML".
     */
    private OutputMode outputMode;

    /**
     * Initialize the combiner for the type of output required.
     *
     * @param outputMode
     */
    public Marc8Combiner(OutputMode outputMode) {

        this.outputMode = outputMode;
    }

    /**
     * Load the marc8 bytes and combine any escapes. As it reads through the bytes, it keeps track of
     * which character modes it is in and adjusts character display as required.
     *
     * @param marc8Bytes
     * @return
     */
    public ArrayList<Marc8Character> combineMarc8Characters(ArrayList<Byte> marc8Bytes) {
        ArrayList<Marc8Character> marc8Characters = new ArrayList<>();

        GraphicCharEscapeMode graphicCharGraphicCharEscapeMode = GraphicCharEscapeMode.NONE;
        CharacterSetEscape characterSetEscapeMode = CharacterSetEscape.NONE;

        for (int i = 0; i < marc8Bytes.size(); i++) {

            // Escape sequences can be up to four bytes long. When gathering the bytes for comparison,
            // be sure not to overrun the end of the file!
            byte b1 = marc8Bytes.get(i);
            byte b2 = i + 1 < marc8Bytes.size() ? marc8Bytes.get(i + 1) : 0;
            byte b3 = i + 2 < marc8Bytes.size() ? marc8Bytes.get(i + 2) : 0;
            byte b4 = i + 3 < marc8Bytes.size() ? marc8Bytes.get(i + 3) : 0;

            // We compare the four bytes against the long list of escape sequences in the Marc8ByteToCharacter
            // enumeration. Escapes are labeled and wrapped in the color and carriage returns defined in the
            // Marc8ByteToCharacter enumeration.
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

            // If we made it this far, we are dealing with a character and not an escape.
            // For now, I just display the marc8 bytes as two digit hex with a space between each.
            // TODO Offer a UTF-8 conversion option so characters are readible on the screen.
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
                                // For now, all the languages fall through to this basic hex byte display method.
                                marc8Characters.add(new Marc8Character(String.format("%2x ", b1)));
                                continue;
                        }
                        break;
                    // Currently, I only handle G0 Normal Set of the ISO/IEC 2022 standard. I haven't run across
                    // any of these odd character set modes yet...
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