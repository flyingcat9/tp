package bloodnet.logic.commands;

import bloodnet.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String DESCRIPTION = "Shows program usage instructions.";
    public static final String EXAMPLE = "Example: " + COMMAND_WORD;

    public static String getMessageUsage() {
        return COMMAND_WORD + ": " + DESCRIPTION + "\n" + EXAMPLE;
    }

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
