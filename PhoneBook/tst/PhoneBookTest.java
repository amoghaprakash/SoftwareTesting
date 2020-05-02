import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneBookTest {
	PhoneBook p;

	private static final String EOL = System.getProperty("line.separator");
	private PrintStream consoleOut;
	private InputStream consoleIn;
	private ByteArrayOutputStream bytesOut;
	private ByteArrayInputStream bytesIn;
	private String outputStart = "";
	private String outputEnd = "";
	private String showContactStart = "";
	private String showContactError = "";
	private String findContactStart = "";
	private String findContactPrompt = "";
	private String findContactError = "";
	private String addContactStart = "";
	private String addContactPrompt = "";
	private String addContactError = "";
	private String appTerminate = "";

	@Before
	public void setUp() {
		p = new PhoneBook("tst/contacts.csv");
		bytesOut = new ByteArrayOutputStream();
		consoleOut = System.out;
		System.setOut(new PrintStream(bytesOut));
		consoleIn = System.in;
		
		outputStart = "PHONE BOOK (ver 0.2)" + EOL + "===========================" + EOL
				+ "Type a command or 'exit' to quit:" + EOL 
				+ "list - lists all saved contacts in alphabetical  order" + EOL 
				+ "show - finds a contact by name" + EOL 
				+ "find - searches for a contact by number" + EOL 
				+ "add - saves a new contact entry into the phone book" + EOL 
				+ "edit - modifies an existing contact" + EOL 
				+ "delete - removes a contact from the phone book" + EOL 
				+ "help - lists all valid commands" + EOL 
				+ "---------------------------" + EOL + "> ";

		showContactStart = "Enter the name you are looking for:";
		showContactError = "Sorry, nothing found!";
		findContactStart = "Enter a number to see to whom does it belong:";
		findContactPrompt = "Enter number:";
		findContactError = "Invalid number! May contain only digits, spaces and '+'. Min length 3, max length 25.";
		addContactStart = "You are about to add a new contact to the phone book.";
		addContactPrompt = "Enter contact name:";
		addContactError = "Name must be in range 2 - 50 symbols.";
		appTerminate = "'Phone Book 0.2' terminated.";

		
		outputEnd = EOL + "Type a command or 'exit' to quit. For a list of valid commands use 'help':" + EOL;
	}

	private void provideInput(String data) {
		bytesIn = new ByteArrayInputStream(data.getBytes());
		System.setIn(bytesIn);
	}

	@After
	public void tearDown() {
		System.setIn(consoleIn);
		System.setOut(consoleOut);
	}

	@Test
	public void testAbortWhenInsufficientArgumentsSupplied() {
		bytesIn = new ByteArrayInputStream("exit".getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("tst/contactsEmpty.csv");
		assertEquals(outputStart + appTerminate + EOL, bytesOut.toString());
	}
	@Test
	public void testListContacts() {
		bytesIn = new ByteArrayInputStream(("list"+ EOL + "exit").getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("PhoneBook/tst/contacts.csv");
		assertEquals(outputStart + "Heetae" + EOL
				+ "2061234567" + EOL
				+ "1234567890" + EOL
				+ EOL
				+ "Marin" + EOL
				+ "0887174411" + EOL
				+ "+359882379597" + EOL
				+ EOL
				+ "Plamena" + EOL
				+ "0883 456 789" + EOL
				+ EOL + outputEnd + EOL
				+ "> " + appTerminate + EOL, bytesOut.toString());
	}
	@Test
	public void testEmptyListContacts() {
		bytesIn = new ByteArrayInputStream(("list"+ EOL + "exit").getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("tst/contactsEmpty.csv");
		assertEquals(outputStart + "No records found, the phone book is empty!" + EOL 
			    + outputEnd + EOL
				+ "> " + appTerminate + EOL, bytesOut.toString());
	}

	@Test
	public void testFindContacts() {
		bytesIn = new ByteArrayInputStream(("find"+ EOL + "2061234567" + EOL + "exit").getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("/tst/contacts.csv");
		assertEquals(outputStart + findContactStart
				+ EOL
				+ "2061234567"
				+ EOL
				+ "Heetae"
				+ EOL
				+ outputEnd + EOL, bytesOut.toString());
	}
}