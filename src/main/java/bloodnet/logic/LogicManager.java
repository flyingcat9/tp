package bloodnet.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import bloodnet.commons.core.GuiSettings;
import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.BloodNetParser;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import bloodnet.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    public static final String TERMINAL_COMMAND_SESSION_STATE_ERROR_MESSAGE =
            "An error has occured under the hood! \n"
                    + "Your previous command was likely not properly captured. Please try again.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final BloodNetParser bloodNetParser;

    private CommandSession currentSession = null;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and
     * {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        bloodNetParser = new BloodNetParser();
    }

    /**
     * Constructs a {@code LogicManager} with the given {@code Model},
     * {@code Storage} and {@code BloodNetParser}.
     */
    public LogicManager(Model model, Storage storage, BloodNetParser bloodNetParser) {
        this.model = model;
        this.storage = storage;
        this.bloodNetParser = bloodNetParser;
    }

    @Override
    public InputResponse handle(String input) throws CommandException, ParseException {
        logger.info("----------------[USER INPUT][" + input + "]");

        if (currentSession == null) {
            Command command = bloodNetParser.parseCommand(input);
            this.currentSession = command.createSession(model);
        }

        assert this.currentSession != null;

        return advanceCurrentSession(input);
    }

    private InputResponse advanceCurrentSession(String input) throws CommandException {
        InputResponse response;
        try {
            response = currentSession.handle(input);
        } catch (TerminalSessionStateException e) {
            currentSession = null;
            response = new InputResponse(TERMINAL_COMMAND_SESSION_STATE_ERROR_MESSAGE);
        } catch (CommandException e) {
            // If a CommandException is thrown upon calling currentSession.handle(input),
            // the current session should be ended.
            // However, we still throw the CommandException,
            // so that the OutputBox's text does not get reset to an empty String.
            // Refer to OutputBox::handleCommandEntered to better understand this.
            currentSession = null;
            throw e;
        }
        if (currentSession != null && currentSession.isDone()) {
            currentSession = null;
            saveBloodNetSafely();
        }
        return response;
    }

    private void saveBloodNetSafely() throws CommandException {
        try {
            storage.saveBloodNet(model.getBloodNet());

            // Reload bloodnet model with the latest data from storage.
            // This way, the ID of newly created objects will be populated in the model
            // The current model is used as a fallback in the event loading from storage fails
            ReadOnlyBloodNet latestBloodNet = storage.readBloodNet().orElse(model.getBloodNet());
            model.setBloodNet(latestBloodNet);

        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        } catch (DataLoadingException e) {
            throw new CommandException("Data file at " + storage.getBloodNetFilePath() + " could not be reloaded.");
        }
    }

    @Override
    public ReadOnlyBloodNet getBloodNet() {
        return model.getBloodNet();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<DonationRecord> getFilteredDonationRecordList() {
        return model.getFilteredDonationRecordList();
    }

    @Override
    public Path getBloodNetFilePath() {
        return model.getBloodNetFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
