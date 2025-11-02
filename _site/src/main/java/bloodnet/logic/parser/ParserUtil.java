package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_DATE_OF_BIRTH_TOO_OLD;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.StringUtil;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Phone;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String bloodType} into an {@code BloodType}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code bloodType} is invalid.
     */
    public static BloodType parseBloodType(String bloodType) throws ParseException {
        requireNonNull(bloodType);
        String trimmedBloodType = bloodType.trim();
        if (!BloodType.isValidBloodType(trimmedBloodType)) {
            throw new ParseException(BloodType.MESSAGE_CONSTRAINTS);
        }
        return new BloodType(trimmedBloodType);
    }

    /**
     * Parses a {@code String date of birth} into a {@code Date of Birth}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date of birth} is invalid.
     */
    public static DateOfBirth parseDateOfBirth(String dateOfBirth) throws ParseException {
        requireNonNull(dateOfBirth);
        String trimmedDateOfBirth = dateOfBirth.trim();
        if (!DateOfBirth.isValidDateOfBirth(trimmedDateOfBirth)) {
            String formattedMessage = String.format(
                    MESSAGE_DATE_OF_BIRTH_TOO_OLD,
                    LocalDate.now().minusYears(130).format(DateOfBirth.DATE_FORMATTER));
            throw new ParseException(formattedMessage);
        }
        return new DateOfBirth(trimmedDateOfBirth);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String donationDate} into a {@code DonationDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code donationDate} is invalid.
     */
    public static DonationDate parseDonationDate(String donationDate) throws ParseException {
        requireNonNull(donationDate);
        String trimmedDonationDate = donationDate.trim();
        if (!DonationDate.isValidDonationDate(trimmedDonationDate)) {
            throw new ParseException(DonationDate.MESSAGE_CONSTRAINTS);
        }
        return new DonationDate(trimmedDonationDate);
    }

    /**
     * Parses a {@code String bloodVolume} into a {@code BloodVolume}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code bloodVolume} is invalid.
     */
    public static BloodVolume parseBloodVolume(String bloodVolume) throws ParseException {
        requireNonNull(bloodVolume);
        String trimmedBloodVolume = bloodVolume.trim();
        if (!BloodVolume.isValidBloodVolume(trimmedBloodVolume)) {
            throw new ParseException(BloodVolume.MESSAGE_CONSTRAINTS);
        }
        return new BloodVolume(trimmedBloodVolume);
    }
}
