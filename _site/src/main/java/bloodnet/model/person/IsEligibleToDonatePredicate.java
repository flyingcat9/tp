package bloodnet.model.person;

import java.util.ArrayList;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;


/**
 * Tests that a {@code Person} meets eligibility criteria based on {@code dateOfBirth}
 * and days since last donation, if they have donated in the past.
 */
public class IsEligibleToDonatePredicate implements Predicate<Person> {

    private final Model model;
    private final DonationDate donationDate;

    /**
     * Constructs a {@code IsEligibleToDonatePredicate}.
     * Sets {@code donationDate} to the specified {@code donationDate}.
     */
    public IsEligibleToDonatePredicate(Model model, DonationDate donationDate) {
        this.model = model;
        this.donationDate = donationDate;
    }

    /**
     * Returns true if the person's date of birth and days since last donation fits the criteria.
     * {@code dateOfBirth} is provided by the user.
     *
     * @param person Person you are checking the {@code dateOfBirth} and days since last donation for.
     */
    public boolean test(Person person) {
        // Using a dummy blood volume, as we want to reuse DonationRecord's .validate() method.
        DonationRecord donationRecord = new DonationRecord(null,
                                                            person.getId(),
                                                            donationDate,
                                                            new BloodVolume("1"));
        ArrayList<String> validationErrorStrings = donationRecord.validate(model.getBloodNet().getPersonList(),
                                                                           model.getBloodNet().getDonationRecordList());
        return validationErrorStrings.isEmpty();
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
