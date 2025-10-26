package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;

/**
 * Represents an interactive command session that can handle
 * multiple inputs from the user to complete a command.
 *
 * <p>
 * A CommandSession manages its own internal state and processes
 * user input incrementally until the command is fully completed.
 * </p>
 */
public interface CommandSession {

    /**
     * Handles a user input within this session and returns
     * the result of processing that input.
     *
     * @param userInput the user input to handle.
     * @return the result of executing or continuing the command session.
     * @throws CommandException              If an error occurs during command execution/ input
     *                                       processing.
     * @throws TerminalSessionStateException If session is already
     *                                       in terminal state.
     */
    InputResponse handle(String userInput) throws CommandException, TerminalSessionStateException;

    /**
     * Returns whether this command session has completed and no longer
     * expects any more user input.
     *
     * @return true if the session is done, false otherwise.
     */
    boolean isDone();
}
