package bloodnet.logic.commands;

import bloodnet.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting BloodNet as requested ...";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Exits the program and closes the graphical user interface.", "", "");

    @Override
    public InputResponse execute(Model model) {
        return new InputResponse(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
