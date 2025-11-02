package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyBloodNet_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyBloodNet_success() {
        Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        expectedModel.setBloodNet(new BloodNet());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void createSession_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> (new ClearCommand()).createSession(null));
    }

    @Test
    public void createSession_validModel_returnsConfirmationCommandSession() throws CommandException {
        Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        CommandSession session = (new ClearCommand()).createSession(model);

        assertTrue(session instanceof ConfirmationCommandSession);
        assertFalse(session.isDone());
    }

    @Test
    public void getMessage_string_returnsTrue() {
        String expectedMessage = ClearCommand.COMMAND_WORD
                + ": Clears the entire donor and donation record list. Be careful with this command,"
                + " you are unable to undo this.";
        assertEquals(expectedMessage, ClearCommand.getMessageUsage().trim());
    }

}
