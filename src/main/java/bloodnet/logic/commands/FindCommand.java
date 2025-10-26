package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.Model;
import bloodnet.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in bloodnet whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String DESCRIPTION = "Finds all donors whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.";

    public static final String EXAMPLE = "Example: " + COMMAND_WORD + " alice bob charlie";

    public static final String PARAMETERS = "Parameters: KEYWORD [MORE_KEYWORDS]...";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public static String getMessageUsage() {
        return COMMAND_WORD + ": " + DESCRIPTION + "\n" + PARAMETERS + "\n" + EXAMPLE;
    }

    @Override
    public InputResponse execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int filteredPersonListSize = model.getFilteredPersonList().size();
        return new InputResponse(
                String.format(Messages.MESSAGE_PEOPLE_LISTED_OVERVIEW,
                        filteredPersonListSize,
                        filteredPersonListSize > 1 ? "s" : ""));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
