package bloodnet.model.person;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's blood type in the donor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBloodType(String)}
 */
public class BloodType {

    public static final String MESSAGE_CONSTRAINTS =
            "Blood type should either be A-, A+, B-, B+, AB-, AB+, O- or O+, and it should not be blank";

    /*
     * The first character of the blood type must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(A|B|AB|O)[+-]$";

    public final String value;

    /**
     * Constructs a {@code BloodType}.
     *
     * @param bloodType A valid blood type.
     */
    public BloodType(String bloodType) {
        requireNonNull(bloodType);
        checkArgument(isValidBloodType(bloodType), MESSAGE_CONSTRAINTS);
        this.value = bloodType;
    }

    /**
     * Returns true if a given string is a valid blood type
     */
    public static boolean isValidBloodType(String test) {
        return test.toUpperCase().matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BloodType)) {
            return false;
        }

        BloodType otherBloodType = (BloodType) other;
        return value.equals(otherBloodType.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
