# VocabParse
Utility to change the date format in the OHDSI vocabulary files to yyyy-MM-dd for Azure SQL Server load.

It will convert the concept.csv, drug_strength.csv, and concept_relationship.csv.

Usage:

jave -jar VocabParse.jar -f <folder>

Files will be saved in subfolder "parsed"
