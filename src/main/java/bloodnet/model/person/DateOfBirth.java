package bloodnet.model.person;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.time.format.ResolverStyle.STRICT;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Person's dateOfBirth (DD-MM-YYYY) in BloodNet
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    public static final String MESSAGE_CONSTRAINTS =
            "The date of birth should be of the format DD-MM-YYYY, not in the future,"
                    + " and not more than 130 years ago from today.";
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);

    public final LocalDate value;

    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDateOfBirth(dateOfBirth), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    }

    /**
     * Returns if a given string is a valid dateOfBirth.
     */
    public static boolean isValidDateOfBirth(String test) {
        try {
            LocalDate date = LocalDate.parse(test, DATE_FORMATTER);
            LocalDate current = LocalDate.now();
            return !date.isAfter(current)
                    && !date.isBefore(current.minusYears(130));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getValue() {
        return value;
    }

    /**
     * Formats the date as the same format as inputted ie: DD-MM-YYYY.
     */
    @Override
    public String toString() {
        return value.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDateOfBirth = (DateOfBirth) other;
        return this.getValue().equals(otherDateOfBirth.getValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

