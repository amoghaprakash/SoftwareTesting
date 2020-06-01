# PHONE BOOK

Target Project : 
Java Phone Book by Marin Marinov: https://github.com/MapuH/Java-Phone-Book 

## Project Description 

The Java Phone Book is a simple console using application that has different commands to look up into a contacts file with the file extension of csv. 

There are commands that are used in the project:  

* List – lists all saved contacts in alphabetical order 

* Show – finds a contact by the name input 

* Find – searches for a contact by number 

* Add – saves a new contact entry into the phone book 

* Edit – modifies an existing contact 

* Delete – removes a contact from the phone book 

* Help – lists all valid commands in the application 
 

## Running the Application: 

### Software Needed: 

Eclipse IDE for Java Developers (https://www.eclipse.org/downloads/packages/) 

JUnit 4.12 (built into Eclipse IDE) 

Hamcrest core 1.3.0

JDK (https://www.oracle.com/technetwork/java/javase/downloads/index.html) 

### Importing the Project 

Make sure you've installed all requirements 

Clone this repository: git clone https://github.com/amoghaprakash/SoftwareTesting/ 

Open the Eclipse IDE 

Import the project 

File --> Open Projects from File System --> Select directory --> Click OK 

### Setting up the project build path

Right click -> Build path -> configure build path -> Source -> Add folder -> add src and tst-> click Apply and Close
Right click -> Build path -> configure build path -> Libraries -> Add. Libraries -> Junit5 -> click Apply and Close


## Running Project & Unit Tests 

### Running application 

With the Eclipse IDE opened and the Phonebook project imported, right-click on src/Phonebook.java, select Run As, then select Java Application. The program will launch in the IDE's Console window. 

### Running unit tests 

With the Eclipse IDE opened and the Phonebook project imported, right-click on tst/PhoneBookTest.java, select Run As, then select JUnit Test. Test results will be displayed in the IDE's JUnit window. 

### Code coverage analysis 
With the Eclipse IDE opened and the Phonebook project imported, right-click on the top-level Phonebook project directory, select Coverage As, then select JUnit Test. Coverage results will be displayed in the IDE's Coverage window. 

## Running Shell Scripts

### Copying Source code and Test files to server (We have used Seattle University CS1)

scp -r Sourcecode folder path Destination folder path

### Building Test Suite

Navigate to destination folder location on server.
run "chmod +rx buildTestSuite.sh"
run "./buildTestSuite.sh"

### Running Regression Test

Navigate to destination folder location on server.
run "chmod +rx runTestSuite.sh"
1. Executes the test suite one time
         ./runTestSuite.sh 1
2. Executes the test suite twice and e-mails results to test@gmail.com.
         ./runTestSuite.sh 2 test@gmail.com
3. Executes the test suite twice and e-mails the results to multiple recipients.
         ./runTestSuite.sh 2 "test1@gmail.com test2@gmail.com test3@gmail.com"
         

### Running Stress Test

Navigate to destination folder location on server.
run "chmod +rx stressTestApp.sh"
Execute "./stressTestApp.sh NumberOfInstances NumberOfIterations"

