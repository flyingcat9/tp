package bloodnet.model.person;

import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} is eligible based on {@code HasBloodTypePredicate} and
 * {@code IsEligibleToDonatePredicate}
 */
public class HasBloodTypeAndIsEligibleToDonatePredicate implements Predicate<Person> {
    private final HasBloodTypePredicate bloodTypePredicate;
    private final IsEligibleToDonatePredicate eligibleToDonatePredicate;


    /**
     * Constructs a {@code HasBloodTypePredicate} and a {@code IsEligibleToDonatePredicate}
     */
    public HasBloodTypeAndIsEligibleToDonatePredicate(HasBloodTypePredicate bloodTypePredicate,
                                                      IsEligibleToDonatePredicate eligibleToDonatePredicate) {
        this.bloodTypePredicate = bloodTypePredicate;
        this.eligibleToDonatePredicate = eligibleToDonatePredicate;
    }

    /**
     * Returns true if both predicates are true.
     *
     * @param person Person that is being checked for.
     */
    public boolean test(Person person) {
        return bloodTypePredicate.test(person) && eligibleToDonatePredicate.test(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles null check
        if (!(other instanceof HasBloodTypeAndIsEligibleToDonatePredicate)) {
            return false;
        }

        HasBloodTypeAndIsEligibleToDonatePredicate otherHasBloodTypeAndIsEligibleToDonatePredicate =
                (HasBloodTypeAndIsEligibleToDonatePredicate) other;

        return this.bloodTypePredicate.equals(otherHasBloodTypeAndIsEligibleToDonatePredicate.bloodTypePredicate)
                && this.eligibleToDonatePredicate.equals(
                        otherHasBloodTypeAndIsEligibleToDonatePredicate.eligibleToDonatePredicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("hasBloodType", bloodTypePredicate)
                .add("dateOfBirthAndDaysSinceLastDonation", eligibleToDonatePredicate).toString();
    }
}
