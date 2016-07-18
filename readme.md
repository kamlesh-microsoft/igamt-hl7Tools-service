#Dependency of HL7Tools to IGAMT Lite

Contains a command line program that prepares data from the official HL7v2 database for ultimate 
inclusion into the IGAMT database.  It reads from an instance of mySQL and writes *.json files to the 
resources directory of this project.  These resources must be included in the *.jar file packaged from
this project.

Before this program can be run, One must transfer the following tables from the official HL7v2 standards database into a
mySQL database: 
	hl7messagetypes
	hl7versions
	hl7msgstructids
	hl7events
	hl7msgstructidsegments
	hl7segmentdataelements
	hl7datastructures
	hl7datastructurecomponents
	hl7components
	hl7tablevalues
	hl7dataelements
	hl7tables
	hl7msgstructidsegments
	hl7events
	hl7eventmessagetypes
	
The transfer process

Use MS Access to export the tables to MS Excel work sheets.
Use MS Excel to export the work sheets to *.csv files.
Create a database in mySQL called mdb.
Import the *.csv files into mdb.

###To build:
	$> mvn install

###To run:
	$> cd <project>/target
	$> java -jar hl7tools-service-1.0.1-SNAPSHOT-jar <versions>
	
	Example: $> java -jar hl7tools-service-1.0.1-SNAPSHOT-jar 2.1 2.2 2.3
	
###Program outputs to a MongoDB database named igamt.
