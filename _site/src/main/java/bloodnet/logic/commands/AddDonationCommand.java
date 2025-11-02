package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.DATE_FORMAT;
import static bloodnet.logic.parser.CliSyntax.MILLILITRE_FORMAT;
import static bloodnet.logic.parser.CliSyntax.POSITIVE_INTEGER_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PERSON_INDEX_ONE_BASED;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
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
import bloodnet.model.donationrecord.DonorIsSamePersonPredicate;
import bloodnet.model.person.Person;

/**
 * Adds a donation record for a person in BloodNet.
 */
public class AddDonationCommand extends Command {

    public static final String COMMAND_WORD = "adddonation";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Adds a donation "
            + "record for the donor identified by their index number in the displayed donor list.",
            "Parameters: " + PREFIX_PERSON_INDEX_ONE_BASED + "DONOR_LIST_INDEX_" + POSITIVE_INTEGER_FORMAT + " "
            + PREFIX_DONATION_DATE + "DONATION_DATE_" + DATE_FORMAT + " " + PREFIX_BLOOD_VOLUME
            + "BLOOD_VOLUME_" + MILLILITRE_FORMAT,
            "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
            + PREFIX_DONATION_DATE + "07-05-2025 "
            + PREFIX_BLOOD_VOLUME + "450 ");

    public static final String MESSAGE_SUCCESS = "New donation record added: %1$s";
    public static final String MESSAGE_DUPLICATE_DONATION_RECORD =
                                                "This donation record already exists in BloodNet.";
    public static final String MESSAGE_CONCATENATED_VALIDATION_ERRORS_HEADER =
            "This record violates the following eligibility criteria: ";

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

    @Override
    public InputResponse execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToAddRecordFor = getPersonToAddRecordFor(model);
        UUID personId = personToAddRecordFor.getId();

        assert personId != null;

        DonationRecord donationRecord = new DonationRecord(null, personId, donationDate, bloodVolume);

        ArrayList<String> validationErrorStrings = donationRecord.validate(model.getBloodNet().getPersonList(),
                                                                           model.getBloodNet().getDonationRecordList());
        if (!validationErrorStrings.isEmpty()) {
            String concatenatedMessage = MESSAGE_CONCATENATED_VALIDATION_ERRORS_HEADER;
            for (String validationErrorString : validationErrorStrings) {
                concatenatedMessage += "\n- " + validationErrorString;
            }
            throw new CommandException(concatenatedMessage);
        }

        if (model.hasDonationRecord(donationRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_DONATION_RECORD);
        }

        model.addDonationRecord(donationRecord);

        model.updateFilteredDonationRecordList(new DonorIsSamePersonPredicate(personToAddRecordFor));

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

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
