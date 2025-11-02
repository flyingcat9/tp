package bloodnet.logic.commands;

import bloodnet.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Shows program usage instructions.", "",
            "");

    public static final String SHOWING_HELP_MESSAGE = "Opened help window!";

    @Override
    public InputResponse execute(Model model) {
        return new InputResponse(SHOWING_HELP_MESSAGE, true, false);
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }
}
