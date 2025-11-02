package bloodnet.storage;

import static bloodnet.storage.JsonAdaptedDonationRecord.MISSING_FIELD_MESSAGE_FORMAT;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalDonationRecords.BENSON_DONATION_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;

public class JsonAdaptedDonationRecordTest {
    private static final String INVALID_DONATION_DATE = "20/20/2222";
    private static final String INVALID_BLOOD_VOLUME = "example.com";

    private static final String VALID_UUID = BENSON_DONATION_RECORD.getId().toString();
    private static final String VALID_PERSON_ID = BENSON_DONATION_RECORD.getPersonId().toString();
    private static final String VALID_DONATION_DATE = BENSON_DONATION_RECORD.getDonationDate().toString();
    private static final String VALID_BLOOD_VOLUME = BENSON_DONATION_RECORD.getBloodVolume().toString();

    @Test
    public void toModelType_validDonationRecordDetails_returnsDonationRecord() throws Exception {
        JsonAdaptedDonationRecord donationRecord = new JsonAdaptedDonationRecord(BENSON_DONATION_RECORD);
        assertEquals(BENSON_DONATION_RECORD, donationRecord.toModelType());
    }

    @Test
    public void toModelType_nullPersonId_throwsIllegalValueException() {
        JsonAdaptedDonationRecord donationRecord = new JsonAdaptedDonationRecord(VALID_UUID, null,
                VALID_DONATION_DATE, VALID_BLOOD_VOLUME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "PersonID");
        assertThrows(IllegalValueException.class, expectedMessage, donationRecord::toModelType);
    }

    @Test
    public void toModelType_invalidDonationDate_throwsIllegalValueException() {
        JsonAdaptedDonationRecord donationRecord =
                new JsonAdaptedDonationRecord(VALID_UUID, VALID_PERSON_ID, INVALID_DONATION_DATE, VALID_BLOOD_VOLUME);
        String expectedMessage = DonationDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, donationRecord::toModelType);
    }

    @Test
    public void toModelType_nullDonationDate_throwsIllegalValueException() {
        JsonAdaptedDonationRecord donationRecord = new JsonAdaptedDonationRecord(VALID_UUID, VALID_PERSON_ID,
                null, VALID_BLOOD_VOLUME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DonationDate.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, donationRecord::toModelType);
    }

    @Test
    public void toModelType_invalidBloodVolume_throwsIllegalValueException() {
        JsonAdaptedDonationRecord donationRecord =
                new JsonAdaptedDonationRecord(VALID_UUID, VALID_PERSON_ID, VALID_DONATION_DATE, INVALID_BLOOD_VOLUME);
        String expectedMessage = BloodVolume.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, donationRecord::toModelType);
    }

    @Test
    public void toModelType_nullBloodVolume_throwsIllegalValueException() {
        JsonAdaptedDonationRecord donationRecord = new JsonAdaptedDonationRecord(VALID_UUID, VALID_PERSON_ID,
                VALID_DONATION_DATE, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, BloodVolume.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, donationRecord::toModelType);
    }

}
