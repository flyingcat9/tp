package bloodnet.model.person;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EligibilityStatusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EligibilityStatus(null));
    }

    @Test
    public void constructor_invalidEligibilityStatus_throwsIllegalArgumentException() {
        String invalidEligibilityStatus = "";
        assertThrows(IllegalArgumentException.class, () -> new EligibilityStatus(invalidEligibilityStatus));
    }

    /**
     * Checks to see if the eligibility status is legal and valid.
     */
    @Test
    public void isValidEligibilityStatus() {
        // null eligibility status
        assertThrows(NullPointerException.class, () -> EligibilityStatus.isValidEligibilityStatus(null));

        // If spaces, the eligibility status is not valid.
        assertFalse(EligibilityStatus.isValidEligibilityStatus("")); // empty string
        assertFalse(EligibilityStatus.isValidEligibilityStatus(" ")); // spaces only

        // any invalid words that are not considered to be a status
        assertFalse(EligibilityStatus.isValidEligibilityStatus("x")); // missing local part
        assertFalse(EligibilityStatus.isValidEligibilityStatus("?")); // missing '@' symbol
        assertFalse(EligibilityStatus.isValidEligibilityStatus("going to be screened soon"));
        assertFalse(EligibilityStatus.isValidEligibilityStatus("age"));

        // valid eligible status
        assertTrue(EligibilityStatus.isValidEligibilityStatus("eligible")); // regular word
        assertTrue(EligibilityStatus.isValidEligibilityStatus("deferred"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("ineligible"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("cooldown"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("unscreened"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("ELIGIBLE")); // capital letters
        assertTrue(EligibilityStatus.isValidEligibilityStatus("defErred"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("CoolDown"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("INELIGIBLE"));
        assertTrue(EligibilityStatus.isValidEligibilityStatus("unSCREENED"));
    }

    @Test
    public void equals() {
        EligibilityStatus eligibilityStatus = new EligibilityStatus("eligible");

        // same object returns true
        assertTrue(eligibilityStatus.equals(eligibilityStatus));

        // same values returns true when compared with one another
        assertTrue(eligibilityStatus.equals(new EligibilityStatus("eligible")));

        // if null, false is returned
        assertFalse(eligibilityStatus.equals(null));

        // if it is a different type, it should return false
        assertFalse(eligibilityStatus.equals(2.0f));

        // different values -> returns false
        assertFalse(eligibilityStatus.equals(new EligibilityStatus("ineligible")));
    }
}
