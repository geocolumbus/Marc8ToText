package org.oclc.campbelg.marc8.marc8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Marc8File {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private String fileName;
    private int maxRecords;
    private ArrayList<ArrayList<Byte>> records = new ArrayList<>();
    private ArrayList<ArrayList<Marc8Byte>> recordsBytes = new ArrayList<>();
    private ArrayList<ArrayList<Marc8Representation>> recordsRepresentation = new ArrayList<>();

    /**
     * Read the file and process each record.
     *
     * @param fileName
     * @throws IOException
     */
    public Marc8File(String fileName, int maxRecords) throws IOException {
        this.fileName = fileName;
        this.maxRecords = maxRecords;
        readMarc8File();
        loadMarc8Bytes();
        for (ArrayList<Marc8Byte> marc8Bytes : recordsBytes) {
            recordsRepresentation.add(Marc8Combiner.combine(marc8Bytes));
        }
    }

    /**
     * Determine if the file exists
     *
     * @return
     */
    public boolean exists() {
        return (new File(fileName)).exists();
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
     * Store the Marc8Bytes in a structure
     */
    private void loadMarc8Bytes() {
        for (int i = 0; i < records.size(); i++) {
            ArrayList<Marc8Byte> marc8Bytes = new ArrayList<>();
            for (byte b : records.get(i)) {
                marc8Bytes.add(new Marc8Byte(b));
            }
            recordsBytes.add(marc8Bytes);
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
     * Return the records in a human readable format
     *
     * @return
     */
    public String marc8RecordsToString() {
        String ret = "";
        for (ArrayList<Marc8Representation> marc8Representations : recordsRepresentation) {
            ret += marc8RepresentationsToString(marc8Representations);
        }
        return ret;
    }

    /**
     * Display the marc8Representations in human readable format
     *
     * @param marc8Representations
     * @return
     */
    private String marc8RepresentationsToString(ArrayList<Marc8Representation> marc8Representations) {
        String ret = "";

        Marc21Mode marc21Mode = Marc21Mode.NONE;

        for (int i = 0; i < marc8Representations.size(); i++) {
            switch (i) {
                case 0:
                    ret += "\n";
                    ret += ANSI_BLUE;
                    ret += "RECORD LENGTH: ";
                    ret += ANSI_RESET;
                    marc21Mode = Marc21Mode.LEADER;
                    break;
                case 5:
                    ret += ANSI_BLUE;
                    ret += "\nRECORD STATUS: ";
                    ret += ANSI_RESET;
                    break;
                case 6:
                    ret += ANSI_BLUE;
                    ret += "\nTYPE OF RECORD: ";
                    ret += ANSI_RESET;
                    break;
                case 7:
                    ret += ANSI_BLUE;
                    ret += "\nBIBLIOGRAPHIC LEVEL: ";
                    ret += ANSI_RESET;
                    break;
                case 8:
                    ret += ANSI_BLUE;
                    ret += "\nTYPE OF CONTROL: ";
                    ret += ANSI_RESET;
                    break;
                case 9:
                    ret += ANSI_BLUE;
                    ret += "\nCHARACTER CODING SCHEME: ";
                    ret += ANSI_RESET;
                    break;
                case 10:
                    ret += ANSI_BLUE;
                    ret += "\nINDICATOR COUNT: ";
                    ret += ANSI_RESET;
                    break;
                case 11:
                    ret += ANSI_BLUE;
                    ret += "\nSUBFIELD CODE COUNT: ";
                    ret += ANSI_RESET;
                    break;
                case 12:
                    ret += ANSI_BLUE;
                    ret += "\nBASE ADDRESS OF DATE: ";
                    ret += ANSI_RESET;
                    break;
                case 17:
                    ret += ANSI_BLUE;
                    ret += "\nENCODING LEVEL: ";
                    ret += ANSI_RESET;
                    break;
                case 18:
                    ret += ANSI_BLUE;
                    ret += "\nDESCRIPTIVE CATALOGING FORM: ";
                    ret += ANSI_RESET;
                    break;
                case 19:
                    ret += ANSI_BLUE;
                    ret += "\nMULTIPART RESOURCE RECORD LEVEL: ";
                    ret += ANSI_RESET;
                    break;
                case 20:
                    ret += ANSI_BLUE;
                    ret += "\nLENGTH OF THE LENGTH-OF-FIELD PORTION: ";
                    ret += ANSI_RESET;
                    break;
                case 21:
                    ret += ANSI_BLUE;
                    ret += "\nLENGTH OF THE STARTING-CHARACTER-POSITION PORTION: ";
                    ret += ANSI_RESET;
                    break;
                case 22:
                    ret += ANSI_BLUE;
                    ret += "\nLENGTH OF THE IMPLEMENTATION-DEFINED PORTION: ";
                    ret += ANSI_RESET;
                    break;
                case 23:
                    ret += ANSI_BLUE;
                    ret += "\nUNDEFINED: ";
                    ret += ANSI_RESET;
                    break;
                case 24:
                    marc21Mode = Marc21Mode.DIRECTORY;
                    break;
            }

            if (marc21Mode == Marc21Mode.DIRECTORY) {
                if (marc8Representations.get(i).getMarc8Byte() == 0x1E) {
                    marc21Mode = Marc21Mode.NONE;
                    ret += marc8Representations.get(i).getRepresentation();
                    continue;
                } else {
                    ret += "\n";
                    for (int count = 0; count < 3; count++) {
                        ret += marc8Representations.get(i + count).getRepresentation();
                    }
                    ret += " ";
                    for (int count = 4; count < 7; count++) {
                        ret += marc8Representations.get(i + count).getRepresentation();
                    }
                    ret += " ";
                    for (int count = 7; count < 12; count++) {
                        ret += marc8Representations.get(i + count).getRepresentation();
                    }
                    i += 11;
                    continue;
                }
            }
            ret += marc8Representations.get(i).getRepresentation();
        }
        return ret;
    }
}
