package bloodnet.ui;

import bloodnet.logic.Logic;
import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.exceptions.ParseException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * The UI component that is responsible for receiving textual user inputs.
 */
public class InputBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "InputBox.fxml";

    private final InputHandler inputHandler;

    @FXML
    private TextField inputField;

    /**
     * Creates a {@code InputBox} with the given {@code InputHandler}.
     */
    public InputBox(InputHandler inputHandler) {
        super(FXML);
        this.inputHandler = inputHandler;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        inputField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleInputEntered() {
        String input = inputField.getText();
        if (input.equals("")) {
            return;
        }

        try {
            inputHandler.handle(input);
            inputField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the input box style to use the default style.
     */
    private void setStyleToDefault() {
        inputField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the input box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = inputField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can handles user input.
     */
    @FunctionalInterface
    public interface InputHandler {
        /**
         * Handles the user input and returns the response.
         *
         * @see Logic#handle(String)
         */
        InputResponse handle(String input) throws CommandException, ParseException;
    }

}
