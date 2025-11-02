package bloodnet.ui;

import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.logic.commands.AllCommands;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Controller for a help page.
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t16-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

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
        logger.info("HelpWindow initialised.");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        getRoot().setWidth(screenBounds.getWidth() * 0.6);
        getRoot().setHeight(screenBounds.getHeight() * 0.8);
        getRoot().setResizable(true);
        addTitle();
        AllCommands.addCommands();
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
        logger.info("User clicked 'Copy Link' button.");
    }

    /**
     * Adds the header for the message box.
     */
    public void addTitle() {
        Text title = new Text("BloodNet Commands\n");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #C12727;");
        helpMessage.getChildren().add(title);
        Text space = new Text("\n");
        helpMessage.getChildren().add(space);

    }

    /**
     * Formats the instructions appropriately.
     */
    public void formatInstructions() {
        for (String command: AllCommands.COMMANDS.keySet()) {
            Text instruction = new Text(command + ": ");
            instruction.setStyle("-fx-font-weight: bold; -fx-fill: #C12727;");
            helpMessage.getChildren().add(instruction);

            Text description = new Text(AllCommands.COMMANDS.get(command).getDescription() + "\n");
            helpMessage.getChildren().add(description);

            String parameters = AllCommands.COMMANDS.get(command).getParameters();
            if (!parameters.isEmpty()) {
                Text parameter = new Text(parameters + "\n");
                parameter.setStyle("-fx-fill: #262525;");
                helpMessage.getChildren().add(parameter);
            }
            String examples = AllCommands.COMMANDS.get(command).getExample();
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
