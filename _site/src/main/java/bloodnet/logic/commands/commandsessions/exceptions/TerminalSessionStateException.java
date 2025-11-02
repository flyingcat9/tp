package bloodnet.logic.commands.commandsessions.exceptions;

/**
 * Signals that operation is attempted to on a command
 * session that has already reached its terminal state.
 */
public class TerminalSessionStateException extends Exception {
    /**
     * Constructs a new {@code TerminalSessionStateException}
     * with a default message.
     */
    public TerminalSessionStateException() {
        super("Session has reached terminal state and should not be handling new user inputs");
    }
}
