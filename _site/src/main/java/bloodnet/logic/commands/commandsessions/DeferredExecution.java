package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.exceptions.CommandException;

/**
 * Represents an execution sequence that is deferred to be carried out later
 */
@FunctionalInterface
public interface DeferredExecution {
    /**
     * Executes the deferred execution sequence.
     *
     * @return the {@link InputResponse} of the execution.
     * @throws CommandException If an error occurs during execution.
     */
    InputResponse run() throws CommandException;
}
