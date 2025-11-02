package bloodnet.ui;

import bloodnet.model.donationrecord.DonationRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code DonationRecord}.
 */
public class DonationRecordCard extends UiPart<Region> {

    private static final String FXML = "DonationRecordCard.fxml";

    public final DonationRecord record;

    @FXML
    private HBox cardPane;

    @FXML
    private Label id;

    @FXML
    private Label donorName;

    @FXML
    private Label donationDate;

    @FXML
    private Label bloodVolume;

    /**
     * Creates a {@code DonationRecordCard} with the given {@code DonationRecord} and index to display.
     */
    public DonationRecordCard(DonationRecord record, int displayedIndex) {
        super(FXML);
        this.record = record;
        id.setText(displayedIndex + ". ");
        donorName.setText(record.getDonorName() + " (" + record.getDonorPhoneNumber() + ")");
        donationDate.setText(record.getDonationDate().toString());
        bloodVolume.setText(record.getBloodVolume().toString() + " ml");

    }
}
