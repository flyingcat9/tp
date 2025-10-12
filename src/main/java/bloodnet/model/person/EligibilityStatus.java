package bloodnet.model.person;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This represents a Person's eligibility status in the donor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEligibilityStatus(String)} EligibilityStatus(String)}
 */
public class EligibilityStatus {

    /* Eligible means can donate, cooldown means the date since last donation is less than 12 weeks, deferred is
     * due to temporary illness, ineligible and can not donate permanently, unscreened for people in the
     * system but have yet to screen/donate
    */
    public static final String MESSAGE_CONSTRAINTS =
            "Eligibility status should either be Eligible, Cooldown, Deferred, Ineligible, or Unscreened, "
                    + "and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(eligible|cooldown|deferred|ineligible|unscreened)$";

    public final String value;

    /**
     * Constructs a {@code EligibilityStatus}.
     *
     * @param eligibilityStatus Status of the blood donor.
     */
    public EligibilityStatus(String eligibilityStatus) {
        requireNonNull(eligibilityStatus);
        System.out.println("Constructing EligibilityStatus with: '" + eligibilityStatus + "'");
        checkArgument(isValidEligibilityStatus(eligibilityStatus.toLowerCase()), MESSAGE_CONSTRAINTS);
        this.value = eligibilityStatus;
    }

    /**
     * Returns true if a given string is a valid blood type
     */
    public static boolean isValidEligibilityStatus(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object otherEligibilityStatus) {
        if (otherEligibilityStatus == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(otherEligibilityStatus instanceof EligibilityStatus)) {
            return false;
        }

        EligibilityStatus theStatus = (EligibilityStatus) otherEligibilityStatus;
        return value.equals(theStatus.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
