package bloodnet.logic.commands;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.SingleStepCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be
 * executed.
 */
public abstract class Command {

    /**
     * Create a command session to handle this command.
     *
     * @param model {@code Model} which the command should operate on.
     * @return a {@code CommandSession} that will manage user interaction of the command.
     * @throws CommandException If an error occurs during session creation.
     */
    public CommandSession createSession(Model model) throws CommandException {
        return new SingleStepCommandSession(this, model);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract InputResponse execute(Model model) throws CommandException;

}
