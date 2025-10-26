package bloodnet.logic.commands;

import bloodnet.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting BloodNet as requested ...";

    @Override
    public InputResponse execute(Model model) {
        return new InputResponse(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

}
