package org.oclc.campbelg.marc8;

import org.oclc.campbelg.marc8.marc8.Marc8File;
import org.oclc.campbelg.marc8.utilities.MyHelp;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            MyHelp.display();
        } else {
            String fileName = args[0];
            try {
                Marc8File marc8File = new Marc8File(fileName);
                System.out.println(marc8File.marc8RepresentationsToString());
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
