package bloodnet.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.logic.commands.AddCommand;
import bloodnet.logic.commands.AddDonationCommand;
import bloodnet.logic.commands.ClearCommand;
import bloodnet.logic.commands.CommandInformation;
import bloodnet.logic.commands.DeleteCommand;
import bloodnet.logic.commands.DeleteDonationCommand;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.ExitCommand;
import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindDonationsCommand;
import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.logic.commands.HelpCommand;
import bloodnet.logic.commands.ListCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t16-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;
    public static final HashMap<String, CommandInformation> TEXT = new LinkedHashMap<>();

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";


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
        addLink();
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

    /**
     * Adds the instructions to the HashMap.
     */
    public void addInstructions() {
        TEXT.put(AddCommand.COMMAND_WORD, new CommandInformation(AddCommand.DESCRIPTION,
                AddCommand.PARAMETERS, AddCommand.EXAMPLE));

        TEXT.put(EditCommand.COMMAND_WORD, new CommandInformation(EditCommand.DESCRIPTION, EditCommand.PARAMETERS,
                EditCommand.EXAMPLE));

        TEXT.put(DeleteCommand.COMMAND_WORD, new CommandInformation(DeleteCommand.DESCRIPTION, DeleteCommand.PARAMETERS,
                DeleteCommand.EXAMPLE));

        TEXT.put(FindCommand.COMMAND_WORD, new CommandInformation(FindCommand.DESCRIPTION,
                FindCommand.PARAMETERS, FindCommand.EXAMPLE));

        TEXT.put(AddDonationCommand.COMMAND_WORD, new CommandInformation(AddDonationCommand.DESCRIPTION,
                AddDonationCommand.PARAMETERS, AddDonationCommand.EXAMPLE));

        TEXT.put(EditDonationCommand.COMMAND_WORD, new CommandInformation(EditDonationCommand.DESCRIPTION,
                EditDonationCommand.PARAMETERS, EditDonationCommand.EXAMPLE));

        TEXT.put(DeleteDonationCommand.COMMAND_WORD, new CommandInformation(DeleteDonationCommand.DESCRIPTION,
                DeleteDonationCommand.PARAMETERS, DeleteDonationCommand.EXAMPLE));

        TEXT.put(FindDonationsCommand.COMMAND_WORD, new CommandInformation(FindDonationsCommand.DESCRIPTION,
                FindDonationsCommand.PARAMETERS, FindDonationsCommand.EXAMPLE));

        TEXT.put(FindEligibleCommand.COMMAND_WORD, new CommandInformation(FindEligibleCommand.DESCRIPTION,
                FindEligibleCommand.PARAMETERS, FindEligibleCommand.EXAMPLE));

        TEXT.put(HelpCommand.COMMAND_WORD, new CommandInformation(HelpCommand.DESCRIPTION,
                "", ""));

        TEXT.put(ListCommand.COMMAND_WORD, new CommandInformation("Lists all blood donors.",
                "", ""));

        TEXT.put(ClearCommand.COMMAND_WORD, new CommandInformation("Clears the entire list "
                + "of blood donors.", "", ""));

        TEXT.put(ExitCommand.COMMAND_WORD, new CommandInformation("Exits out of the program and closes "
                + "the graphical user interface.", "", ""));
    }

    /**
     * Adds the header for the message box.
     */
    public void addTitle() {
        Text title = new Text("BloodNet Commands\n");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text newLine = new Text("\n");
        helpMessage.getChildren().add(title);
        helpMessage.getChildren().add(newLine);
    }

    /**
     * Formats the instructions appropriately.
     */
    public void formatInstructions() {
        for (String command: TEXT.keySet()) {
            Text instruction = new Text(command + ": ");
            instruction.setStyle("-fx-font-weight: bold;");
            helpMessage.getChildren().add(instruction);

            Text description = new Text(TEXT.get(command).getDescription() + "\n");
            helpMessage.getChildren().add(description);

            String parameters = TEXT.get(command).getParameters();
            if (!parameters.isEmpty()) {
                Text parameter = new Text(parameters + "\n");
                parameter.setStyle("-fx-fill: #262525;");
                helpMessage.getChildren().add(parameter);
            }

            String examples = TEXT.get(command).getExample();
            if (!examples.isEmpty()) {
                Text example = new Text(examples + "\n");
                example.setStyle("-fx-fill: #4d4b4b;");
                helpMessage.getChildren().add(example);
            }
            Text space = new Text("\n");
            helpMessage.getChildren().add(space);
        }
    }

    /**
     * Add the link to the message box.
     */
    public void addLink() {
        Text text = new Text(HELP_MESSAGE + "         ");
        helpMessage.getChildren().add(text);
        Button linker = new Button("Copy link");
        linker.getStyleClass().add("copyButton");
        linker.setOnAction(event -> copyUrl());
        helpMessage.getChildren().add(linker);
    }
}
