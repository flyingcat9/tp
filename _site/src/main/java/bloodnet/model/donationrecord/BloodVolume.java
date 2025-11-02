package bloodnet.model.donationrecord;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents the volume of blood donated during a blood donation session in the bloodnet.
 * Guarantees: immutable; is valid as declared in {@link #isValidBloodVolume(String)}
 */
public class BloodVolume {

    public static final String MESSAGE_CONSTRAINTS =
            "Blood volume should be a positive whole number less than 500 millilitres, as per the official guidelines.";

    public final Integer volume;

    /**
     * Constructs a {@code Name}.
     *
     * @param volume A valid volume.
     */
    public BloodVolume(String volume) {
        requireNonNull(volume);
        checkArgument(isValidBloodVolume(volume), MESSAGE_CONSTRAINTS);
        this.volume = Integer.parseInt(volume);
    }

    /**
     * Returns true if the blood volume is valid.
     */
    public static boolean isValidBloodVolume(String test) {
        if (test == null) {
            throw new NullPointerException();
        }

        // Check that the string can be parsed into an integer
        try {
            Integer i = Integer.parseInt(test);
            // Ensure that the blood volume is not 0 or less than 0
            if (i <= 0) {
                return false;
            }
            if (i >= 500) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // If it can, it is a valid volume.
        // Note: We may chose to apply further validation in the future,
        // such as defining upper and lower bounds on the valid volumes.
        return true;
    }


    @Override
    public String toString() {
        return volume.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BloodVolume)) {
            return false;
        }

        BloodVolume otherBloodVolume = (BloodVolume) other;
        return volume.equals(otherBloodVolume.volume);
    }

    @Override
    public int hashCode() {
        return volume.hashCode();
    }

}
