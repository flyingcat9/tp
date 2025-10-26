package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.CollectionUtil;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;


/**
 * Allows users to edit the blood donation records of an existing person in BloodNet.
 */
public class EditDonationCommand extends Command {

    public static final String COMMAND_WORD = "editdonation";

    public static final String DESCRIPTION = "Edits the donation record identified "
            + "by the index number used in the displayed donation record list.";

    public static final String EXAMPLE = "Example: editdonation 1 v/100 d/02-02-2002";

    public static final String MESSAGE_EDIT_DONATION_RECORD_SUCCESS = "Edited Donation Record: %1$s";

    public static final String MESSAGE_DUPLICATE_DONATION_RECORD =
            "No change to the donation record.";

    static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    public static final String PARAMETERS = "Parameters: INDEX (must be a positive integer) \"\n" +
            "            + PREFIX_DONATION_DATE + \"DONATION DATE (DD-MM-YYYY) \"\n" +
            "            + PREFIX_BLOOD_VOLUME + \"BLOOD VOLUME (IN MILLILITRES)";

    private final Index indexOfDonationRecord;
    private final EditDonationRecordDescriptor editDonationRecordDescriptor;

    /**
     * @param indexOfDonationRecord of the donation record in the donation record list
     * @param editDonationRecordDescriptor details related to the edits made to the donation record
     */
    public EditDonationCommand(Index indexOfDonationRecord,
                               EditDonationRecordDescriptor editDonationRecordDescriptor) {
        requireNonNull(indexOfDonationRecord);
        requireNonNull(editDonationRecordDescriptor);
        this.indexOfDonationRecord = indexOfDonationRecord;
        this.editDonationRecordDescriptor = new EditDonationRecordDescriptor(editDonationRecordDescriptor);
    }

    public static String getMessageUsage() {
        return COMMAND_WORD + ": " + DESCRIPTION + "\n" + PARAMETERS + "\n" + EXAMPLE;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        DonationRecord recordToEdit = getDonationRecordToEdit(model);
        Person personToEditRecordFor = getPersonToEditRecordFor(model, recordToEdit);
        UUID personId = personToEditRecordFor.getId();
        assert personId != null;
        DonationRecord editedDonationRecord = createEditedDonationRecord(recordToEdit, editDonationRecordDescriptor);
        if (recordToEdit.equals(editedDonationRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_DONATION_RECORD);
        }
        model.setDonationRecord(recordToEdit, editedDonationRecord);
        return new CommandResult(String.format(MESSAGE_EDIT_DONATION_RECORD_SUCCESS,
                Messages.format(editedDonationRecord, personToEditRecordFor)));
    }

    /**
     * Creates and returns a {@code DonationRecord} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static DonationRecord createEditedDonationRecord(
            DonationRecord donationRecordToEdit, EditDonationRecordDescriptor editDonationRecordDescriptor) {
        DonationDate updatedDonationDate =
                editDonationRecordDescriptor.getDonationDate().orElse(donationRecordToEdit.getDonationDate());
        BloodVolume updatedBloodVolume =
                editDonationRecordDescriptor.getBloodVolume().orElse(donationRecordToEdit.getBloodVolume());
        return new DonationRecord(donationRecordToEdit.getId(),
                donationRecordToEdit.getPersonId(), updatedDonationDate, updatedBloodVolume);
    }

    private DonationRecord getDonationRecordToEdit(Model model) throws CommandException {
        requireNonNull(model);
        List<DonationRecord> donationRecordList = model.getFilteredDonationRecordList();
        if (indexOfDonationRecord.getZeroBased() >= donationRecordList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DONATION_DISPLAYED_INDEX);
        }
        return donationRecordList.get(indexOfDonationRecord.getZeroBased());
    }

    /**
     * Retrieves the {@code Person} corresponding to the {@code personId} of provided {@code donationRecord}
     */
    private Person getPersonToEditRecordFor(Model model, DonationRecord donationRecord) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();
        Optional<Person> optionalPerson = personList.stream()
                .filter(person -> person.getId().equals(donationRecord.getPersonId())).findFirst();

        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDonationCommand)) {
            return false;
        }

        EditDonationCommand otherEditDonationCommand = (EditDonationCommand) other;
        return indexOfDonationRecord.equals(otherEditDonationCommand.indexOfDonationRecord)
                && editDonationRecordDescriptor.equals(otherEditDonationCommand.editDonationRecordDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("indexOfDonationRecord", indexOfDonationRecord)
                .add("editDonationRecordDescriptor", editDonationRecordDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the donation record with. Each non-empty field value will
     * replace the corresponding field value of the donation record.
     */
    public static class EditDonationRecordDescriptor {
        private BloodVolume bloodVolume;
        private DonationDate donationDate;

        public EditDonationRecordDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditDonationRecordDescriptor(EditDonationRecordDescriptor toCopy) {
            setBloodVolume(toCopy.bloodVolume);
            setDonationDate(toCopy.donationDate);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(bloodVolume, donationDate);
        }

        public void setBloodVolume(BloodVolume bloodVolume) {
            this.bloodVolume = bloodVolume;
        }

        public void setDonationDate(DonationDate donationDate) {
            this.donationDate = donationDate;
        }

        public Optional<DonationDate> getDonationDate() {
            return Optional.ofNullable(donationDate);
        }

        public Optional<BloodVolume> getBloodVolume() {
            return Optional.ofNullable(bloodVolume);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditDonationRecordDescriptor)) {
                return false;
            }

            EditDonationRecordDescriptor otherEditPersonDescriptor = (EditDonationRecordDescriptor) other;
            return Objects.equals(bloodVolume, otherEditPersonDescriptor.bloodVolume)
                    && Objects.equals(donationDate, otherEditPersonDescriptor.donationDate);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("bloodVolume", getBloodVolume())
                    .add("donationDate", getDonationDate())
                    .toString();
        }
    }
}
