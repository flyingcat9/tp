package bloodnet.model.donationrecord;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_VOLUME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DONATION_DATE_BOB;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalDonationRecords.ALICE_DONATION_RECORD;
import static bloodnet.testutil.TypicalDonationRecords.BENSON_DONATION_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.CommandTestUtil;
import bloodnet.model.donationrecord.exceptions.DonationRecordNotFoundException;
import bloodnet.model.donationrecord.exceptions.DuplicateDonationRecordException;
import bloodnet.testutil.DonationRecordBuilder;

public class UniqueDonationRecordListTest {

    private final UniqueDonationRecordList uniqueDonationRecordList = new UniqueDonationRecordList();

    @Test
    public void contains_nullDonationRecord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList.contains(null));
    }

    @Test
    public void contains_donationRecordNotInList_returnsFalse() {
        assertFalse(uniqueDonationRecordList.contains(ALICE_DONATION_RECORD));
    }

    @Test
    public void contains_donationRecordInList_returnsTrue() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        assertTrue(uniqueDonationRecordList.contains(ALICE_DONATION_RECORD));
    }

    @Test
    public void contains_donationRecordWithSameIdentityFieldsInList_returnsTrue() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        DonationRecord editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withBloodVolume(VALID_BLOOD_VOLUME_AMY)
                .build();
        assertTrue(uniqueDonationRecordList.contains(editedAlice));
    }

    @Test
    public void add_nullDonationRecord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList.add(null));
    }

    @Test
    public void add_duplicateDonationRecord_throwsDuplicateDonationRecordException() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        assertThrows(DuplicateDonationRecordException.class, () -> uniqueDonationRecordList
                .add(ALICE_DONATION_RECORD));
    }

    @Test
    public void setDonationRecord_nullTargetDonationRecord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList
                .setDonationRecord(null, ALICE_DONATION_RECORD));
    }

    @Test
    public void setDonationRecord_nullEditedDonationRecord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList
                .setDonationRecord(ALICE_DONATION_RECORD, null));
    }

    @Test
    public void setDonationRecord_targetDonationRecordNotInList_throwsDonationRecordNotFoundException() {
        assertThrows(DonationRecordNotFoundException.class, () -> uniqueDonationRecordList
                .setDonationRecord(ALICE_DONATION_RECORD, ALICE_DONATION_RECORD));
    }

    @Test
    public void setDonationRecord_editedDonationRecordIsSameDonationRecord_success() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        uniqueDonationRecordList.setDonationRecord(ALICE_DONATION_RECORD, ALICE_DONATION_RECORD);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        expectedUniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecord_editedDonationRecordHasSameIdentity_success() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        DonationRecord editedAlice = new DonationRecordBuilder(ALICE_DONATION_RECORD)
                .withDonationDate(VALID_DONATION_DATE_BOB).withBloodVolume(CommandTestUtil.VALID_BLOOD_VOLUME_BOB)
                .build();
        uniqueDonationRecordList.setDonationRecord(ALICE_DONATION_RECORD, editedAlice);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        expectedUniqueDonationRecordList.add(editedAlice);
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecord_editedDonationRecordHasDifferentIdentity_success() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        uniqueDonationRecordList.setDonationRecord(ALICE_DONATION_RECORD, BENSON_DONATION_RECORD);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        expectedUniqueDonationRecordList.add(BENSON_DONATION_RECORD);
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecord_editedDonationRecordHasNonUniqueIdentity_throwsDuplicateDonationRecordException() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        uniqueDonationRecordList.add(BENSON_DONATION_RECORD);
        assertThrows(DuplicateDonationRecordException.class, () -> uniqueDonationRecordList.setDonationRecord(
                ALICE_DONATION_RECORD, BENSON_DONATION_RECORD));
    }

    @Test
    public void remove_nullDonationRecord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList.remove(null));
    }

    @Test
    public void remove_donationRecordDoesNotExist_throwsDonationRecordNotFoundException() {
        assertThrows(DonationRecordNotFoundException.class, () -> uniqueDonationRecordList
                .remove(ALICE_DONATION_RECORD));
    }

    @Test
    public void remove_existingDonationRecord_removesDonationRecord() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        uniqueDonationRecordList.remove(ALICE_DONATION_RECORD);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecords_nullUniqueDonationRecordList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList
                .setDonationRecords((UniqueDonationRecordList) null));
    }

    @Test
    public void setDonationRecords_uniqueDonationRecordList_replacesOwnListWithProvidedUniqueDonationRecordList() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        expectedUniqueDonationRecordList.add(BENSON_DONATION_RECORD);
        uniqueDonationRecordList.setDonationRecords(expectedUniqueDonationRecordList);
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecords_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDonationRecordList
                .setDonationRecords((List<DonationRecord>) null));
    }

    @Test
    public void setDonationRecords_list_replacesOwnListWithProvidedList() {
        uniqueDonationRecordList.add(ALICE_DONATION_RECORD);
        List<DonationRecord> donationRecordList = Collections.singletonList(BENSON_DONATION_RECORD);
        uniqueDonationRecordList.setDonationRecords(donationRecordList);
        UniqueDonationRecordList expectedUniqueDonationRecordList = new UniqueDonationRecordList();
        expectedUniqueDonationRecordList.add(BENSON_DONATION_RECORD);
        assertEquals(expectedUniqueDonationRecordList, uniqueDonationRecordList);
    }

    @Test
    public void setDonationRecords_listWithDuplicateDonationRecords_throwsDuplicateDonationRecordException() {
        List<DonationRecord> listWithDuplicateDonationRecords = Arrays.asList(ALICE_DONATION_RECORD,
                ALICE_DONATION_RECORD);
        assertThrows(DuplicateDonationRecordException.class, () -> uniqueDonationRecordList
                .setDonationRecords(listWithDuplicateDonationRecords));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> uniqueDonationRecordList
                .asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueDonationRecordList.asUnmodifiableObservableList().toString(),
                uniqueDonationRecordList.toString());
    }
}
