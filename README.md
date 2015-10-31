# Marc8ToText

A command line utility to display a Marc21 format / Marc8 encoded record to the console with the fixed field layed out neatly, and the escapes explicitly displayed and labeled. It beats a hex dump for quick debugging any day.

## Requires

Java 1.8

## Install and Use

Download Marc8ToText.jar and run it thusly:

`java -jar Marc8ToText.jar filename [optional max records to display, default=10]`

Note: The optional max records to display was added because raw binary Marc21/Marc8 files can be enormous, and you don't want to lock up your machine by trying to display 10 million records. The purpose of this tool is to look at a handful of records to make sure you are processing them correctly.

## Sample output

![Color Marc Record](https://dl.dropboxusercontent.com/u/8515698/Color%20Marc21%3AMarc8%20Record.png "Color Marc21/Marc8 Record")
