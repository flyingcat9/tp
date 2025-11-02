package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        InputResponse expectedInputResponse = new InputResponse(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), model, expectedInputResponse, expectedModel);
    }

    @Test
    public void getMessage_string_returnsTrue() {
        String expectedMessage = ExitCommand.COMMAND_WORD + ": Exits the program and closes the graphical "
                + "user interface.";
        assertEquals(expectedMessage, ExitCommand.getMessageUsage().trim());
    }
}
