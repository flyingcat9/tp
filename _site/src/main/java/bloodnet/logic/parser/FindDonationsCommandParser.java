package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.FindDonationsCommand;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and create a new FindDonationsCommand object
 */
public class FindDonationsCommandParser implements Parser<FindDonationsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindDonationsCommand
     * and returns a FindDonationsCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindDonationsCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new FindDonationsCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDonationsCommand.getMessageUsage()), pe);
        }
    }
}
