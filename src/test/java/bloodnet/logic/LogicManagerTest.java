package bloodnet.logic;

import static bloodnet.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static bloodnet.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_TYPE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.DATE_OF_BIRTH_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.AMY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.logic.commands.AddCommand;
import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.ListCommand;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.BloodNetParser;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.UserPrefs;
import bloodnet.model.person.Person;
import bloodnet.storage.JsonBloodNetStorage;
import bloodnet.storage.JsonUserPrefsStorage;
import bloodnet.storage.StorageManager;
import bloodnet.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonBloodNetStorage bloodNetStorage =
                new JsonBloodNetStorage(temporaryFolder.resolve("bloodnet.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(bloodNetStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_multiStateCommand_success() throws CommandException, ParseException {
        JsonBloodNetStorage bloodNetStorage =
                new JsonBloodNetStorage(temporaryFolder.resolve("bloodnet.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(bloodNetStorage, userPrefsStorage);
        Logic logic = new LogicManager(model, storage, new BloodNetParserStub());


        InputResponse response;
        for (int i = 1; i <= 3; i++) {
            response = logic.handle("multi state");
            // Verify that session is still ongoing
            assertEquals(Integer.toString(i), response.getFeedbackToUser());
        }

        // Verify that upon reaching the terminal state for 'multi state' command,
        // the session is cleaned up and subsequent handling of user input behaves as a fresh
        // command, not session input
        response = logic.handle("");
        assertEquals("Success", response.getFeedbackToUser());
    }

    @Test
    public void execute_terminalSessionStateException_resetsSession() throws CommandException, ParseException {
        JsonBloodNetStorage bloodNetStorage =
                new JsonBloodNetStorage(temporaryFolder.resolve("bloodnet.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(bloodNetStorage, userPrefsStorage);
        Logic logic = new LogicManager(model, storage, new BloodNetParserStub());

        InputResponse response = logic.handle("throw terminal");

        assertEquals(LogicManager.TERMINAL_COMMAND_SESSION_STATE_ERROR_MESSAGE, response.getFeedbackToUser());

        // Verify that session is cleaned up after terminalSessionStateException and
        // subsequent handling of user input behaves as a fresh command, not session input
        response = logic.handle("");
        assertEquals("Success", response.getFeedbackToUser());
    }

    @Test
    public void execute_commandExceptionDuringSessionHandle_resetsSession() throws CommandException, ParseException {
        JsonBloodNetStorage bloodNetStorage = new JsonBloodNetStorage(temporaryFolder.resolve("bloodnet.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(bloodNetStorage, userPrefsStorage);
        Logic logic = new LogicManager(model, storage, new BloodNetParserStub());

        InputResponse response;
        try {
            response = logic.handle("throw command exception");
            throw new AssertionError("Should throw a CommandException");
        } catch (CommandException e) {
            assertEquals("Command exception occurred", e.getMessage());
        }

        // Verify that session is cleaned up after CommandException and
        // subsequent execute behaves as a fresh command, not session input
        response = logic.handle("");
        assertEquals("Success", response.getFeedbackToUser());
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredDonationRecordsList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredDonationRecordList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        InputResponse response = logic.handle(inputCommand);
        assertEquals(expectedMessage, response.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the response message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the response message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the response message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getBloodNet(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.handle(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an BloodNetStorage that throws the IOException e when saving
        JsonBloodNetStorage bloodNetStorage = new JsonBloodNetStorage(prefPath) {
            @Override
            public void saveBloodNet(ReadOnlyBloodNet bloodNet, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(bloodNetStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveBloodNet method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + BLOOD_TYPE_DESC_AMY + DATE_OF_BIRTH_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    /**
     * A CommandSession stub that throws TerminalSessionStateException
     */
    private class TerminalExceptionSessionStub implements CommandSession {
        @Override
        public InputResponse handle(String input) throws TerminalSessionStateException {
            throw new TerminalSessionStateException();
        }

        @Override
        public boolean isDone() {
            return false;
        }
    }

    /**
     * A Command stub that creates a session that throws TerminalSessionStateException
     */
    private class TerminalExceptionCommandStub extends Command {
        @Override
        public CommandSession createSession(Model model) {
            return new TerminalExceptionSessionStub();
        }

        @Override
        public InputResponse execute(Model model) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A CommandSession stub involving multiple states.
     * <p>
     * Upon each {@code handle} call, it returns the number of times
     * {@code handle} has been invoked. When it has invoked {@code handle}
     * 3 or more times, the session is done.
     * </p>
     */
    private class MultiStateCommandSessionStub implements CommandSession {
        private int currentState = 0;

        @Override
        public InputResponse handle(String input) throws CommandException, TerminalSessionStateException {
            currentState += 1;
            return new InputResponse(Integer.toString(currentState));
        }

        @Override
        public boolean isDone() {
            return this.currentState >= 3;
        }
    }

    /**
     * A Command stub to create a command session involving multiple states.
     */
    private class MultiStateCommandStub extends Command {
        @Override
        public CommandSession createSession(Model model) {
            return new MultiStateCommandSessionStub();
        }

        @Override
        public InputResponse execute(Model model) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Command stub that always succeeds.
     */
    private class SuccessCommandStub extends Command {
        @Override
        public InputResponse execute(Model model) {
            return new InputResponse("Success");
        }
    }

    /**
     * A BloodNetParser stub that allows for parsing of command text to
     * return the Command stubs defined within this enclosing test class.
     */
    private class BloodNetParserStub extends BloodNetParser {
        @Override
        public Command parseCommand(String userInput) {
            if (userInput == "throw terminal") {
                return new TerminalExceptionCommandStub();
            } else if (userInput == "multi state") {
                return new MultiStateCommandStub();
            } else if (userInput == "throw command exception") {
                return new CommandExceptionCommandStub();
            } else {
                return new SuccessCommandStub();
            }
        }
    }

    /**
     * A CommandSession stub that throws CommandException
     */
    private class CommandExceptionSessionStub implements CommandSession {
        @Override
        public InputResponse handle(String input) throws CommandException {
            throw new CommandException("Command exception occurred");
        }

        @Override
        public boolean isDone() {
            return false;
        }
    }

    /**
     * A Command stub that creates a session that throws CommandException
     */
    private class CommandExceptionCommandStub extends Command {
        @Override
        public CommandSession createSession(Model model) {
            return new CommandExceptionSessionStub();
        }

        @Override
        public InputResponse execute(Model model) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
