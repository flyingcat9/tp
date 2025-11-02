package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.POSITIVE_INTEGER_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonorIsSamePersonPredicate;
import bloodnet.model.person.Person;

/**
 * Finds and list all donation records in BloodNet related to
 * the person identified by the index number used in the displayed
 * person list from BloodNet.
 */
public class FindDonationsCommand extends Command {
    public static final String COMMAND_WORD = "finddonations";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Finds all donation "
            + "records related to the donor identified by the index number used in the displayed donor list.",
            "Parameters: DONATION_RECORD_LIST_INDEX_" + POSITIVE_INTEGER_FORMAT, "Example: "
            + COMMAND_WORD + " 1");

    private final Index targetPersonIndex;

    public FindDonationsCommand(Index targetPersonIndex) {
        this.targetPersonIndex = targetPersonIndex;
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        Person personToFindRecordsOf = getPersonToFindRecordsOf(model);
        DonorIsSamePersonPredicate predicate = new DonorIsSamePersonPredicate(personToFindRecordsOf);
        model.updateFilteredDonationRecordList(predicate);
        int filteredDonationRecordListSize = model.getFilteredDonationRecordList().size();
        return new InputResponse(
                String.format(Messages.MESSAGE_DONATIONS_LISTED_OVERVIEW,
                        filteredDonationRecordListSize,
                        filteredDonationRecordListSize == 1 ? "" : "s",
                        personToFindRecordsOf.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handle nulls
        if (!(other instanceof FindDonationsCommand)) {
            return false;
        }

        FindDonationsCommand otherFindDonationsCommand = (FindDonationsCommand) other;
        return targetPersonIndex.equals(otherFindDonationsCommand.targetPersonIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .toString();
    }

    private Person getPersonToFindRecordsOf(Model model) throws CommandException {
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
