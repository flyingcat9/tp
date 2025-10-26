package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PERSON_INDEX_ONE_BASED;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * Adds a donation record for a person in BloodNet.
 */
public class AddDonationCommand extends Command {

    public static final String COMMAND_WORD = "adddonation";

    public static final String DESCRIPTION = "Adds a donation record for "
            + "the donor identified by their index number in the displayed donor list.";

    public static final String EXAMPLE = "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
            + PREFIX_DONATION_DATE + "07-05-2025 "
            + PREFIX_BLOOD_VOLUME + "450 ";

    public static final String MESSAGE_SUCCESS = "New donation record added: %1$s";
    public static final String MESSAGE_DUPLICATE_DONATION_RECORD =
                                                "This donation record already exists in BloodNet";

    public static final String PARAMETERS = "Parameters: "
            + PREFIX_PERSON_INDEX_ONE_BASED + "PERSON INDEX (must be positive integer) "
            + PREFIX_DONATION_DATE + "DONATION DATE (DD-MM-YYYY) "
            + PREFIX_BLOOD_VOLUME + "BLOOD VOLUME (IN MILLILITRES)";

    private final Index targetPersonIndex;
    private final DonationDate donationDate;
    private final BloodVolume bloodVolume;

    /**
     * Creates an AddDonationCommand to add the specified {@code DonationRecord}
     */
    public AddDonationCommand(Index targetPersonIndex, DonationDate donationDate, BloodVolume bloodVolume) {
        requireNonNull(donationDate);
        requireNonNull(bloodVolume);
        this.targetPersonIndex = targetPersonIndex;
        this.donationDate = donationDate;
        this.bloodVolume = bloodVolume;
    }

    public static String getMessageUsage() {
        return COMMAND_WORD + ": " + DESCRIPTION + "\n" + PARAMETERS + "\n" + EXAMPLE;
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToAddRecordFor = getPersonToAddRecordFor(model);
        UUID personId = personToAddRecordFor.getId();

        assert personId != null;

        DonationRecord donationRecord = new DonationRecord(null, personId, donationDate, bloodVolume);

        if (model.hasDonationRecord(donationRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_DONATION_RECORD);
        }

        model.addDonationRecord(donationRecord);

        return new InputResponse(String.format(MESSAGE_SUCCESS, Messages.format(donationRecord, personToAddRecordFor)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddDonationCommand)) {
            return false;
        }

        AddDonationCommand otherAddDonationCommand = (AddDonationCommand) other;
        return targetPersonIndex.equals(otherAddDonationCommand.targetPersonIndex)
            && donationDate.equals(otherAddDonationCommand.donationDate)
            && bloodVolume.equals(otherAddDonationCommand.bloodVolume);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetPersonIndex", targetPersonIndex)
            .add("donationDate", donationDate)
            .add("bloodVolume", bloodVolume)
            .toString();
    }

    /**
     * Retrieves the {@code Person} corresponding to the {@code targetPersonIndex} of this {@code AddDonationCommand}
     */
    private Person getPersonToAddRecordFor(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(targetPersonIndex.getZeroBased());
    }
}
