package org.oclc.campbelg.marc8;

import org.oclc.campbelg.marc8.marc8.Marc8File;
import org.oclc.campbelg.marc8.marc8.OutputMode;
import org.oclc.campbelg.marc8.utilities.MyHelp;

import java.io.IOException;

public class Main {

    private static final int MAX_RECORDS = 10;

    public static void main(String[] args) {
        int maxRecords = MAX_RECORDS;
        if (args.length == 0) {
            MyHelp.display();
        } else {
            if (args.length == 2) {
                maxRecords = Integer.parseInt(args[1]);
            }
            String fileName = args[0];
            try {
                Marc8File marc8File = new Marc8File(fileName, maxRecords, OutputMode.ANSI);
                System.out.println(marc8File.convertRecordsToString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
