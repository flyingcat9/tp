package bloodnet.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.logic.commands.AddCommand;
import bloodnet.logic.commands.AddDonationCommand;
import bloodnet.logic.commands.ClearCommand;
import bloodnet.logic.commands.DeleteCommand;
import bloodnet.logic.commands.DeleteDonationCommand;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.ExitCommand;
import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.HelpCommand;
import bloodnet.logic.commands.ListCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.text.*;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t16-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    public static final HashMap<String, CommandInformation> text = new LinkedHashMap<>();


    @FXML
    private Button copyButton;

    @FXML
    private TextFlow helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        addTitle();
        addInstructions();
        formatInstructions();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException <ul>
     *                               <li>
     *                               if this method is called on a thread other than the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }

    public void addInstructions() {
        text.put(AddCommand.COMMAND_WORD, new CommandInformation(AddCommand.DESCRIPTION,
                AddCommand.PARAMETERS, AddCommand.EXAMPLE));

        text.put(EditCommand.COMMAND_WORD, new CommandInformation(EditCommand.DESCRIPTION, EditCommand.PARAMETERS,
                EditCommand.EXAMPLE));

        text.put(DeleteCommand.COMMAND_WORD, new CommandInformation(DeleteCommand.DESCRIPTION, DeleteCommand.PARAMETERS,
                DeleteCommand.EXAMPLE));

        text.put(FindCommand.COMMAND_WORD, new CommandInformation(FindCommand.DESCRIPTION,
                FindCommand.PARAMETERS, FindCommand.EXAMPLE));

        text.put(AddDonationCommand.COMMAND_WORD, new CommandInformation(AddDonationCommand.DESCRIPTION, AddDonationCommand.PARAMETERS,
                AddDonationCommand.EXAMPLE));

        text.put(EditDonationCommand.COMMAND_WORD, new CommandInformation(EditDonationCommand.DESCRIPTION,
                EditDonationCommand.PARAMETERS, EditDonationCommand.EXAMPLE));

        text.put(DeleteDonationCommand.COMMAND_WORD, new CommandInformation(DeleteDonationCommand.DESCRIPTION,
                DeleteDonationCommand.PARAMETERS, DeleteDonationCommand.EXAMPLE));

        text.put(HelpCommand.COMMAND_WORD, new CommandInformation(HelpCommand.DESCRIPTION,
                "", ""));

        text.put(ListCommand.COMMAND_WORD, new CommandInformation("Lists all blood donors.",
                "", ""));

        text.put(ClearCommand.COMMAND_WORD, new CommandInformation("Clears the entire list "
                + "of blood donors.", "", ""));

        text.put(ExitCommand.COMMAND_WORD, new CommandInformation("Exits out of the program and closes "
                + "the graphical user interface.", "", ""));
    }

    public void addTitle() {
        Text title = new Text("BloodNet Commands\n\n");
        helpMessage.getChildren().add(title);
    }

    public void formatInstructions() {
        for (String command: text.keySet()) {
            Text instruction = new Text(command + ": ");
            instruction.setStyle("-fx-font-weight: bold;");
            helpMessage.getChildren().add(instruction);

            Text description = new Text(text.get(command).getDescription() + "\n");
            helpMessage.getChildren().add(description);

            String parameters = text.get(command).getParameters();
            if (!parameters.isEmpty()) {
                Text parameter = new Text(parameters + "\n");
                parameter.setStyle("-fx-fill: #403e3e;");
                helpMessage.getChildren().add(parameter);
            }

            String examples = text.get(command).getExample();
            if (!examples.isEmpty()) {
                Text example = new Text(examples + "\n");
                example.setStyle("-fx-fill: #403e3e;");
                helpMessage.getChildren().add(example);
            }
            Text space = new Text("\n");
            helpMessage.getChildren().add(space);
        }
    }
}
