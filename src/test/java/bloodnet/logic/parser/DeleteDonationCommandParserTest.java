package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_DONATION;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.DeleteDonationCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteDonationCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteDonationCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteDonationCommandParserTest {

    private DeleteDonationCommandParser parser = new DeleteDonationCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteDonationCommand() {
        assertParseSuccess(parser, "1", new DeleteDonationCommand(INDEX_FIRST_DONATION));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, DeleteDonationCommand.getMessageUsage()));
    }
}
