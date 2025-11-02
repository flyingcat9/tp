package bloodnet.model.donationrecord;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * Represents a Donation Record in BloodNet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class DonationRecord {
    // Static validation error strings
    public static final String MESSAGE_AGE_BELOW_16 =
            "Donor is too young on the donation date. "
            + "Donation date cannot be between %s (their birthdate) "
            + "and %s (one day before their 16th birthdate) inclusive.";
    public static final String MESSAGE_NEIGHBOURING_DONATION_TOO_CLOSE =
            "Consecutive donations must be at least 12 weeks (84 days) apart. "
            + "However, the donor has already donated blood on %s. "
            + "Therefore, the donation date cannot be between %s and %s inclusive.";
    public static final String MESSAGE_FIRST_TIME_DONOR_TOO_OLD =
            "This is a first-time donor. "
            + "Donation date cannot be on or after %s (their 61st birthdate).";
    public static final String MESSAGE_NON_RECENT_DONOR_TOO_OLD =
            "This is a repeated donor. However, they have not donated blood in the last 3 years. "
            + "Donation date cannot be on or after %s (their 66th birthdate).";

    // Identity fields
    private UUID id;
    private final UUID personId;
    private final DonationDate donationDate;

    // Data fields
    private final BloodVolume bloodVolume;

    // Fields which aren't actually stored in the database, but displayed
    // in the UI
    private String donorName;
    private String donorPhoneNumber;

    /**
     * Every field other than ID must be present and not null.
     */
    public DonationRecord(UUID id, UUID personId, DonationDate donationDate, BloodVolume bloodVolume) {
        requireAllNonNull(personId, donationDate, bloodVolume);
        this.id = id;

        this.personId = personId;
        this.donationDate = donationDate;
        this.bloodVolume = bloodVolume;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public DonationDate getDonationDate() {
        return donationDate;
    }

    public BloodVolume getBloodVolume() {
        return bloodVolume;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorPhoneNumber(String donorPhoneNumber) {
        this.donorPhoneNumber = donorPhoneNumber;
    }

    public String getDonorPhoneNumber() {
        return donorPhoneNumber;
    }

    /**
     * Returns true if both records have the same date and personId.
     * This defines a weaker notion of equality between two donation records.
     */
    public boolean isSameDonationRecord(DonationRecord otherDonationRecord) {
        if (otherDonationRecord == this) {
            return true;
        }

        return otherDonationRecord != null
                && (otherDonationRecord.getDonationDate().equals(getDonationDate())
                && otherDonationRecord.getPersonId().equals(getPersonId()));
    }

    /**
     * Validates this donation record using the below criteria.
     * Only uses the {@code personId} and {@code donationDate} fields of this
     * {@code DonationRecord} object.
     * Does not use the {@code id} and {@code bloodVolume} fields.
     *
     * <p>
     * Criteria for invalid donation record:
     * </p>
     * <ul>
     * <li>Age of person, at the date of donationDate, is < 16</li>
     *
     * <li>Number of days between the predecessor donation record (if it exists) and
     * the donationDate is strictly less than 84. That is, if the predecessor donation
     * record is the 0th date, then return false if donationDate is the 83rd date or
     * lower.</li>
     *
     * <li>Number of days between the successor donation record (if it exists) and
     * the donationDate is strictly less than 84. That is, if the donationDate is the
     * 0th date, then return false if successor donation record is the 83rd date or
     * lower.</li>
     *
     * <li>If person is a first-time blood donor (ie, there exists no predecessor donation
     * record for that donor) AND donationDate >= 61st birthdate of the person.</li>
     *
     * <li>If person is not a first-time blood donor (ie, there exists some predecessor
     * donation record for that donor) AND person has not donated in the last 3 years
     * AND donationDate >= 66th birthdate of the person.</li>
     * </ul>
     *
     * @return an {@code ArrayList} containing a list of validation error strings
     */
    public ArrayList<String> validate(ObservableList<Person> fullPersonList,
                                      ObservableList<DonationRecord> fullDonationRecordList) {
        ArrayList<String> validationErrorStrings = new ArrayList<>();

        // We try to resolve the personId to a person by
        // checking against the FULL personList in the BloodNet,
        // not just against the filteredPersonList.

        // This is to allow this method to be used
        // by the isEligibleToDonate predicate,
        // which may create a DonationRecord object
        // with a personId that is not in the filteredPersonList.
        Person person = fullPersonList.stream()
                .filter(p -> p.getId().equals(personId))
                .findFirst()
                .orElseThrow();

        LocalDate donationDateValue = donationDate.getValue();
        LocalDate dateOfBirthValue = person.getDateOfBirth().getValue();

        // 1. Age of person at donationDate must be >= 16
        int ageAtDonation = Period.between(dateOfBirthValue, donationDateValue).getYears();
        if (ageAtDonation < 16) {
            LocalDate sixteenthBirthday = dateOfBirthValue.plusYears(16);
            String errorString = String.format(MESSAGE_AGE_BELOW_16,
                                                dateOfBirthValue.format(DateOfBirth.DATE_FORMATTER),
                                                sixteenthBirthday.minusDays(1).format(DonationDate.DATE_FORMATTER));
            validationErrorStrings.add(errorString);
        }

        // If .validate() is called when trying to edit a donation record,
        // we want to filter fullDonationRecordList to only include those records that are
        // not the record that is currently being edited.

        // However, if .validate() is called by IsEligibleToDonatePredicate#test(Person)
        // or by AddDonationCommand#execute(),
        // we do not want to impose the above-mentioned filter.

        // If this.getId() == null, this method
        // is being called by either IsEligibleToDonatePredicate#test(Person)
        // or AddDonationCommand#execute().
        boolean isDonationRecordIdNull = this.getId() == null;

        // Find predecessor (last donation before donationDate)
        Optional<DonationDate> predecessorDonationDateOptional = fullDonationRecordList.stream()
                .filter(donationRecord -> donationRecord.getPersonId().equals(person.getId()))
                .filter(donationRecord -> isDonationRecordIdNull || !donationRecord.getId().equals(this.getId()))
                .map(DonationRecord::getDonationDate)
                .filter(donationDate -> !donationDate.getValue().isAfter(donationDateValue))
                .max(Comparator.comparing(DonationDate::getValue));

        // Find successor (first donation after donationDate)
        Optional<DonationDate> successorDonationDateOptional = fullDonationRecordList.stream()
                .filter(donationRecord -> donationRecord.getPersonId().equals(person.getId()))
                .filter(donationRecord -> isDonationRecordIdNull || !donationRecord.getId().equals(this.getId()))
                .map(DonationRecord::getDonationDate)
                .filter(donationDate -> !donationDate.getValue().isBefore(donationDateValue))
                .min(Comparator.comparing(DonationDate::getValue));

        // 2. Days between predecessor and donationDate must be >= 84
        if (predecessorDonationDateOptional.isPresent()) {
            LocalDate predecessorDonationDate = predecessorDonationDateOptional.get().getValue();
            long daysSinceLastDonation = ChronoUnit.DAYS.between(predecessorDonationDate,
                    donationDateValue);
            if (daysSinceLastDonation < 84) {
                String errorString = String.format(MESSAGE_NEIGHBOURING_DONATION_TOO_CLOSE,
                                                    predecessorDonationDate.format(DonationDate.DATE_FORMATTER),
                                                    predecessorDonationDate.format(DonationDate.DATE_FORMATTER),
                                                    predecessorDonationDate.plusDays(83)
                                                                           .format(DonationDate.DATE_FORMATTER));
                validationErrorStrings.add(errorString);
            }
        }

        // 3. Days between donationDate and successor must be >= 84
        if (successorDonationDateOptional.isPresent()) {
            LocalDate successorDonationDate = successorDonationDateOptional.get().getValue();
            long daysToNextDonation = ChronoUnit.DAYS.between(donationDateValue,
                    successorDonationDate);
            if (daysToNextDonation < 84) {
                String errorString = String.format(MESSAGE_NEIGHBOURING_DONATION_TOO_CLOSE,
                                                    successorDonationDate.format(DonationDate.DATE_FORMATTER),
                                                    successorDonationDate.minusDays(83)
                                                                         .format(DonationDate.DATE_FORMATTER),
                                                    successorDonationDate.format(DonationDate.DATE_FORMATTER));
                validationErrorStrings.add(errorString);
            }
        }

        // 4. Person is a first-time blood donor and donationDate >= 61st birthday
        LocalDate sixtyFirstBirthday = dateOfBirthValue.plusYears(61);
        if (predecessorDonationDateOptional.isEmpty() && !donationDateValue.isBefore(sixtyFirstBirthday)) {
            String errorMessage = String.format(MESSAGE_FIRST_TIME_DONOR_TOO_OLD, sixtyFirstBirthday.format(
                    DonationDate.DATE_FORMATTER));
            validationErrorStrings.add(errorMessage);
        }

        // 5. Person not first-time blood donor AND not donated in last 3 years AND donationDate >= 66th birthday
        LocalDate sixtySixthBirthday = dateOfBirthValue.plusYears(66);
        if (predecessorDonationDateOptional.isPresent()) {
            LocalDate lastDonation = predecessorDonationDateOptional.get().getValue();
            if (!lastDonation.plusYears(3).isAfter(donationDateValue)
                    && !donationDateValue.isBefore(sixtySixthBirthday)) {
                String errorMessage = String.format(MESSAGE_NON_RECENT_DONOR_TOO_OLD, sixtySixthBirthday.format(
                        DonationDate.DATE_FORMATTER));
                validationErrorStrings.add(errorMessage);
            }
        }
        return validationErrorStrings;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DonationRecord)) {
            return false;
        }

        DonationRecord otherDonationRecord = (DonationRecord) other;
        return personId.equals(otherDonationRecord.personId)
                && donationDate.equals(otherDonationRecord.donationDate)
                && bloodVolume.equals(otherDonationRecord.bloodVolume);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, personId, donationDate, bloodVolume);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("personId", personId)
                .add("donationDate", donationDate)
                .add("bloodVolume", bloodVolume)
                .toString();
    }

}
