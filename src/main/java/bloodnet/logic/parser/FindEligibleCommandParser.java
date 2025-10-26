package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;

import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindEligibleCommand object
 */
public class FindEligibleCommandParser implements Parser<FindEligibleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindEligibleCommand
     * and returns a FindEligibleCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindEligibleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] bloodTypesToFilterFor = trimmedArgs.split("\\s+");
        List<String> list = Arrays.asList(bloodTypesToFilterFor);

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEligibleCommand.getMessageUsage()));
        }

        for (String bloodType : list) {
            ParserUtil.parseBloodType(bloodType);
        }

        return new FindEligibleCommand(list);
    }
}
