import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhoneBookTest {

	private static final String EOL = System.getProperty("line.separator");
	private PrintStream consoleOut;
	private PrintStream consoleErr;
	private InputStream consoleIn;
	private ByteArrayOutputStream bytesOut;
	private ByteArrayOutputStream bytesErr;
	private ByteArrayInputStream bytesIn;
	
	private static final String OUTPUT_START = "PHONE BOOK (ver 0.2)" + EOL + "===========================" + EOL
			+ "Type a command or 'exit' to quit:" + EOL 
			+ "list - lists all saved contacts in alphabetical  order" + EOL 
			+ "show - finds a contact by name" + EOL 
			+ "find - searches for a contact by number" + EOL 
			+ "add - saves a new contact entry into the phone book" + EOL 
			+ "edit - modifies an existing contact" + EOL 
			+ "delete - removes a contact from the phone book" + EOL 
			+ "help - lists all valid commands" + EOL 
			+ "---------------------------" + EOL + "> ";
	
	private static final String OUTPUT_HELP = 
			"list - lists all saved contacts in alphabetical  order" + EOL 
			+ "show - finds a contact by name" + EOL
			+ "find - searches for a contact by number" + EOL
			+ "add - saves a new contact entry into the phone book" + EOL 
			+ "edit - modifies an existing contact" + EOL
			+ "delete - removes a contact from the phone book" + EOL
			+ "help - lists all valid commands" + EOL
			+ "---------------------------" + EOL;
	
	private static final String OUTPUT_END = EOL + "Type a command or 'exit' to quit. For a list of valid commands use 'help':" + EOL;

	//PhoneBooks
	private static final String EMPTY_PHONEBOOK = "tst/contactsEmpty.csv";
	private static final String VALID_PHONEBOOK = "tst/contacts.csv";
	private static final String MISSING_PHONEBOOK = "tst/missing.csv";
	private static final String ADD_NEWCONTACT_PHONEBOOK = "tst/newcontact.csv";
	private static final String WRONG_CONTACT_PHONEBOOK = "tst1/newcontact.csv";

	//Commands
	private static final String INVALID_COMMAND = "invalid";
	private static final String EXIT_COMMAND = "exit";
	private static final String LIST_COMMAND = "list";
	private static final String SHOW_COMMAND = "show";
	private static final String FIND_COMMAND = "find";
	private static final String ADD_COMMAND = "add";
	private static final String DELETE_COMMAND = "delete";
	private static final String EDIT_COMMAND = "edit";
	private static final String HELP_COMMAND = "help";
	private static final String SAVE_COMMAND = "saveContacts";
	
	//String Constants
	private static final String SHOW_CONTACT_START = "Enter the name you are looking for:";
	private static final String SHOW_CONTACT_ERROR = "Sorry, nothing found!";
	private static final String FIND_CONTACT_START = "Enter a number to see to whom does it belong:";
	private static final String FIND_CONTACT_PROMPT = "Enter number:";
	private static final String FIND_CONTACT_ERROR = "Invalid number! May contain only digits, spaces and '+'. Min length 3, max length 25.";
	private static final String ADD_CONTACT_START = "You are about to add a new contact to the phone book.";
	private static final String ADD_CONTACT_NAME = "Enter contact name:";
	private static final String ADD_CONTACT_NUMBER = "Enter contact number:";
	private static final String ADD_CONTACT_ERROR = "Name must be in range 2 - 50 symbols.";
	private static final String APP_TERMINATE = "'Phone Book 0.2' terminated.";
	private static final String LOAD_CONTACT_ERROR = "Could not load contacts, phone book is empty!";
	private static final String LIST_CONTACT_ERROR = "No records found, the phone book is empty!";
	private static final String EDIT_CONTACT_START = "Enter name of the contact you would like to modify:";
	private static final String EDIT_CONTACT_ERROR = "Sorry, name not found!";
	
	@Before
	public void setUp() {
		bytesOut = new ByteArrayOutputStream();
		bytesErr = new ByteArrayOutputStream();
		consoleOut = System.out;
		consoleErr = System.err;
		System.setOut(new PrintStream(bytesOut));
		System.setErr(new PrintStream(bytesErr));
		consoleIn = System.in;
	}

	@After
	public void tearDown() {
		PhoneBook pb = new PhoneBook(ADD_NEWCONTACT_PHONEBOOK);
		Map<String, List<String>> contacts = new HashMap<String, List<String>>();
		contacts.put("NewName", new ArrayList<>(Arrays.asList("123456789")));
		contacts.put("testingCorrectName", new ArrayList<>(Arrays.asList("6823462209")));
		contacts.put("testingForDele", new ArrayList<>(Arrays.asList("124124124", "3123415315")));
		contacts.put("testingForDeleInvalid", new ArrayList<>(Arrays.asList("151351355", "235436437246")));
		contacts.put("testingForEdit", new ArrayList<>(Arrays.asList("3134354525")));
		contacts.put("testingForEditInvalid", new ArrayList<>(Arrays.asList("4314325134")));
		contacts.put("testingWrongNumberFormat", new ArrayList<>(Arrays.asList("")));
		contacts.put("testingName", new ArrayList<>(Arrays.asList("+1234242")));
		contacts.put("testingForCancel", new ArrayList<>(Arrays.asList("+4542624624")));
		pb.saveContacts(contacts);
		System.setIn(consoleIn);
		System.setOut(consoleOut);
		System.setErr(consoleErr);
	}

	@Test
	public void testLoadAndExit_LoadContactsAndExit_PhonebookTerminates() {
		bytesIn = new ByteArrayInputStream(EXIT_COMMAND.getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testLoadAndExit_LoadContactsAndExit_ExceptionWhenReadingCSV() {
		bytesIn = new ByteArrayInputStream(EXIT_COMMAND.getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(MISSING_PHONEBOOK);
		assertEquals(OUTPUT_START + APP_TERMINATE + EOL, bytesOut.toString());
		assertEquals(LOAD_CONTACT_ERROR + EOL, bytesErr.toString());
	}

	@Test
	public void testList_ValidPhonebook_DisplayPhonebookContent() {
		bytesIn = new ByteArrayInputStream((LIST_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "Marin" + EOL 
				+ "0887174411" + EOL
				+ "+359882379597" + EOL
				+ EOL
				+ "Plamena" + EOL
				+ "0883 456 789" + EOL
				+ EOL + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testListOnEmptyContacts_EmptyPhonebook_DisplayNoRecordsError() {
		bytesIn = new ByteArrayInputStream((LIST_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(EMPTY_PHONEBOOK);
		assertEquals(OUTPUT_START + LIST_CONTACT_ERROR + EOL 
			    + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_ValidContactName_DisplayContactInfo() {
		bytesIn = new ByteArrayInputStream((SHOW_COMMAND + EOL + "Marin" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + SHOW_CONTACT_START + EOL 
				+ "Marin" + EOL 
				+ "0887174411" + EOL
				+ "+359882379597" + EOL
			    + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_MissingContactName_DisplayError() {
		bytesIn = new ByteArrayInputStream((SHOW_COMMAND + EOL + "NoName" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + SHOW_CONTACT_START + EOL 
				+ SHOW_CONTACT_ERROR + EOL 
			    + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testfind_ValidContactNumber_DisplayContactInfo() {
		bytesIn = new ByteArrayInputStream((FIND_COMMAND + EOL + "0887174411" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + FIND_CONTACT_START + EOL 
				+ "Marin" + EOL 
				+ "0887174411" + EOL
			    + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testfind_InvalidContactNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((FIND_COMMAND + EOL + "-1." + EOL + "0887174411" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + FIND_CONTACT_START + EOL 
				+ FIND_CONTACT_ERROR + EOL 
				+ FIND_CONTACT_PROMPT + EOL
				+ "Marin" + EOL 
				+ "0887174411" + EOL
			    + OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}

	@Test
	public void testInvalidCommand_DisplayCommandError() {
		bytesIn = new ByteArrayInputStream((INVALID_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Invalid command!" + EOL + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testAddContact_ValidContactNameAndNumber_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((ADD_COMMAND + EOL + "testingAddName" + EOL + "6823462209" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ ADD_CONTACT_START + EOL
				+ ADD_CONTACT_NAME + EOL
				+ ADD_CONTACT_NUMBER + EOL
				+ "Successfully added contact 'testingAddName' !" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testAddContact_InValidContactName_DisplayError() {
		bytesIn = new ByteArrayInputStream((ADD_COMMAND + EOL + "@" + EOL + "testAddNewCorrectName" + EOL + "6823462209" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ ADD_CONTACT_START + EOL
				+ ADD_CONTACT_NAME + EOL
				+ "Name must be in range 2 - 50 symbols." + EOL
				+ ADD_CONTACT_NAME + EOL 
				+ ADD_CONTACT_NUMBER + EOL
				+ "Successfully added contact 'testAddNewCorrectName' !" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testAddContact_InValidContactNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((ADD_COMMAND + EOL + "testingWrongNumberFormat" + EOL + "$*6823462209" + EOL + "32314354643" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ ADD_CONTACT_START + EOL
				+ ADD_CONTACT_NAME + EOL
				+ ADD_CONTACT_NUMBER + EOL
				+ "Number may contain only '+', spaces and digits. Min length 3, max length 25." + EOL
				+ ADD_CONTACT_NUMBER + EOL 
				+ "Successfully added contact 'testingWrongNumberFormat' !" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testAddContact_ExistingContactNameNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((ADD_COMMAND + EOL + "testingCorrectName" + EOL + "6823462209" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ ADD_CONTACT_START + EOL
				+ ADD_CONTACT_NAME + EOL
				+ ADD_CONTACT_NUMBER + EOL
				+ "'testingCorrectName' already exists in the phone book!" + EOL
				+ "Number 6823462209 already available for contact 'testingCorrectName'." + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testAddContact_ExistingContactNameAddedNewNumber_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((ADD_COMMAND + EOL + "NewName" + EOL + "0123256789" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ ADD_CONTACT_START + EOL
				+ ADD_CONTACT_NAME + EOL
				+ ADD_CONTACT_NUMBER + EOL
				+ "'NewName' already exists in the phone book!" + EOL
				+ "Successfully added number 0123256789 for contact 'NewName'." + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testDeleteContact_ValidContactNameDeleteYes_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((DELETE_COMMAND + EOL + "testingName" + EOL + "Y" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Enter name of the contact to be deleted:" + EOL
				+ "Contact 'testingName' will be deleted. Are you sure? [Y/N]:"+ EOL
				+ "Contact was deleted successfully!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testDeleteContact_InValidContactName_DisplayError() {
		bytesIn = new ByteArrayInputStream((DELETE_COMMAND + EOL + "invalidtestingName" + EOL + "Y" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Enter name of the contact to be deleted:" + EOL
				+ "Sorry, name not found!" + EOL
				+ OUTPUT_END + EOL
				+ "> Invalid command!" + EOL + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testDeleteContact_ValidContactNameDeleteNo_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((DELETE_COMMAND + EOL + "testingName" + EOL + "N" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Enter name of the contact to be deleted:" + EOL
				+ "Contact 'testingName' will be deleted. Are you sure? [Y/N]:"+ EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testDeleteContact_ValidContactNameDeleteNotYesNotNo_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((DELETE_COMMAND + EOL + "testingName" + EOL + "A" + EOL + "Y" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Enter name of the contact to be deleted:" + EOL
				+ "Contact 'testingName' will be deleted. Are you sure? [Y/N]:"+ EOL
				+ "Delete contact? [Y/N]:" + EOL
				+ "Contact was deleted successfully!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	/*-------------------------------------EDIT START---------------------------------------*/
	@Test
	public void testEditContact_InValidContactName_DisplayError() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "WrongName" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ EDIT_CONTACT_ERROR + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_ValidContactNameAddNewNumber_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForEdit" + EOL  + "add" + EOL + "123456789" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForEdit:" + EOL
				+ "3134354525" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Enter new number:" + EOL
				+ "Number 123456789 was successfully added, record updated!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_ValidContactNameAddInValidNewNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForEditInvalid" + EOL + "add" + EOL + "@34" + EOL + "123456789" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForEditInvalid:" + EOL
				+ "4314325134" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Enter new number:" + EOL
				+ "Number may contain only '+', spaces and digits. Min length 3, max length 25." + EOL
				+ "Enter new number:" + EOL
				+ "Number 123456789 was successfully added, record updated!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_ValidContactNameDeleteNumber_DisplaySuccess() {
		//change numbers here
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForDele" + EOL + "delete" + EOL + "3123415315" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForDele:" + EOL
				+ "124124124" + EOL
				+ "3123415315" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Enter the number you want to delete:" + EOL
				+ "Number 3123415315 was removed from the record for 'testingForDele'" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_InValidContactNameDeleteNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForDeleInvalid" + EOL + "delete" + EOL + "124365362" + EOL + "235436437246" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForDeleInvalid:" + EOL
				+ "151351355" + EOL
				+ "235436437246" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Enter the number you want to delete:" + EOL
				+ "Number does not exist! Current number(s) for testingForDeleInvalid:" + EOL
				+ "151351355" + EOL
				+ "235436437246" + EOL
				+ "Enter the number you want to delete:" + EOL
				+ "Number 235436437246 was removed from the record for 'testingForDeleInvalid'" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_ValidContactNameCancel_DisplaySuccess() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForCancel" + EOL  + "cancel" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForCancel:" + EOL
				+ "+4542624624" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Contact was not modified!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	
	@Test
	public void testEditContact_ValidContactNameEdit_DisplayError() {
		bytesIn = new ByteArrayInputStream((EDIT_COMMAND + EOL + "testingForCancel" + EOL  + "+t21" + EOL + "cancel" + EOL +  EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ EDIT_CONTACT_START + EOL
				+ "Current number(s) for testingForCancel:" + EOL
				+ "+4542624624" + EOL + EOL
				+ "Would you like to add a new number or delete an existing number for this contact? [add/delete/cancel]" + EOL
				+ "Use 'add' to save a new number, 'delete' to remove an existing number or 'cancel' to go back." + EOL
				+ "Contact was not modified!" + EOL
				+ OUTPUT_END + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testHelp_ValidHelpCommands_DisplayCommands() {
		bytesIn = new ByteArrayInputStream((HELP_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(ADD_NEWCONTACT_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ OUTPUT_HELP + EOL
				+ "> " + APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testEmptyArguments_EmptyArguments_Exit() {
		bytesIn = new ByteArrayInputStream((EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main();
		assertEquals(OUTPUT_START 
				+ APP_TERMINATE + EOL, bytesOut.toString());
	}
	@Test
	public void testSave_WrongPhonebookPath_ThrowsException() {
		PhoneBook pb = new PhoneBook(WRONG_CONTACT_PHONEBOOK);
		Map<String, List<String>> contacts = new HashMap<String, List<String>>();
		pb.saveContacts(contacts);
		//bytesIn = new ByteArrayInputStream((pb.saveContacts(contacts).getBytes());
		//System.setIn(bytesIn);
		//PhoneBook.main();
		assertEquals("tst1/newcontact.csv (No such file or directory)" + EOL
				, bytesErr.toString()); 
	}

}
