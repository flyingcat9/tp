package bloodnet.logic.commands.commandsessions;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.ExitCommand;
import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;

public class SingleStateCommandSessionTest {
    private Model model = new ModelManager();

    @Test
    public void handle_userInput_isDone() throws CommandException, TerminalSessionStateException {
        SingleStepCommandSession session = new SingleStepCommandSession(
                new CommandStub(), model);

        session.handle("");
        assertTrue(session.isDone());
    }

    @Test
    public void handle_terminalState_throwTerminalSessionStateException()
            throws CommandException, TerminalSessionStateException {
        SingleStepCommandSession session = new SingleStepCommandSession(
                new ExitCommand(), model);

        session.handle("");

        assertThrows(TerminalSessionStateException.class, () -> session.handle(""));
    }

    /**
     * A command stub that have a dummy execute method.
     */
    private class CommandStub extends Command {
        @Override
        public CommandSession createSession(Model mdoel) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public InputResponse execute(Model model) {
            return new InputResponse("Success", false, false);
        }
    }
}
