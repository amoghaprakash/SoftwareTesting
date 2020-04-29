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
	private InputStream consoleIn;
	private ByteArrayOutputStream bytesOut;
	private ByteArrayInputStream bytesIn;
	private String outputStart = "";
	private String outputEnd = "";

	@Before
	public void setUp() {
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
		
		outputEnd = EOL + "Type a command or 'exit' to quit. For a list of valid commands use 'help':" + EOL;
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
		assertEquals(outputStart + "'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}
	@Test
	public void testListContacts() {
		bytesIn = new ByteArrayInputStream(("list"+ EOL + "exit").getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("tst/contacts.csv");
		assertEquals(outputStart + "Marin" + EOL 
				+ "0887174411" + EOL
				+ "+359882379597" + EOL
				+ EOL
				+ "Plamena" + EOL
				+ "0883 456 789" + EOL
				+ EOL + outputEnd + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}
	@Test
	public void testEmptyListContacts() {
		bytesIn = new ByteArrayInputStream(("list"+ EOL + "exit").getBytes());
		System.setIn(bytesIn);
		PhoneBook.main("tst/contactsEmpty.csv");
		assertEquals(outputStart + "No records found, the phone book is empty!" + EOL 
			    + outputEnd + EOL
				+ "> 'Phone Book 0.2' terminated." + EOL, bytesOut.toString());
	}
}
