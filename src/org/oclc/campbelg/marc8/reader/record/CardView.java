package org.oclc.campbelg.marc8.reader.record;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A structure for holding a card record of a Marc8 record (bytes grouped into fields).
 */
public class CardView {
    private String header;
    private String fieldId;
    private Integer startCount;
    private Integer length;
    private HashMap<String, ArrayList<String>> fieldContents;

    public CardView() {
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getStartCount() {
        return startCount;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public HashMap<String, ArrayList<String>> getFieldContents() {
        return fieldContents;
    }

    public void setFieldContents(HashMap<String, ArrayList<String>> fieldContents) {
        this.fieldContents = fieldContents;
    }
}
