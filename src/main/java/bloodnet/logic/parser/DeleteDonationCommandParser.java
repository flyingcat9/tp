package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.DeleteDonationCommand;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteDonationCommand object
 */
public class DeleteDonationCommandParser implements Parser<DeleteDonationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteDonationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteDonationCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteDonationCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDonationCommand.getMessageUsage()), pe);
        }
    }

}
