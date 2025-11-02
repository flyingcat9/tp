package bloodnet.model.donationrecord;

import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.person.Person;

/**
 * Tests a {@code DonationRecord}'s {@code personId}' matches the {@code id}
 * of the {@code Person} given.
 */
public class DonorIsSamePersonPredicate implements Predicate<DonationRecord> {
    private final Person personToFindDonationRecordsOf;

    public DonorIsSamePersonPredicate(Person personToFindDonationRecordsOf) {
        this.personToFindDonationRecordsOf = personToFindDonationRecordsOf;
    }

    @Override
    public boolean test(DonationRecord donationRecord) {
        return donationRecord.getPersonId().equals(personToFindDonationRecordsOf.getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DonorIsSamePersonPredicate)) {
            return false;
        }

        DonorIsSamePersonPredicate otherDonorIsSameKeywordsPredicate = (DonorIsSamePersonPredicate) other;
        return personToFindDonationRecordsOf.equals(otherDonorIsSameKeywordsPredicate.personToFindDonationRecordsOf);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("person", Messages.format(personToFindDonationRecordsOf)).toString();
    }
}
