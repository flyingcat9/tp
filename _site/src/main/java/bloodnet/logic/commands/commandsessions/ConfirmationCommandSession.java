package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;

/**
 * Represents a command session that requires user confirmation before
 * proceeding with its execution.
 * <p>
 * This session is typically used for commands that perform destructive
 * operations, such as deletion, where explicit user consent is needed
 * to continue.
 * </p>
 * <p>
 * The session manages a simple state machine that prompts the user for
 * confirmation ("yes" or "no") and handles the user's response accordingly.
 * </p>
 */
public class ConfirmationCommandSession implements CommandSession {
    private enum State {
        INITIAL {
            @Override
            InputOutcome handle(ConfirmationCommandSession session, String action, String input) {
                InputResponse prompt = new InputResponse(
                        String.format(ConfirmationCommandSession.MESSAGE_SEEK_CONFIRMATION, action));
                return new InputOutcome(PENDING_CONFIRMATION, prompt);
            }
        },
        PENDING_CONFIRMATION {
            @Override
            InputOutcome handle(ConfirmationCommandSession session, String action, String input)
                    throws CommandException {
                String normalisedInput = input.trim().toLowerCase();
                if ("yes".equals(normalisedInput)) {
                    InputResponse response = session.onConfirm.run();
                    return new InputOutcome(DONE, response);
                } else if ("no".equals(normalisedInput)) {
                    InputResponse response = new InputResponse(String.format(MESSAGE_CANCELLED, action));
                    return new InputOutcome(DONE, response);
                } else {
                    InputResponse response = new InputResponse(
                            String.format(MESSAGE_INVALID_INPUT,
                                    session.action));
                    return new InputOutcome(this, response);
                }
            }
        },
        DONE {
            @Override
            InputOutcome handle(ConfirmationCommandSession session, String action,
                            String input) throws TerminalSessionStateException {
                throw new TerminalSessionStateException();
            }
        };

        abstract InputOutcome handle(ConfirmationCommandSession session,
                                 String action, String input) throws CommandException, TerminalSessionStateException;
    }

    private record InputOutcome(State nextState, InputResponse response) {
    }

    public static final String MESSAGE_SEEK_CONFIRMATION =
            "Are you sure you want to %s? This action is not reversible.\nKey in either 'yes' or 'no'.";
    public static final String MESSAGE_INVALID_INPUT =
            "Please respond with either 'yes' or 'no'. Are you sure you want to %s?";
    public static final String MESSAGE_CANCELLED = "Operation cancelled. Did not %s.";

    private State currentState = State.INITIAL;
    private final String action;
    private final DeferredExecution onConfirm;

    /**
     * Constructs a {@code ConfirmationCommandSession} with the given action to be
     * executed and the {@code DeferredCommand}.
     */
    public ConfirmationCommandSession(String action, DeferredExecution onConfirm) {
        this.action = action;
        this.onConfirm = onConfirm;
    }

    @Override
    public InputResponse handle(String input) throws CommandException, TerminalSessionStateException {
        InputOutcome outcome = currentState.handle(this, action, input);
        this.currentState = outcome.nextState();
        return outcome.response();
    }

    @Override
    public boolean isDone() {
        return this.currentState.equals(State.DONE);
    }
}
