package bloodnet.model.donationrecord;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.UUID;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Represents a Donation Record in BloodNet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class DonationRecord {
    // Identity fields
    private UUID id;
    private final UUID personId;
    private final DonationDate donationDate;

    // Data fields
    private final BloodVolume bloodVolume;

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
