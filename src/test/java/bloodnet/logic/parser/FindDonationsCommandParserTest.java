package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.FindDonationsCommand;

public class FindDonationsCommandParserTest {
    private final FindDonationsCommandParser parser = new FindDonationsCommandParser();

    @Test
    public void parse_validArgs_returnsFindDonationsCommand() {
        assertParseSuccess(parser, "1", new FindDonationsCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(
                parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDonationsCommand.getMessageUsage()));
    }
}
