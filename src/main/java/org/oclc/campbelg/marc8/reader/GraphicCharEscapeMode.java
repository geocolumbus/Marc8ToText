package org.oclc.campbelg.marc8.reader;

/**
 * Graphic character escape mode, per ISO/IEC 2022.
 */
public enum GraphicCharEscapeMode {
    NONE,
    G0_SET_NORMAL,
    G0_SET_ADDITIONAL,
    G1_SET_NORMAL,
    G1_SET_ADDITIONAL
}
