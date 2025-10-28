package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandFailure;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalDonationRecords.getTypicalBloodNet;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_DONATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import bloodnet.testutil.DonationRecordBuilder;
import bloodnet.testutil.EditDonationRecordsDescriptorBuilder;

public class EditDonationCommandTest {
    private final Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void constructor_validArguments_success() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        new EditDonationCommand(indexStub, descriptorStub);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        Index indexStub = Index.fromZeroBased(0);

        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand edit = new EditDonationCommand(indexStub, descriptorStub);
        assertThrows(NullPointerException.class, () -> edit.execute(null));
    }

    @Test
    public void execute_validArguments_success() throws Exception {
        DonationRecord editedDonationRecord = new DonationRecordBuilder().build();
        expectedModel.setDonationRecord(model.getFilteredDonationRecordList().get(0), editedDonationRecord);

        EditDonationRecordDescriptor editDonationRecordDescriptor = new EditDonationRecordsDescriptorBuilder(
                editedDonationRecord).build();

        Person personToEditRecordFor = model.getFilteredPersonList().stream()
                .filter(person -> person.getId().equals(editedDonationRecord.getPersonId()))
                .findFirst()
                .orElseThrow();

        EditDonationCommand editDonationCommand = new EditDonationCommand(INDEX_FIRST_DONATION,
                editDonationRecordDescriptor);

        String expectedMessage = String.format(
                EditDonationCommand.MESSAGE_EDIT_DONATION_RECORD_SUCCESS,
                Messages.format(editedDonationRecord, personToEditRecordFor));

        Model expectedModel = new ModelManager(new BloodNet(model.getBloodNet()), new UserPrefs());

        expectedModel.setDonationRecord(model.getFilteredDonationRecordList().get(0), editedDonationRecord);

        assertCommandSuccess(editDonationCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidDonationDate_throwsCommandException() throws Exception {
        // 16-05-2025 is invalid as it is one day after Alice's most recent donation (15-05-2025)
        String donationDateString = "16-05-2025";
        DonationRecord editedDonationRecord = new DonationRecordBuilder()
                                                    .withPersonId(DonationRecordBuilder.DEFAULT_PERSON.getId())
                                                    .withDonationDate(donationDateString)
                                                    .withBloodVolume(DonationRecordBuilder.DEFAULT_BLOOD_VOLUME)
                                                    .build();

        EditDonationRecordDescriptor editDonationRecordDescriptor = new EditDonationRecordsDescriptorBuilder(
                editedDonationRecord).build();

        EditDonationCommand editDonationCommand = new EditDonationCommand(INDEX_FIRST_DONATION,
                editDonationRecordDescriptor);


        Model expectedModel = new ModelManager(new BloodNet(model.getBloodNet()), new UserPrefs());

        expectedModel.setDonationRecord(model.getFilteredDonationRecordList().get(0), editedDonationRecord);
        String expectedMessage = EditDonationCommand.MESSAGE_CONCATENATED_VALIDATION_ERRORS_HEADER
                                + "\n- "
                                + String.format(DonationRecord.MESSAGE_NEIGHBOURING_DONATION_TOO_CLOSE,
                                                "15-05-2025",
                                                "15-05-2025",
                                                "06-08-2025");

        assertCommandFailure(editDonationCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateDonationRecord_failure() throws Exception {
        DonationRecord firstDonationRecord = model.getFilteredDonationRecordList()
                .get(INDEX_FIRST_DONATION.getZeroBased());
        EditDonationRecordDescriptor editDonationDescriptor = new EditDonationRecordsDescriptorBuilder(
                firstDonationRecord).build();

        EditDonationCommand editDonationCommand = new EditDonationCommand(INDEX_FIRST_DONATION, editDonationDescriptor);

        assertCommandFailure(editDonationCommand, model, EditDonationCommand.MESSAGE_DONATION_RECORD_ALREADY_EXISTS);
    }

    @Test
    public void execute_personIdIsNull_failure() throws Exception {
        Model model = new ModelManager();
        assertThrows(NullPointerException.class, ()
                -> model.addDonationRecord(new DonationRecordBuilder().withPersonId(null).build()));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        EditDonationCommand editedDonationRecord = new EditDonationCommand(indexStub,
                new EditDonationRecordDescriptor());
        assertEquals(editedDonationRecord, editedDonationRecord);
    }

    @Test
    public void equals_sameDescriptor_returnsTrue() {
        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        assertEquals(descriptorStub, descriptorStub);
    }

    @Test
    public void equals_sameValuesInDescriptor_returnsTrue() {

        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        EditDonationRecordDescriptor secondDescriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        secondDescriptorStub.setBloodVolume(bloodVolumeStub);
        secondDescriptorStub.setDonationDate(donationDateStub);

        assert(descriptorStub.equals(secondDescriptorStub));
    }

    @Test
    public void equals_sameValuesInCommand_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand editDonationCommandOne = new EditDonationCommand(indexStub, descriptorStub);
        EditDonationCommand editDonationCommandTwo = new EditDonationCommand(indexStub, descriptorStub);
        assert(editDonationCommandOne.equals(editDonationCommandTwo));
    }

    @Test
    public void equals_differentIndexInCommand_returnsFalse() {
        Index indexStub1 = Index.fromZeroBased(1);
        Index indexStub2 = Index.fromZeroBased(2);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand command1 = new EditDonationCommand(indexStub1, descriptorStub);
        EditDonationCommand command2 = new EditDonationCommand(indexStub2, descriptorStub);
        assertFalse(command1.equals(command2));
    }

    @Test
    public void toString_validCommand_returnsStringRepresentation() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand command = new EditDonationCommand(indexStub, descriptorStub);
        String result = command.toString();

        assert(result.contains("donationDate"));
        assert(result.contains("bloodVolume"));
    }
}
