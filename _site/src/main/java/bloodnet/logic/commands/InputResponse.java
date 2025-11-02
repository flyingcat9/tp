package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Represents a response to a user command input.
 */
public class InputResponse {

    private final String feedbackToUser;

    /**
     * Help information should be shown to the user.
     */
    private final boolean showHelp;

    /**
     * The application should exit.
     */
    private final boolean exit;

    /**
     * Constructs a {@code InputResponse} with the specified fields.
     */
    public InputResponse(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
    }

    /**
     * Constructs a {@code InputResponse} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public InputResponse(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InputResponse)) {
            return false;
        }

        InputResponse otherInputResponse = (InputResponse) other;
        return feedbackToUser.equals(otherInputResponse.feedbackToUser)
                && showHelp == otherInputResponse.showHelp
                && exit == otherInputResponse.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }

}
