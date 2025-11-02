package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.testutil.DonationRecordBuilder;
import bloodnet.testutil.PersonBuilder;

public class IsEligibleToDonatePredicateTest {
    private DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());

    @Test
    public void equals() {
        IsEligibleToDonatePredicate predicateOne = new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());
        IsEligibleToDonatePredicate predicateTwo = new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());

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
        Person person = new PersonBuilder().withId(UUID.randomUUID())
                .withDateOfBirth(youngestDateOfBirthAllowed).build();
        model.addPerson(person);
        assertTrue(predicate.test(person));
    }

    /**
     * Tests individual with age in between 16 and 60.
     */
    @Test
    public void donorIsBetweenSixteenAndSixty_returnsTrue() {
        String dateOfBirthInRange = LocalDate.now().minusYears(35).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirthInRange).build();
        model.addPerson(person);
        assertTrue(predicate.test(person));
    }

    /**
     * Tests individual with age in between 16 and 60 and has donated before.
     */
    @Test
    public void donorIsInRangeAndHasDonatedBefore_returnsTrue() {
        String dateOfBirthInRangeAndDonatedBefore = LocalDate.now().minusYears(35).format(formatter);
        Person personInRangeAndDonatedBefore = new PersonBuilder()
            .withId(UUID.randomUUID())
            .withDateOfBirth(dateOfBirthInRangeAndDonatedBefore).build();
        model.addPerson(personInRangeAndDonatedBefore);
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
        Person oldestFirstTimePerson = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(oldestFirstTimeDonator).build();
        model.addPerson(oldestFirstTimePerson);
        assertTrue(predicate.test(oldestFirstTimePerson));
    }

    /**
     * Tests individual who is 61 years old but has donated before.
     */
    @Test
    public void donorIsSixtyOneButDonatedBefore_returnsTrue() {
        String overSixtyButHasDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(overSixtyButHasDonatedBefore).build();
        model.addPerson(sixtyOneYearsOldPerson);

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
        Person sixtyOneYearsOldPerson = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(olderButHasDonatedBefore).build();
        model.addPerson(sixtyOneYearsOldPerson);
        assertFalse(predicate.test(sixtyOneYearsOldPerson));
    }

    /**
     * Tests individual who is the oldest repeat donor.
     */
    @Test
    public void oldestRepeatDonor_returnsTrue() {
        String olderButHasDonatedBefore = LocalDate.now().minusYears(66).plusDays(1)
                .format(formatter);
        Person oldestRepeatDonor = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(olderButHasDonatedBefore).build();
        model.addPerson(oldestRepeatDonor);
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
        Person personHasDonatedBefore = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(aboveSixtyFiveButHasDonatedBefore).build();
        model.addPerson(personHasDonatedBefore);

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
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(fifteenYearsOld).build();
        model.addPerson(person);
        assertFalse(predicate.test(person));
    }

    /**
     * Tests individual who donated within 12 weeks ago.
     */
    @Test
    public void validDateOfBirthRangeButDonationLengthTooShort_returnsFalse() {
        String dateOfBirthInRangeAndDonated = LocalDate.now().minusYears(35).format(formatter);
        Person tester = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirthInRangeAndDonated)
            .build();
        model.addPerson(tester);

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
        Person sixtyOneYearsOldPerson = new PersonBuilder().withId(UUID.randomUUID())
            .withDateOfBirth(neverDonatedBefore).build();
        model.addPerson(sixtyOneYearsOldPerson);
        assertFalse(predicate.test(sixtyOneYearsOldPerson));
    }

    /**
     * Tests an individual who is 66 years old and has not donated in the past three years.
     */
    @Test
    public void donorIsAboveSixtySixAndDonationIsNotInRange_returnsFalse() {
        String tooLongAgo = LocalDate.now().minusYears(66).format(formatter);
        Person sixtySixYearOld = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(tooLongAgo).build();
        model.addPerson(sixtySixYearOld);

        DonationRecordBuilder builder = new DonationRecordBuilder();
        String donationDateOfPerson = LocalDate.now().minusYears(10)
                .minusDays(15).format(formatter);
        model.addDonationRecord(builder.withPersonId(sixtySixYearOld.getId())
                .withDonationDate(donationDateOfPerson).build());

        assertFalse(predicate.test(sixtySixYearOld));
    }

    /**
     * Predecessor gap strictly less than 84 days should be ineligible.
     * Uses today as the donationDate and a previous donation 83 days ago.
     */
    @Test
    public void donorHasPredecessorDonationWithin84Days_returnsFalse() {
        String dateOfBirth = LocalDate.now().minusYears(30).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirth).build();
        model.addPerson(person);

        // Add a previous donation 83 days ago (predecessor < 84)
        String prevDonationDate = LocalDate.now().minusDays(83).format(formatter);
        model.addDonationRecord(new DonationRecordBuilder()
                        .withPersonId(person.getId())
                        .withDonationDate(prevDonationDate)
                        .build());

        // Using default predicate (donationDate = today)
        assertFalse(predicate.test(person));
    }

    /**
     * Predecessor gap exactly 84 days should be eligible.
     * Uses today as the donationDate and a previous donation exactly 84 days ago.
     */
    @Test
    public void donorHasPredecessorDonationAtExactly84Days_returnsTrue() {
        String dateOfBirth = LocalDate.now().minusYears(30).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirth).build();
        model.addPerson(person);

        // Add a previous donation exactly 84 days ago (boundary)
        String prevDonationDate = LocalDate.now().minusDays(84).format(formatter);
        model.addDonationRecord(new DonationRecordBuilder()
                        .withPersonId(person.getId())
                        .withDonationDate(prevDonationDate)
                        .build());

        // Using default predicate (donationDate = today)
        assertTrue(predicate.test(person));
    }

    /**
     * Tests individual who has a successor donation (later donation) at exactly 84 days.
     * Predicate should return true (is eligible).
     */
    @Test
    public void donorHasSuccessorDonationAtExactly84Days_returnsTrue() {
        String dateOfBirth = LocalDate.now().minusYears(30).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirth).build();
        model.addPerson(person);

        // Add another donation 150 days ago (successor to the test date)
        String secondDonationDate = LocalDate.now().minusDays(150).format(formatter);
        model.addDonationRecord(new DonationRecordBuilder()
                .withPersonId(person.getId())
                .withDonationDate(secondDonationDate)
                .build());

        // Test if person was eligible to donate 234 days ago (exactly 84 days before the second donation)
        // Should be true because the successor donation is exactly 84 days away
        String testDonationDate = LocalDate.now().minusDays(234).format(formatter);
        IsEligibleToDonatePredicate testPredicate = new IsEligibleToDonatePredicate(model,
                                                                                    new DonationDate(testDonationDate));
        assertTrue(testPredicate.test(person));
    }

    /**
     * Tests individual who has a successor donation (later donation) within 84 days.
     * Predicate should return false in this case (ineligible).
     */
    @Test
    public void donorHasSuccessorDonationWithin84Days_returnsFalse() {
        String dateOfBirth = LocalDate.now().minusYears(30).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirth).build();
        model.addPerson(person);
        // Add another donation 150 days ago (successor to the test date)
        String secondDonationDate = LocalDate.now().minusDays(150).format(formatter);
        model.addDonationRecord(new DonationRecordBuilder()
                .withPersonId(person.getId())
                .withDonationDate(secondDonationDate)
                .build());

        // Test if person was eligible to donate 180 days ago (only 30 days before the second donation)
        // Should be false because the successor donation is less than 84 days away
        String testDonationDate = LocalDate.now().minusDays(180).format(formatter);
        IsEligibleToDonatePredicate testPredicate = new IsEligibleToDonatePredicate(model,
                                                                                    new DonationDate(testDonationDate));
        assertFalse(testPredicate.test(person));
    }

    /**
     * Tests individual who has a successor donation (later donation) strictly more than 84 days away.
     * Predicate should return true (eligible) since the gap to the successor is > 84 days.
     */
    @Test
    public void donorHasSuccessorDonationMoreThan84Days_returnsTrue() {
        String dateOfBirth = LocalDate.now().minusYears(30).format(formatter);
        Person person = new PersonBuilder().withId(UUID.randomUUID()).withDateOfBirth(dateOfBirth).build();
        model.addPerson(person);

        // Choose a test donation date in the past
        String testDonationDate = LocalDate.now().minusDays(200).format(formatter);

        // Add a successor donation 100 days after the test date
        String successorDonationDate = LocalDate.now().minusDays(100).format(formatter); // 200 - 100 = 100 days gap
        model.addDonationRecord(new DonationRecordBuilder()
                        .withPersonId(person.getId())
                        .withDonationDate(successorDonationDate)
                        .build());

        IsEligibleToDonatePredicate testPredicate = new IsEligibleToDonatePredicate(model,
                                                                                    new DonationDate(testDonationDate));
        assertTrue(testPredicate.test(person));
    }

    @Test
    public void toStringMethod() {
        String expected = IsEligibleToDonatePredicate.class.getCanonicalName() + "{}";
        assertEquals(expected, predicate.toString());
    }
}
