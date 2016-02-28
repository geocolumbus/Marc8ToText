package org.oclc.campbelg.marc8.reader.record;

import org.oclc.campbelg.marc8.reader.Marc8Character;

import java.util.ArrayList;

/**
 * List of the raw bytes for each record
 */
public class RawBytes {
    private ArrayList<Marc8Character> marc8Characters;

    public RawBytes() {
    }

    public RawBytes(ArrayList<Marc8Character> marc8Characters) {
        this.marc8Characters = marc8Characters;
    }

    public ArrayList<Marc8Character> getMarc8Characters() {
        return marc8Characters;
    }

    public void setMarc8Characters(ArrayList<Marc8Character> marc8Characters) {
        this.marc8Characters = marc8Characters;
    }
}
