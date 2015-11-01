package org.oclc.campbelg.marc8.marc8;

/**
 * Ansi and Web colors for characters.
 */
public enum CharacterColor {
    RESET("\u001B[0m", "</span>"),
    BLACK("\u001B[30m", "<span style=\"color:black\">"),
    RED("\u001B[31m", "<span style=\"color:red\">"),
    GREEN("\u001B[32m", "<span style=\"color:green\">"),
    YELLOW("\u001B[33m", "<span style=\"color:yellow\">"),
    BLUE("\u001B[34m", "<span style=\"color:blue\">"),
    PURPLE("\u001B[35m", "<span style=\"color:purple\">"),
    CYAN("\u001B[36m", "<span style=\"color:cyan\">"),
    WHITE("\u001B[37m", "<span style=\"color:white\">");

    private String ansiColor;
    private String webColor;

    CharacterColor(String ansiColor, String webColor) {
        this.ansiColor = ansiColor;
        this.webColor = webColor;
    }

    public String getWebColor() {
        return webColor;
    }

    public String getAnsiColor() {
        return ansiColor;
    }
}
