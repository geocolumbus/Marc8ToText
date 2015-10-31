# Marc8ToText

Command line utility to display a Marc21 / Marc8 record to the console in mostly human readable form.

## Usage

`java -jar Marc8ToText.jar path/filename.marc [max records to display, default=1]`

## Sample output

Note - output on ANSI terminals is in color.

<pre>
RECORD LENGTH: 00982
RECORD STATUS: n
TYPE OF RECORD: a
BIBLIOGRAPHIC LEVEL: m
TYPE OF CONTROL:
CHARACTER CODING SCHEME: a
INDICATOR COUNT: 2
SUBFIELD CODE COUNT: 2
BASE ADDRESS OF DATE: 00301
ENCODING LEVEL: 1
DESCRIPTIVE CATALOGING FORM: i
MULTIPART RESOURCE RECORD LEVEL:
LENGTH OF THE LENGTH-OF-FIELD PORTION: 4
LENGTH OF THE STARTING-CHARACTER-POSITION PORTION: 5
LENGTH OF THE IMPLEMENTATION-DEFINED PORTION: 0
UNDEFINED: 0
001 016 00000
003 004 00016
005 017 00020
006 015 00037
007 015 00052
008 041 00067
020 026 00108
035 025 00134
035 024 00159
035 023 00183
040 013 00206
041 011 00219
100 032 00230
240 031 00262
245 103 00293
260 040 00396
300 027 00436
500 037 00463
500 020 00500
538 030 00520
600 032 00550
651 049 00582
651 049 00631[1E Fld Term]
MIU01-000018011[1E Fld Term]
MiU[1E Fld Term]
19880715000000.0[1E Fld Term]
m        d||||[1E Fld Term]
cr bn ---auaua[1E Fld Term]
880715s1980    enk           00110deng d[1E Fld Term]

[1F Subfld]a0701124695 :
[1F Subfld]c12.95[1E Fld Term]

[1F Subfld]a(RLIN)MIUG16773209-B[1E Fld Term]

[1F Subfld]a(CaOTULAS)159837895[1E Fld Term]

[1F Subfld]a(OCoLC)ocm06615413[1E Fld Term]

[1F Subfld]aMiU
[1F Subfld]cMiU[1E Fld Term]
1
[1F Subfld]aengscr[1E Fld Term]
1
[1F Subfld]aMiunovi, Veljko,
[1F Subfld]d1916-[1E Fld Term]
10
[1F Subfld]aMoskovske godine.
[1F Subfld]lEnglish[1E Fld Term]
10
[1F Subfld]aMoscow diary /
[1F Subfld]cVeljko Miunovi ; translated by David Floyd ; with an introd. by George Kennan.[1E Fld Term]

[1F Subfld]aLondon :
[1F Subfld]bChatto and Windus,
[1F Subfld]c1980.[1E Fld Term]

[1F Subfld]axxvi, 474 p. ;
[1F Subfld]c25 cm.[1E Fld Term]

[1F Subfld]aTranslation of Moskovske godine.[1E Fld Term]

[1F Subfld]aIncludes index.[1E Fld Term]

[1F Subfld]aMode of access: Internet.[1E Fld Term]
10
[1F Subfld]aMiunovi, Veljko,
[1F Subfld]d1916-[1E Fld Term]
 0
[1F Subfld]aYugoslavia
[1F Subfld]xForeign relations
[1F Subfld]zSoviet Union.[1E Fld Term]
 0
[1F Subfld]aSoviet Union
[1F Subfld]xForeign relations
[1F Subfld]zYugoslavia.[1E Fld Term]

[1D Rec Term]
</pre>
