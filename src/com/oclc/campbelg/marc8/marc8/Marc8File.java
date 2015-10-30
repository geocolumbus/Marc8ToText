package com.oclc.campbelg.marc8.marc8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Marc8File {

    String fileName;
    byte[] file;
    ArrayList<Marc8Byte> marc8Bytes = new ArrayList<>();
    ArrayList<Marc8Representation> marc8Representations = new ArrayList<>();

    public Marc8File(String fileName) throws IOException {
        this.fileName = fileName;
        readMarc8File();
        loadMarc8Bytes();
        marc8Representations = Marc8Combiner.combine(marc8Bytes);
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
        Path path = Paths.get(fileName);
        file = Files.readAllBytes(path);
    }

    /**
     * Store the Marc8Bytes in a structure
     */
    private void loadMarc8Bytes() {
        for (byte b : file) {
            marc8Bytes.add(new Marc8Byte(b));
        }
    }

    private enum Marc21Mode {NONE, LEADER, DIRECTORY}

    ;

    /**
     * Display the marc8Representations in human readable format
     *
     * @return
     */
    public String marc8RepresentationsToString() {
        String ret = "";

        Marc21Mode marc21Mode = Marc21Mode.NONE;

        for (int i = 0; i < marc8Representations.size(); i++) {
            switch (i) {
                case 0:
                    ret += "== L E A D E R   B E G I N ==============================================================\n";
                    ret += "RECORD LENGTH: ";
                    marc21Mode = Marc21Mode.LEADER;
                    break;
                case 5:
                    ret += "\nRECORD STATUS: ";
                    break;
                case 6:
                    ret += "\nTYPE OF RECORD: ";
                    break;
                case 7:
                    ret += "\nBIBLIOGRAPHIC LEVEL: ";
                    break;
                case 8:
                    ret += "\nTYPE OF CONTROL: ";
                    break;
                case 9:
                    ret += "\nCHARACTER CODING SCHEME: ";
                    break;
                case 10:
                    ret += "\nINDICATOR COUNT: ";
                    break;
                case 11:
                    ret += "\nSUBFIELD CODE COUNT: ";
                    break;
                case 12:
                    ret += "\nBASE ADDRESS OF DATE: ";
                    break;
                case 17:
                    ret += "\nENCODING LEVEL: ";
                    break;
                case 18:
                    ret += "\nDESCRIPTIVE CATALOGING FORM: ";
                    break;
                case 19:
                    ret += "\nMULTIPART RESOURCE RECORD LEVEL: ";
                    break;
                case 20:
                    ret += "\nLENGTH OF THE LENGTH-OF-FIELD PORTION: ";
                    break;
                case 21:
                    ret += "\nLENGTH OF THE STARTING-CHARACTER-POSITION PORTION: ";
                    break;
                case 22:
                    ret += "\nLENGTH OF THE IMPLEMENTATION-DEFINED PORTION: ";
                    break;
                case 23:
                    ret += "\nUNDEFINED: ";
                    break;
                case 24:
                    ret += "\n== L E A D E R   E N D ==================================================================\n";
                    ret += "== D I R E C T O R Y   B E G I N ========================================================";
                    marc21Mode = Marc21Mode.DIRECTORY;
                    break;
            }

            if (marc21Mode == Marc21Mode.DIRECTORY) {
                if (marc8Representations.get(i).getMarc8Byte() == 0x1E) {
                    marc21Mode = Marc21Mode.NONE;
                    ret += marc8Representations.get(i).getRepresentation();
                    ret += "== D I R E C T O R Y   E N D ============================================================\n";
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

    public ArrayList<Marc8Byte> getMarc8Bytes() {
        return marc8Bytes;
    }

    public ArrayList<Marc8Representation> getMarc8Representations() {
        return marc8Representations;
    }
}
