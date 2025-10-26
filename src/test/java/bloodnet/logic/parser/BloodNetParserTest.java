package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.AddCommand;
import bloodnet.logic.commands.ClearCommand;
import bloodnet.logic.commands.DeleteCommand;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditCommand.EditPersonDescriptor;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.ExitCommand;
import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindDonationsCommand;
import bloodnet.logic.commands.HelpCommand;
import bloodnet.logic.commands.ListCommand;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.person.NameContainsKeywordsPredicate;
import bloodnet.model.person.Person;
import bloodnet.testutil.EditPersonDescriptorBuilder;
import bloodnet.testutil.PersonBuilder;
import bloodnet.testutil.PersonUtil;

public class BloodNetParserTest {

    private final BloodNetParser parser = new BloodNetParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_finddonations() throws Exception {
        FindDonationsCommand command = (FindDonationsCommand) parser.parseCommand(
                FindDonationsCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new FindDonationsCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_editdonations() throws Exception {
        EditDonationCommand command = (EditDonationCommand) parser.parseCommand(
                EditDonationCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + " v/200");
        EditDonationCommand.EditDonationRecordDescriptor edit = new EditDonationCommand.EditDonationRecordDescriptor();
        edit.setBloodVolume(new BloodVolume("200"));
        assertEquals(new EditDonationCommand(INDEX_FIRST_PERSON, edit), command);
    }



    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.getMessageUsage()), () -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
