import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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
	
	private static final String OUTPUT_END = EOL + "Type a command or 'exit' to quit. For a list of valid commands use 'help':" + EOL;

	//PhoneBooks
	private static final String EMPTY_PHONEBOOK = "tst/contactsEmpty.csv";
	private static final String VALID_PHONEBOOK = "tst/contacts.csv";
	private static final String MISSING_PHONEBOOK = "tst/missing.csv";

	//Commands
	private static final String INVALID_COMMAND = "invalid";
	private static final String EXIT_COMMAND = "exit";
	private static final String LIST_COMMAND = "list";
	private static final String SHOW_COMMAND = "show";
	private static final String FIND_COMMAND = "find";

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
		System.setIn(consoleIn);
		System.setOut(consoleOut);
		System.setErr(consoleErr);
	}

	@Test
	public void testLoadAndExit_LoadContactsAndExit_PhonebookTerminates() {
		bytesIn = new ByteArrayInputStream(EXIT_COMMAND.getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testLoadAndExit_LoadContactsAndExit_ExceptionWhenReadingCSV() {
		bytesIn = new ByteArrayInputStream(EXIT_COMMAND.getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(MISSING_PHONEBOOK);
		assertEquals(OUTPUT_START + "'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
		assertEquals("Could not load contacts, phone book is empty!" + EOL, bytesErr.toString());
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
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testListOnEmptyContacts_EmptyPhonebook_DisplayNoRecordsError() {
		bytesIn = new ByteArrayInputStream((LIST_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(EMPTY_PHONEBOOK);
		assertEquals(OUTPUT_START + "No records found, the phone book is empty!" + EOL 
			    + OUTPUT_END + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_ValidContactName_DisplayContactInfo() {
		bytesIn = new ByteArrayInputStream((SHOW_COMMAND + EOL + "Marin" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "Enter the name you are looking for:" + EOL 
				+ "Marin" + EOL 
				+ "0887174411" + EOL
				+ "+359882379597" + EOL
			    + OUTPUT_END + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_MissingContactName_DisplayError() {
		bytesIn = new ByteArrayInputStream((SHOW_COMMAND + EOL + "Marin_1" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "Enter the name you are looking for:" + EOL 
				+ "Sorry, nothing found!" + EOL 
			    + OUTPUT_END + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_ValidContactNumber_DisplayContactInfo() {
		bytesIn = new ByteArrayInputStream((FIND_COMMAND + EOL + "0887174411" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "Enter a number to see to whom does it belong:" + EOL 
				+ "Marin" + EOL 
				+ "0887174411" + EOL
			    + OUTPUT_END + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testShow_InvalidContactNumber_DisplayError() {
		bytesIn = new ByteArrayInputStream((FIND_COMMAND + EOL + "-1." + EOL + "0887174411" + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START + "Enter a number to see to whom does it belong:" + EOL 
				+ "Invalid number! May contain only digits, spaces and '+'. Min length 3, max length 25." + EOL 
				+ "Enter number:" + EOL
				+ "Marin" + EOL 
				+ "0887174411" + EOL
			    + OUTPUT_END + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

	@Test
	public void testInvalidCommand_DisplayCommandError() {
		bytesIn = new ByteArrayInputStream((INVALID_COMMAND + EOL + EXIT_COMMAND).getBytes());
		System.setIn(bytesIn);
		PhoneBook.main(VALID_PHONEBOOK);
		assertEquals(OUTPUT_START 
				+ "Invalid command!" + EOL + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}

}
