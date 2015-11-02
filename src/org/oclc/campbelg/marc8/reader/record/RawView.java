package org.oclc.campbelg.marc8.reader.record;

import java.util.ArrayList;

/**
 * The raw record shows they bytes in order, instead of grouped into fields.
 */
public class RawView {
    private ArrayList<String> rawRecord;

    public RawView() {
    }

    public ArrayList<String> getRawRecord() {
        return rawRecord;
    }

    public void setRawRecord(ArrayList<String> rawRecord) {
        this.rawRecord = rawRecord;
    }
}
