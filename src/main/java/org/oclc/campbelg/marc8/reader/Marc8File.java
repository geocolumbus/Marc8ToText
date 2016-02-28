package org.oclc.campbelg.marc8.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Marc8File {

    private String reset;
    private String blue;
    private String newLine;

    private String fileName;
    private int maxRecords;
    private ArrayList<ArrayList<Byte>> records = new ArrayList<>();
    private ArrayList<ArrayList<Marc8Character>> recordsRepresentation = new ArrayList<>();

    /**
     * Read the file and process each record.
     *
     * @param fileName
     * @throws java.io.IOException
     */
    public Marc8File(String fileName, int maxRecords, OutputMode outputMode) throws IOException {
        this.fileName = fileName;
        this.maxRecords = maxRecords;

        switch (outputMode) {
            case ANSI:
                reset = CharacterColor.RESET.getAnsiColor();
                blue = CharacterColor.BLUE.getAnsiColor();
                newLine = "\n";
                break;
            case HTML:
                reset = CharacterColor.RESET.getWebColor();
                blue = CharacterColor.BLUE.getWebColor();
                newLine = "<br>";
                break;
        }
        readMarc8File();
        for (ArrayList<Byte> marc8Bytes : records) {
            recordsRepresentation.add((new Marc8Combiner(outputMode)).combineMarc8Characters(marc8Bytes));
        }
    }

    /**
     * Red the Marc8 file in as bytes
     */
    private void readMarc8File() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        byte[] bytes = new byte[1];
        int recordCounter = 0;
        ArrayList<Byte> record = new ArrayList<>();
        while ((fileInputStream.read(bytes)) != -1) {
            record.add(bytes[0]);
            if (bytes[0] == 0x1D) { // Record Terminator
                records.add(record);
                if (++recordCounter == maxRecords) {
                    break;
                } else {
                    record = new ArrayList<>();
                }
            }
        }
    }

    /**
     * The Marc21 Sections
     */
    private enum Marc21Mode {
        NONE,
        LEADER,
        DIRECTORY
    }

    /**
     * Loop through the records, converting each to a string.
     *
     * @return
     */
    public String convertRecordsToString() {
        String ret = "";
        for (ArrayList<Marc8Character> marc8Characters : recordsRepresentation) {
            ret += convertMarc8CharsToString(marc8Characters);
        }
        return ret;
    }

    /**
     * Display the marc8Characters in human readable format
     *
     * @param marc8Characters
     * @return
     */
    private String convertMarc8CharsToString(ArrayList<Marc8Character> marc8Characters) {
        String ret = "";

        Marc21Mode marc21Mode = Marc21Mode.NONE;

        for (int i = 0; i < marc8Characters.size(); i++) {
            switch (i) {
                case 0:
                    ret += newLine;
                    ret += blue;
                    ret += "RECORD LENGTH: ";
                    ret += reset;
                    marc21Mode = Marc21Mode.LEADER;
                    break;
                case 5:
                    ret += blue;
                    ret += newLine + "RECORD STATUS: ";
                    ret += reset;
                    break;
                case 6:
                    ret += blue;
                    ret += newLine + "TYPE OF RECORD: ";
                    ret += reset;
                    break;
                case 7:
                    ret += blue;
                    ret += newLine + "BIBLIOGRAPHIC LEVEL: ";
                    ret += reset;
                    break;
                case 8:
                    ret += blue;
                    ret += newLine + "TYPE OF CONTROL: ";
                    ret += reset;
                    break;
                case 9:
                    ret += blue;
                    ret += newLine + "CHARACTER CODING SCHEME: ";
                    ret += reset;
                    break;
                case 10:
                    ret += blue;
                    ret += newLine + "INDICATOR COUNT: ";
                    ret += reset;
                    break;
                case 11:
                    ret += blue;
                    ret += newLine + "SUBFIELD CODE COUNT: ";
                    ret += reset;
                    break;
                case 12:
                    ret += blue;
                    ret += newLine + "BASE ADDRESS OF DATE: ";
                    ret += reset;
                    break;
                case 17:
                    ret += blue;
                    ret += newLine + "ENCODING LEVEL: ";
                    ret += reset;
                    break;
                case 18:
                    ret += blue;
                    ret += newLine + "DESCRIPTIVE CATALOGING FORM: ";
                    ret += reset;
                    break;
                case 19:
                    ret += blue;
                    ret += newLine + "MULTIPART RESOURCE RECORD LEVEL: ";
                    ret += reset;
                    break;
                case 20:
                    ret += blue;
                    ret += newLine + "LENGTH OF THE LENGTH-OF-FIELD PORTION: ";
                    ret += reset;
                    break;
                case 21:
                    ret += blue;
                    ret += newLine + "LENGTH OF THE STARTING-CHARACTER-POSITION PORTION: ";
                    ret += reset;
                    break;
                case 22:
                    ret += blue;
                    ret += newLine + "LENGTH OF THE IMPLEMENTATION-DEFINED PORTION: ";
                    ret += reset;
                    break;
                case 23:
                    ret += blue;
                    ret += newLine + "UNDEFINED: ";
                    ret += reset;
                    break;
                case 24:
                    marc21Mode = Marc21Mode.DIRECTORY;
                    break;
            }

            if (marc21Mode == Marc21Mode.DIRECTORY) {
                if (marc8Characters.get(i).getMarc8Byte() == 0x1E) {
                    marc21Mode = Marc21Mode.NONE;
                    ret += marc8Characters.get(i).getMarc8Char();
                    continue;
                } else {
                    ret += newLine;
                    for (int count = 0; count < 3; count++) {
                        ret += marc8Characters.get(i + count).getMarc8Char();
                    }
                    ret += " ";
                    for (int count = 4; count < 7; count++) {
                        ret += marc8Characters.get(i + count).getMarc8Char();
                    }
                    ret += " ";
                    for (int count = 7; count < 12; count++) {
                        ret += marc8Characters.get(i + count).getMarc8Char();
                    }
                    i += 11;
                    continue;
                }
            }
            ret += marc8Characters.get(i).getMarc8Char();
        }
        return ret;
    }
}