package org.oclc.campbelg.marc8.utilities;

/**
 * Display a little help message when the user cacs on the command line.
 */
public class MyHelp {

    public static void display() {
       String ret="Usage: marc8 filename [max number of records]";
        System.out.println(ret);
    }
}
