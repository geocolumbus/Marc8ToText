# Marc8ToText

A command line utility to display a Marc21 format / Marc8 encoded record to the console with the fixed fields layed out neatly and escapes explicitly displayed and labeled. It beats a hex dump for quick visualization and debugging of the record.

## Requires

Java 1.7 or greater.

## Installation and Use

Download Marc8ToText.jar and run it thusly:

<pre>
git clone https://github.com/geocolumbus/Marc8ToText.git
cd Marc8ToText
mvn clean package
java -jar target/marc8-1.0-SNAPSHOT-jar-with-dependencies.jar
</pre>

Note: The optional max records to display has a default value of 10 because raw binary Marc21/Marc8 files can be enormous, and you don't want to tax your machine by trying to display 10 million records.

## Sample output

![Color Marc Record](https://dl.dropboxusercontent.com/u/8515698/Color%20Marc21%3AMarc8%20Record.png "Color Marc21/Marc8 Record")

## Sample Marc21 format Marc8 encoded records

* [OCLC - sample records with International Characters](http://psw.oclc.org/sample.html)
* [Large Marc21 records from various sources](http://wiki.koha-community.org/wiki/Free_sample_MARC_data)
