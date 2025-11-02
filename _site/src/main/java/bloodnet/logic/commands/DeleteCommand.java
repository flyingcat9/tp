package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.POSITIVE_INTEGER_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.person.Person;

/**
 * Deletes a person identified using its displayed index from the person list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Deletes the "
                    + "donor identified by the index number used in the displayed donor list. Note that the donor "
                    + "can only be deleted if the donor has no donation records.",
            "Parameters: DONOR_LIST_INDEX_" + POSITIVE_INTEGER_FORMAT,
            "Example: " + COMMAND_WORD + " 1");

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted donor: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandSession createSession(Model model) throws CommandException {
        Person personToDelete = getPersonToDelete(model);
        return new ConfirmationCommandSession(COMMAND_WORD + " " + personToDelete.getName(), () -> this.execute(model));
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        Person personToDelete = getPersonToDelete(model);
        model.deletePerson(personToDelete);
        return new InputResponse(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private Person getPersonToDelete(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        if (model.hasDonationRecordFor(personToDelete)) {
            throw new CommandException(Messages.MESSAGE_DELETE_PERSON_WITH_DONATION);
        }

        return personToDelete;
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
