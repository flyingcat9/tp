package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.testutil.PersonBuilder;

public class HasBloodTypeAndIsEligibleToDonatePredicateTest {
    private DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
    private final Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());


    @Test
    public void equals() {

        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        String[] arrayOfBloodType = new String[]{"O+"};

        HasBloodTypePredicate firstBloodTypePredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligibleToDonatePredicate =
                new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());

        HasBloodTypePredicate secondBloodTypePredicate =
                new HasBloodTypePredicate(Arrays.asList(arrayOfBloodType));

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicateCopy =
                new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate secondPredicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(secondBloodTypePredicate, eligibleToDonatePredicate);

        // same predicate -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicateCopy.equals(1));

        // null -> returns false
        assertFalse(firstPredicateCopy.equals(null));

        // different person -> returns false
        assertFalse(firstPredicateCopy.equals(secondPredicate));
    }


    @Test
    public void test_personSuccessWithBothPredicates_returnsTrue() {
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());

        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        String dateOfBirthOfPerson = LocalDate.now().minusYears(25)
                .minusMonths(10).format(formatter);

        // Build a person and add to model to satisfy validations that look up the person
        Person person = new PersonBuilder().withId(UUID.randomUUID())
                .withBloodType("O+")
                .withDateOfBirth(dateOfBirthOfPerson).build();
        model.addPerson(person);
        assertTrue(bothPredicates.test(person));
    }

    @Test
    public void test_personFailureWithFailingBloodTypePredicate_returnsFalse() {
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());

        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        String dateOfBirthOfPerson = LocalDate.now().minusYears(25)
                .minusMonths(10).format(formatter);

        Person person = new PersonBuilder().withId(UUID.randomUUID())
                .withBloodType("A+")
                .withDateOfBirth(dateOfBirthOfPerson).build();
        model.addPerson(person);
        assertFalse(bothPredicates.test(person));
    }

    @Test
    public void toStringMethod() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        HasBloodTypePredicate bloodPredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligiblePredicate =
                new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate());
        HasBloodTypeAndIsEligibleToDonatePredicate predicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(bloodPredicate,
                        new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate()));

        String expected = HasBloodTypeAndIsEligibleToDonatePredicate.class.getCanonicalName()
                + "{hasBloodType=" + bloodPredicate.toString()
                + ", dateOfBirthAndDaysSinceLastDonation=" + eligiblePredicate.toString() + "}";
        assertEquals(expected, predicate.toString());
    }
}
