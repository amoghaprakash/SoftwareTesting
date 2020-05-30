import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class PhoneBookTestRunner {
	public static void main(String[] args) {
		Result res = JUnitCore.runClasses(PhoneBookTestSuit.class);
		System.out.println("Running PhoneBookTestSuit: ");
		for(Failure failure : res.getFailures()) {
			System.out.println(failure.toString());
			failure.getException().printStackTrace();
		}
		System.out.println("Tests Run: " + res.getRunCount() + "/28");
		System.out.println("Tests Failure count: " + res.getFailureCount() + "/28");
		System.out.println("Tests Successful? :" + res.wasSuccessful());
	}
}
