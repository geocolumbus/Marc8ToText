package org.oclc.campbelg.marc8.help;

/**
 * Display a help message when the user fails to include proper arguments.
 */
public class MyHelp {
    public static void display() {
        String ret="Usage: marc8 filename [max number of records]";
        System.out.println(ret);
    }
}
