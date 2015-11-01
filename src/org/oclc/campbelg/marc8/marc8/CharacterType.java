package org.oclc.campbelg.marc8.marc8;

/**
 * Created by georgecampbell on 11/1/15.
 */
public enum CharacterType {
    ONE(2,"r"), TWO(3,"s");

    private int val;
    private String word;

    CharacterType(int val, String word) {
        this.val = val;
        this.word=word;
    }
}
