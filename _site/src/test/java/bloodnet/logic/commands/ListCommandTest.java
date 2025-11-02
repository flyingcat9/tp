package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.logic.commands.CommandTestUtil.showPersonAtIndex;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        expectedModel = new ModelManager(model.getBloodNet(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void getMessage_string_returnsTrue() {
        String expectedMessage = ListCommand.COMMAND_WORD + ": Lists out all donors.";
        assertEquals(expectedMessage, ListCommand.getMessageUsage().trim());
    }
}
