package org.oclc.campbelg.marc8;

import org.oclc.campbelg.marc8.reader.Marc8File;
import org.oclc.campbelg.marc8.reader.OutputMode;
import org.oclc.campbelg.marc8.help.MyHelp;

import java.io.IOException;

public class Main {

    private static final int MAX_RECORDS = 10;

    /**
     * Entry point for the command line tool
     * @param args
     */
    public static void main(String[] args) {
        int maxRecords = MAX_RECORDS;
        // If no arguments, display a hint on how to use the tool and exit.
        // At least one argument, the file name, is required!
        if (args.length == 0) {
            MyHelp.display();
        } else {
            // Optional argument #2 overrides the default 10 record limit.
            if (args.length == 2) {
                maxRecords = Integer.parseInt(args[1]);
            }
            // Argument #1 is the filename.
            String fileName = args[0];
            try {
                // Read the records into the Marc8 file.
                // The OutputMode is either ANSI or HTML.
                Marc8File marc8File = new Marc8File(fileName, maxRecords, OutputMode.ANSI);

                // Output the formatted Marc21/Marc8 records to the console.
                System.out.println(marc8File.convertRecordsToString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
