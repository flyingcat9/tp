package bloodnet.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.testutil.PersonBuilder;

public class HasBloodTypePredicateTest {

    @Test
    public void equals() {
        List<String> oneBloodTypeProvided = Collections.singletonList("A+");
        List<String> multipleBloodTypesProvided = Arrays.asList("B+", "O+", "A+");

        HasBloodTypePredicate oneBloodType = new HasBloodTypePredicate(oneBloodTypeProvided);
        HasBloodTypePredicate multipleBloodTypes = new HasBloodTypePredicate(multipleBloodTypesProvided);

        // same object -> returns true
        assertTrue(oneBloodType.equals(oneBloodType));

        // same values -> returns true
        HasBloodTypePredicate oneBloodTypeCopy = new HasBloodTypePredicate(oneBloodTypeProvided);
        assertTrue(oneBloodType.equals(oneBloodTypeCopy));

        // different types -> returns false
        assertFalse(oneBloodType.equals(1));

        // null -> returns false
        assertFalse(oneBloodType.equals(null));

        // different person -> returns false
        assertFalse(oneBloodType.equals(multipleBloodTypes));
    }

    @Test
    public void test_personHasBloodType_returnsTrue() {
        // One blood type provided
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Collections.singletonList("O+"));
        assertTrue(predicate.test(new PersonBuilder().withBloodType("O+").build()));

        // Multiple blood types provided
        List<String> multipleBloodTypesProvided = Arrays.asList("AB+", "O+", "A+");
        predicate = new HasBloodTypePredicate(multipleBloodTypesProvided);
        assertTrue(predicate.test(new PersonBuilder().withBloodType("O+").build()));
    }

    @Test
    public void test_personDoesNotHaveBloodType_returnsFalse() {
        // Zero blood types listed
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withBloodType("O+").build()));

        // Non-matching blood type provided
        predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        assertFalse(predicate.test(new PersonBuilder().withBloodType("A+").build()));

        // Non-matching blood types provided
        List<String> multipleBloodTypesProvided = Arrays.asList("AB+", "O+", "A+");
        predicate = new HasBloodTypePredicate(multipleBloodTypesProvided);
        assertFalse(predicate.test(new PersonBuilder().withBloodType("O-").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> bloodTypesProvided = List.of("bloodTypeOne", "bloodTypeTwo");
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(bloodTypesProvided);
        String expected = HasBloodTypePredicate.class.getCanonicalName() + "{bloodType=" + bloodTypesProvided + "}";
        assertEquals(expected, predicate.toString());
    }
}
