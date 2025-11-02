package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;

/**
 * Clears the BloodNet person and donation record list.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "BloodNet has been cleared!";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Clears the entire donor and donation record list. Be careful with this command, "
                    + "you are unable to undo this.", "", "");

    @Override
    public CommandSession createSession(Model model) throws CommandException {
        requireNonNull(model);
        return new ConfirmationCommandSession(COMMAND_WORD + " " + "BloodNet", () -> this.execute(model));
    }

    @Override
    public InputResponse execute(Model model) {
        requireNonNull(model);
        model.setBloodNet(new BloodNet());
        return new InputResponse(MESSAGE_SUCCESS);
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
