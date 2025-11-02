package bloodnet.model.donationrecord;

import static bloodnet.testutil.Assert.assertThrows;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class DonationDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DonationDate(null));
    }

    @Test
    public void constructor_invalidDonationDate_throwsIllegalArgumentException() {
        String invalidDonationDate = "";
        assertThrows(IllegalArgumentException.class, () -> new DonationDate(invalidDonationDate));
    }

    @Test
    public void isValidDonationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);

        // null donationDate
        assertThrows(NullPointerException.class, () -> DonationDate.isValidDonationDate(null));

        // invalid donation date
        assertFalse(DonationDate.isValidDonationDate("")); // empty string
        assertFalse(DonationDate.isValidDonationDate(" ")); // spaces only, not accepted
        assertFalse(DonationDate.isValidDonationDate("\n\n\t")); // only non-alphanumeric characters

        LocalDate date130yearsAnd1DayAgo = LocalDate.now().minusYears(130).minusDays(1);
        assertFalse(DonationDate.isValidDonationDate(date130yearsAnd1DayAgo.format(formatter)));

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertFalse(DonationDate.isValidDonationDate(tomorrow.format(formatter)));

        assertFalse(DonationDate.isValidDonationDate(
                "XX-DD-YY11")); // contains alphanumeric characters and with the date range
        assertFalse(DonationDate.isValidDonationDate(
                "31-02-2010")); // contains an invalid day (February 31)
        assertFalse(DonationDate.isValidDonationDate(
                "33-01-2010")); // contains an invalid date
        assertFalse(DonationDate.isValidDonationDate(
                "30-13-2010")); // contains an invalid month
        assertFalse(DonationDate.isValidDonationDate(
                "30-01-1800")); // contains an invalid year

        // valid donation dates that are accepted
        LocalDate earliestDateAccepted = LocalDate.now().minusYears(130);
        assertTrue(DonationDate.isValidDonationDate(earliestDateAccepted.format(formatter))); // earliest date accepted
        LocalDate latestDateAccepted = LocalDate.now();
        assertTrue(DonationDate.isValidDonationDate(latestDateAccepted.format(formatter))); // latest date accepted
        assertTrue(DonationDate.isValidDonationDate("12-12-2002")); // random donation datedate
        assertTrue(DonationDate.isValidDonationDate("01-07-1951")); // random donation date
    }

    @Test
    public void equals() {
        DonationDate donationDate = new DonationDate("01-01-2008");
        DonationDate anotherValidBirthDate = new DonationDate("14-02-2000");

        // same values -> returns true
        assertTrue(donationDate.equals(new DonationDate("01-01-2008")));

        // same object -> returns true
        assertTrue(donationDate.equals(donationDate));

        // null -> returns false
        assertFalse(donationDate.equals(null));

        // different types -> returns false
        assertFalse(donationDate.equals(3.0f));

        // different values -> returns false
        assertFalse(donationDate.equals(anotherValidBirthDate));

        // checking the hashCode method against each other
        assertNotEquals(donationDate.hashCode(), anotherValidBirthDate.hashCode());

        //valid with itself
        assertEquals(donationDate.hashCode(), donationDate.hashCode());
    }
}
