package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
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
        IsEligibleToDonatePredicate eligibleToDonatePredicate = new IsEligibleToDonatePredicate(model);

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
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model);

        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        String dateOfBirthOfPerson = LocalDate.now().minusYears(25)
                .minusMonths(10).format(formatter);

        assertTrue(bothPredicates.test(new PersonBuilder().withBloodType("O+")
                .withDateOfBirth(dateOfBirthOfPerson).build()));
    }

    @Test
    public void test_personFailureWithFailingBloodTypePredicate_returnsFalse() {
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model);

        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        String dateOfBirthOfPerson = LocalDate.now().minusYears(25)
                .minusMonths(10).format(formatter);

        assertFalse(bothPredicates.test(new PersonBuilder().withBloodType("A+")
                .withDateOfBirth(dateOfBirthOfPerson).build()));
    }

    @Test
    public void toStringMethod() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        HasBloodTypePredicate bloodPredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligiblePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypeAndIsEligibleToDonatePredicate predicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(bloodPredicate, new IsEligibleToDonatePredicate(model));

        String expected = HasBloodTypeAndIsEligibleToDonatePredicate.class.getCanonicalName()
                + "{hasBloodType=" + bloodPredicate.toString()
                + ", dateOfBirthAndDaysSinceLastDonation=" + eligiblePredicate.toString() + "}";
        assertEquals(expected, predicate.toString());
    }
}
