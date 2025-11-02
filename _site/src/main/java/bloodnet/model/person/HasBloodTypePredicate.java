package bloodnet.model.person;

import java.util.List;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class HasBloodTypePredicate implements Predicate<Person> {
    private final List<String> bloodTypes;

    /**
     * Constructs a {@code HasBloodTypePredicate}.
     *
     * @param bloodTypes A list of blood types to be matched provided by the user.
     */
    public HasBloodTypePredicate(List<String> bloodTypes) {
        this.bloodTypes = bloodTypes;
    }

    /**
     * Returns true if the person's blood type is in the list of bloodTypes.
     * {@code bloodType} is provided by the user.
     *
     * @param person Person you are checking the {@code bloodType} for.
     */
    public boolean test(Person person) {
        boolean bloodTypePredicate = bloodTypes.stream()
                .anyMatch(bloodType -> bloodType.equalsIgnoreCase(person.getBloodType().value));
        return bloodTypePredicate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HasBloodTypePredicate)) {
            return false;
        }

        HasBloodTypePredicate otherHasBloodTypePredicate = (HasBloodTypePredicate) other;
        return bloodTypes.equals(otherHasBloodTypePredicate.bloodTypes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("bloodType", bloodTypes).toString();
    }
}
