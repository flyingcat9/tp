package bloodnet.model.person;

import static bloodnet.testutil.Assert.assertThrows;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class DateOfBirthTest {

    /**
     * Checking what happens if null is thrown.
     */
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateOfBirth(null));
    }

    /**
     * The parser does not accept invalid dates.
     */
    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidDateOfBirth = " ";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidDateOfBirth));
    }

    @Test
    public void isValidDateOfBirth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        // null date of birth
        assertThrows(NullPointerException.class, () -> DateOfBirth.isValidDateOfBirth(null));

        // invalid date of birth
        assertFalse(DateOfBirth.isValidDateOfBirth("")); // empty string
        assertFalse(DateOfBirth.isValidDateOfBirth(" ")); // spaces only, not accepted
        assertFalse(DateOfBirth.isValidDateOfBirth("\n\n\t")); // only non-alphanumeric characters

        LocalDate earliestDateNotAccepted = LocalDate.now().minusYears(130).minusDays(1);
        assertFalse(DateOfBirth.isValidDateOfBirth(earliestDateNotAccepted
                .format(formatter))); // the earliest day not accepted
        LocalDate latestDateNotAccepted = LocalDate.now().plusDays(1);
        assertFalse(DateOfBirth.isValidDateOfBirth(latestDateNotAccepted.format(formatter))); // latest day not accepted
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "XX-DD-YY11")); // contains alphanumeric characters and with the date range
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "31-02-2010")); // contains an invalid day (February 31)
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "33-01-2010")); // contains an invalid date
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "30-13-2010")); // contains an invalid month
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "30-01-1800")); // contains an invalid year

        // valid date of births that are accepted
        LocalDate earliestDateAccepted = LocalDate.now().minusYears(130);
        assertTrue(DateOfBirth.isValidDateOfBirth(earliestDateAccepted.format(formatter))); // earliest date accepted
        LocalDate latestDateAccepted = LocalDate.now();
        assertTrue(DateOfBirth.isValidDateOfBirth(latestDateAccepted.format(formatter))); // latest date accepted
        assertTrue(DateOfBirth.isValidDateOfBirth("12-12-2002")); // random date of birthdate
        assertTrue(DateOfBirth.isValidDateOfBirth("01-07-1951")); // random date of birth

    }


    @Test
    public void equals() {
        DateOfBirth dateOfBirth = new DateOfBirth("01-01-2008");
        DateOfBirth anotherValidBirthDate = new DateOfBirth("14-02-2000");

        // same values -> returns true
        assertTrue(dateOfBirth.equals(new DateOfBirth("01-01-2008")));

        // same object -> returns true
        assertTrue(dateOfBirth.equals(dateOfBirth));

        // null -> returns false
        assertFalse(dateOfBirth.equals(null));

        // different types -> returns false
        assertFalse(dateOfBirth.equals(3.0f));

        // different values -> returns false
        assertFalse(dateOfBirth.equals(anotherValidBirthDate));

        // checking the hashCode method against each other
        assertNotEquals(dateOfBirth.hashCode(), anotherValidBirthDate.hashCode());

        //valid with itself
        assertEquals(dateOfBirth.hashCode(), dateOfBirth.hashCode());



    }
}
