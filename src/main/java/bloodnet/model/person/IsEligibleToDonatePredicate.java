package bloodnet.model.person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;


/**
 * Tests that a {@code Person} meets eligibility criteria based on {@code dateOfBirth}
 * and days since last donation, if they have donated in the past. .
 */
public class IsEligibleToDonatePredicate implements Predicate<Person> {

    private final Model model;
    /**
     * Constructs a {@code IsEligibleToDonatePredicate}.
     */
    public IsEligibleToDonatePredicate(Model model) {
        this.model = model;
    }

    /**
     * Returns true if the person's date of birth and days since last donation fits the criteria.
     * {@code dateOfBirth} is provided by the user, while donation date is assumed to be the current day.
     *
     * @param person Person you are checking the {@code dateOfBirth} and days since last donation for.
     */
    public boolean test(Person person) {
        LocalDate dateOfBirth = person.getDateOfBirth().getValue();
        Optional<DonationDate> lastDonationDate = model.getFilteredDonationRecordList().stream()
                .filter(donationRecord -> donationRecord.getPersonId()
                        .equals(person.getId()))
                .max(Comparator.comparing(donationRecord ->
                        donationRecord.getDonationDate().getValue()))
                .map(DonationRecord::getDonationDate);

        LocalDate currentDate = LocalDate.now();
        LocalDate dateForYoungestDonor = currentDate.minusYears(16);
        LocalDate dateForFirstTimeBloodDonor = currentDate.minusYears(61);
        LocalDate dateForOldestRepeatDonor = currentDate.minusYears(66);

        if (!dateOfBirth.isAfter(dateForYoungestDonor)) {
            if (!dateOfBirth.isAfter(dateForOldestRepeatDonor)
                    && lastDonationDate.isPresent() && lastDonationDate.get().getValue()
                    .plusYears(3).isBefore(currentDate)) {
                return false;
            }
            if (!dateOfBirth.isAfter(dateForFirstTimeBloodDonor) && lastDonationDate.isEmpty()) {
                return false;
            }
            return lastDonationDate.isEmpty() || !lastDonationDate.get().getValue()
                    .plusDays(84).isAfter(currentDate);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls or if it is of a different type
        return other instanceof IsEligibleToDonatePredicate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
