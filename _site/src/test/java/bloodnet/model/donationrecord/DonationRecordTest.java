package bloodnet.model.donationrecord;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_VOLUME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DONATION_DATE_BOB;
import static bloodnet.testutil.TypicalDonationRecords.ALICE_DONATION_RECORD;
import static bloodnet.testutil.TypicalPersons.BENSON;
import static bloodnet.testutil.TypicalPersons.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.testutil.DonationRecordBuilder;

public class DonationRecordTest {

    @Test
    public void isSameDonationRecord() {
        // same object -> returns true
        assertTrue(ALICE_DONATION_RECORD.isSameDonationRecord(ALICE_DONATION_RECORD));

        // null -> returns false
        assertFalse(ALICE_DONATION_RECORD.isSameDonationRecord(null));

        // same personId, all other attributes different -> returns false
        DonationRecord editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withDonationDate(VALID_DONATION_DATE_BOB).withBloodVolume(VALID_BLOOD_VOLUME_BOB)
                .build();
        assertFalse(ALICE_DONATION_RECORD.isSameDonationRecord(editedAlice));

        // same donation date, all other attributes different -> returns false
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD).withPersonId(BENSON.getId())
                .withBloodVolume(VALID_BLOOD_VOLUME_BOB)
                .build();
        assertFalse(ALICE_DONATION_RECORD.isSameDonationRecord(editedAlice));

        // same personId and same donation date, all other attributes different -> return true
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD).withBloodVolume(VALID_BLOOD_VOLUME_BOB)
                .build();
        assertTrue(ALICE_DONATION_RECORD.isSameDonationRecord(editedAlice));

        // different personId and donation date, all other attributes same -> return false
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD).withPersonId(BENSON.getId())
                .withDonationDate(VALID_DONATION_DATE_BOB).build();
        assertFalse(ALICE_DONATION_RECORD.isSameDonationRecord(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        DonationRecord aliceCopy = new DonationRecordBuilder(ALICE_DONATION_RECORD).build();
        assertTrue(ALICE_DONATION_RECORD.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE_DONATION_RECORD.equals(ALICE_DONATION_RECORD));

        // null -> returns false
        assertFalse(ALICE_DONATION_RECORD.equals(null));

        // different type -> returns false
        assertFalse(ALICE_DONATION_RECORD.equals(2));

        // different donationRecord -> returns false
        assertFalse(ALICE_DONATION_RECORD.equals(BOB));

        // different personId -> returns false
        DonationRecord editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withPersonId(BENSON.getId()).build();
        assertFalse(ALICE_DONATION_RECORD.equals(editedAlice));

        // different donationDate -> returns false
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withDonationDate(VALID_DONATION_DATE_BOB).build();
        assertFalse(ALICE_DONATION_RECORD.equals(editedAlice));

        // different bloodVolume -> returns false
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withBloodVolume(VALID_BLOOD_VOLUME_BOB).build();
        assertFalse(ALICE_DONATION_RECORD.equals(editedAlice));

        // testing two different hash codes
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withBloodVolume(VALID_BLOOD_VOLUME_BOB).build();
        assertNotEquals(ALICE_DONATION_RECORD.hashCode(), editedAlice.hashCode());

        // testing the same hash
        editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD).build();
        assertEquals(ALICE_DONATION_RECORD.hashCode(), ALICE_DONATION_RECORD.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = DonationRecord.class.getCanonicalName()
                + "{id=" + ALICE_DONATION_RECORD.getId()
                + ", personId=" + ALICE_DONATION_RECORD.getPersonId()
                + ", donationDate=" + ALICE_DONATION_RECORD.getDonationDate()
                + ", bloodVolume=" + ALICE_DONATION_RECORD.getBloodVolume()
                + "}";
        assertEquals(expected, ALICE_DONATION_RECORD.toString());
    }
}
