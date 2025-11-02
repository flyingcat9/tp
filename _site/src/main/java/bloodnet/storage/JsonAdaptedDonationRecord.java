package bloodnet.storage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * Jackson-friendly version of {@link DonationRecord}.
 */
class JsonAdaptedDonationRecord {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Donation Record's %s field is missing!";

    private final String id;
    private final String personId;
    private final String donationDate;
    private final String bloodVolume;

    /**
     * Constructs a {@code JsonAdaptedDonationRecord} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedDonationRecord(@JsonProperty("id") String id, @JsonProperty("personId") String personId,
                                     @JsonProperty("donationDate") String donationDate,
                                     @JsonProperty("bloodVolume") String bloodVolume) {
        this.id = id;
        this.personId = personId;
        this.donationDate = donationDate;
        this.bloodVolume = bloodVolume;
    }

    /**
     * Converts a given {@code DonationRecord} into this class for Jackson use.
     */
    public JsonAdaptedDonationRecord(DonationRecord source) {
        id = source.getId().toString();
        personId = source.getPersonId().toString();
        donationDate = source.getDonationDate().toString();
        bloodVolume = source.getBloodVolume().toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code DonationRecord} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public DonationRecord toModelType() throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "ID"));
        }
        final UUID modelId = UUID.fromString(id);

        if (personId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "PersonID"));
        }

        // TODO: add validation to check if the personId exists in the persons table.
        final UUID modelPersonId = UUID.fromString(personId);

        if (donationDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    DonationDate.class.getSimpleName()));
        }
        if (!DonationDate.isValidDonationDate(donationDate)) {
            throw new IllegalValueException(DonationDate.MESSAGE_CONSTRAINTS);
        }
        final DonationDate modelDonationDate = new DonationDate(donationDate);

        if (bloodVolume == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    BloodVolume.class.getSimpleName()));
        }
        if (!BloodVolume.isValidBloodVolume(bloodVolume)) {
            throw new IllegalValueException(BloodVolume.MESSAGE_CONSTRAINTS);
        }
        final BloodVolume modelBloodVolume = new BloodVolume(bloodVolume);


        return new DonationRecord(modelId, modelPersonId, modelDonationDate, modelBloodVolume);
    }

}
