package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static bloodnet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static bloodnet.logic.parser.CliSyntax.PREFIX_NAME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PHONE;
import static java.util.Objects.requireNonNull;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditCommand.EditPersonDescriptor;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_BLOOD_TYPE, PREFIX_DATE_OF_BIRTH);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.getMessageUsage()), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_DATE_OF_BIRTH, PREFIX_BLOOD_TYPE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_BLOOD_TYPE).isPresent()) {
            editPersonDescriptor.setBloodType(ParserUtil.parseBloodType(argMultimap.getValue(PREFIX_BLOOD_TYPE).get()));
        }
        if (argMultimap.getValue(PREFIX_DATE_OF_BIRTH).isPresent()) {
            editPersonDescriptor.setDateOfBirth(ParserUtil.parseDateOfBirth(
                    argMultimap.getValue(PREFIX_DATE_OF_BIRTH).get()));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }


}
