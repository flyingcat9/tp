package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bloodnet.commons.core.LogsCenter;
import bloodnet.logic.commands.AddCommand;
import bloodnet.logic.commands.AddDonationCommand;
import bloodnet.logic.commands.ClearCommand;
import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.DeleteCommand;
import bloodnet.logic.commands.DeleteDonationCommand;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.ExitCommand;
import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindDonationsCommand;
import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.logic.commands.HelpCommand;
import bloodnet.logic.commands.ListCommand;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class BloodNetParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(BloodNetParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.getMessageUsage()));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditDonationCommand.COMMAND_WORD:
            return new EditDonationCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeleteDonationCommand.COMMAND_WORD:
            return new DeleteDonationCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case FindDonationsCommand.COMMAND_WORD:
            return new FindDonationsCommandParser().parse(arguments);

        case AddDonationCommand.COMMAND_WORD:
            return new AddDonationCommandParser().parse(arguments);

        case FindEligibleCommand.COMMAND_WORD:
            return new FindEligibleCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
