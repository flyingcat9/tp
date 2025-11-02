package bloodnet.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A UI for the status bar that is displayed at the header of the application.
 */
public class OutputDisplay extends UiPart<Region> {

    private static final String FXML = "OutputDisplay.fxml";

    @FXML
    private TextArea outputDisplay;

    public OutputDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        outputDisplay.setWrapText(true);
        outputDisplay.setText(feedbackToUser);
    }
}
