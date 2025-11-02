package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.person.HasBloodTypeAndIsEligibleToDonatePredicate;
import bloodnet.model.person.HasBloodTypePredicate;
import bloodnet.model.person.IsEligibleToDonatePredicate;

/**
 * Finds and lists all eligible persons based on blood type, date of birth
 * and number of days since their last donation, if applicable.
 */
public class FindEligibleCommand extends Command {

    public static final String COMMAND_WORD = "findeligible";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Finds all donors who are eligible to donate blood for the specified blood type(s) "
                    + "(case-insensitive) and displays them as a list with index numbers. All eligible blood donors "
                    + "must be at least 16 (inclusive) years old on the donation date. For first-time donors, the  "
                    + "donation date must be strictly before their 61st birthday. For returning donors, they must have "
                    + "made a previous donation within the last 3 years from the donation date (inclusive of "
                    + "that donation date), and the donation date must be strictly before their 66th birthday."
                    + " In addition, there must be a minimum of 84 days between any two consecutive donations.",
            "Parameters: BLOOD_TYPE...", "Example: " + COMMAND_WORD + " O+ A+ B+");

    private final List<String> enteredBloodTypes;

    public FindEligibleCommand(List<String> enteredBloodTypes) {
        this.enteredBloodTypes = enteredBloodTypes;
    }

    @Override
    public InputResponse execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new HasBloodTypeAndIsEligibleToDonatePredicate(
                new HasBloodTypePredicate(enteredBloodTypes),
                new IsEligibleToDonatePredicate(model, DonationDate.getTodayDate())));
        int filteredPersonListSize = model.getFilteredPersonList().size();
        return new InputResponse(
                String.format(Messages.MESSAGE_PEOPLE_LISTED_OVERVIEW,
                        filteredPersonListSize,
                        filteredPersonListSize == 1 ? "" : "s"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindEligibleCommand)) {
            return false;
        }

        FindEligibleCommand otherFindCommand = (FindEligibleCommand) other;
        return enteredBloodTypes.equals(otherFindCommand.enteredBloodTypes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("bloodTypes", enteredBloodTypes)
                .toString();
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
