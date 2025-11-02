package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.POSITIVE_INTEGER_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;


/**
 * Deletes a donation record identified using its displayed index.
 */
public class DeleteDonationCommand extends Command {

    public static final String COMMAND_WORD = "deletedonation";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Deletes the "
            + "donation record identified by the index number used in the displayed donation record list.",
            "Parameters: DONATION_RECORD_LIST_INDEX_" + POSITIVE_INTEGER_FORMAT,
            "Example: " + COMMAND_WORD + " 1");

    public static final String MESSAGE_DELETE_DONATION_SUCCESS = "Deleted Donation Record: %1$s";

    private final Index targetIndex;

    public DeleteDonationCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandSession createSession(Model model) throws CommandException {
        DonationRecord donationToDelete = getDonationToDelete(model);
        Person relatedPerson = getPersonForDonation(model, donationToDelete, targetIndex);
        return new ConfirmationCommandSession("delete donation record for: "
                + Messages.format(donationToDelete, relatedPerson), () ->
                this.execute(model)
        );
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        DonationRecord donationToDelete = getDonationToDelete(model);
        Person relatedPerson = getPersonForDonation(model, donationToDelete, targetIndex);
        model.deleteDonationRecord(donationToDelete);
        return new InputResponse(String.format(MESSAGE_DELETE_DONATION_SUCCESS, Messages.format(donationToDelete,
                relatedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteDonationCommand)) {
            return false;
        }

        DeleteDonationCommand otherDeleteCommand = (DeleteDonationCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private DonationRecord getDonationToDelete(Model model) throws CommandException {
        requireNonNull(model);
        List<DonationRecord> lastShownList = model.getFilteredDonationRecordList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DONATION_DISPLAYED_INDEX);
        }

        return lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Retrieves the {@code Person} corresponding to the {@code targetPersonIndex} of this {@code DeleteDonationCommand}
     */
    private Person getPersonForDonation(Model model, DonationRecord donationRecord, Index targetIndex)
            throws CommandException {
        requireNonNull(model);
        requireNonNull(donationRecord);

        List<DonationRecord> lastShownList = model.getFilteredDonationRecordList();
        UUID personId = donationRecord.getPersonId();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        for (Person person : model.getBloodNet().getPersonList()) {
            if (person.getId().equals(personId)) {
                return person;
            }
        }

        throw new CommandException(Messages.MESSAGE_PERSON_NOT_FOUND);
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
