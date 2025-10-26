package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.testutil.DonationRecordBuilder;
import bloodnet.testutil.PersonBuilder;

public class IsEligibleToDonatePredicateTest {
    private DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);

    @Test
    public void equals() {
        IsEligibleToDonatePredicate predicateOne = new IsEligibleToDonatePredicate(model);
        IsEligibleToDonatePredicate predicateTwo = new IsEligibleToDonatePredicate(model);

        // same object -> return true
        assertTrue(predicateOne.equals(predicateOne));

        // compare object with null -> return false
        assertFalse(predicateOne.equals(null));

        // compare object with a different type -> return false
        assertFalse(predicateOne.equals(1));

        // compare two objects with one another -> returns false
        assertTrue(predicateOne.equals(predicateTwo));
    }

    /**
     * Tests the youngest age eligible to donate blood.
     */
    @Test
    public void donorIsSixteen_returnsTrue() {
        String youngestDateOfBirthAllowed = LocalDate.now().minusYears(16).format(formatter);
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth(youngestDateOfBirthAllowed).build()));
    }

    /**
     * Tests individual with age in between 16 and 60.
     */
    @Test
    public void donorIsBetweenSixteenAndSixty_returnsTrue() {
        String dateOfBirthInRange = LocalDate.now().minusYears(35).format(formatter);
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth(dateOfBirthInRange).build()));
    }

    /**
     * Tests individual with age in between 16 and 60 and has donated before.
     */
    @Test
    public void donorIsInRangeAndHasDonatedBefore_returnsTrue() {
        String dateOfBirthInRangeAndDonatedBefore = LocalDate.now().minusYears(35).format(formatter);
        Person personInRangeAndDonatedBefore = new PersonBuilder()
                .withDateOfBirth(dateOfBirthInRangeAndDonatedBefore).build();
        String donationDateOfPerson = LocalDate.now().minusYears(13)
                .minusMonths(10).format(formatter);
        model.addDonationRecord(new DonationRecordBuilder().withPersonId(personInRangeAndDonatedBefore.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertTrue(predicate.test(personInRangeAndDonatedBefore));
    }

    /**
     * Tests individual who is 60 years and 354 days old.
     * Represents the oldest age a first time blood donor can be.
     */
    @Test
    public void donorIsSixtyYearsOld_returnsTrue() {
        String oldestFirstTimeDonator = LocalDate.now().minusYears(61)
                .plusDays(1).format(formatter);
        Person oldestFirstTimePerson = new PersonBuilder().withDateOfBirth(oldestFirstTimeDonator).build();

        assertTrue(predicate.test(oldestFirstTimePerson));
    }

    /**
     * Tests individual who is 61 years old but has donated before.
     */
    @Test
    public void donorIsSixtyOneButDonatedBefore_returnsTrue() {
        String overSixtyButHasDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withDateOfBirth(overSixtyButHasDonatedBefore).build();

        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusYears(13)
                .minusMonths(10).format(formatter);
        model.addDonationRecord(builder.withPersonId(sixtyOneYearsOldPerson.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertTrue(predicate.test(sixtyOneYearsOldPerson));
    }

    /**
     * Tests individual who is 61 years old and has never donated before.
     */
    @Test
    public void donorIsSixtyOneAndNeverDonatedBefore() {
        String olderButHasDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withDateOfBirth(olderButHasDonatedBefore).build();
        assertFalse(predicate.test(sixtyOneYearsOldPerson));
    }

    /**
     * Tests individual who is the oldest repeat donor.
     */
    @Test
    public void oldestRepeatDonor_returnsTrue() {
        String olderButHasDonatedBefore = LocalDate.now().minusYears(66).plusDays(1)
                .format(formatter);
        Person oldestRepeatDonor = new PersonBuilder().withDateOfBirth(olderButHasDonatedBefore).build();
        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusYears(13)
                .minusMonths(10).format(formatter);
        model.addDonationRecord(builder.withPersonId(oldestRepeatDonor.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertTrue(predicate.test(oldestRepeatDonor));
    }

    /**
     * Tests individual who is over 66 years old but has donated
     * in the past three years.
     */
    @Test
    public void donatedWithLastThreeYears_returnsTrue() {
        String aboveSixtyFiveButHasDonatedBefore = LocalDate.now().minusYears(68).format(formatter);
        Person personHasDonatedBefore = new PersonBuilder().withDateOfBirth(aboveSixtyFiveButHasDonatedBefore).build();

        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusMonths(11)
                .minusDays(22).format(formatter);
        model.addDonationRecord(builder.withPersonId(personHasDonatedBefore.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertTrue(predicate.test(personHasDonatedBefore));
    }

    /**
     * Tests individual who is 15 years and 364 days old.
     */
    @Test
    public void donorIsFifteen_returnsFalse() {
        String fifteenYearsOld = LocalDate.now().minusYears(16)
                .plusDays(1).format(formatter);
        assertFalse(predicate.test(new PersonBuilder().withDateOfBirth(fifteenYearsOld).build()));
    }

    /**
     * Tests individual who donated within 12 weeks ago.
     */
    @Test
    public void validDateOfBirthRangeButDonationLengthTooShort_returnsFalse() {
        String dateOfBirthInRangeAndDonated = LocalDate.now().minusYears(35).format(formatter);
        Person tester = new PersonBuilder().withDateOfBirth(dateOfBirthInRangeAndDonated).build();

        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusMonths(1)
                .minusDays(15).format(formatter);
        model.addDonationRecord(builder.withPersonId(tester.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertFalse(predicate.test(tester));
    }

    /**
     * Tests individual who is 61 and never donated before
     */
    @Test
    public void donorIsSixtyOneAndNeverDonatedBefore_returnsFalse() {
        String neverDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withDateOfBirth(neverDonatedBefore).build();
        assertFalse(predicate.test(sixtyOneYearsOldPerson));
    }

    /**
     * Tests an individual who is 66 years old and has not donated in the past three years.
     */
    @Test
    public void donorIsAboveSixtySixAndDonationIsNotInRange_returnsFalse() {
        String tooLongAgo = LocalDate.now().minusYears(66).format(formatter);
        Person sixtySixYearOld = new PersonBuilder().withDateOfBirth(tooLongAgo).build();

        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusYears(10)
                .minusDays(15).format(formatter);
        model.addDonationRecord(builder.withPersonId(sixtySixYearOld.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertFalse(predicate.test(sixtySixYearOld));
    }

    @Test
    public void toStringMethod() {
        String expected = IsEligibleToDonatePredicate.class.getCanonicalName() + "{}";
        assertEquals(expected, predicate.toString());
    }
}
