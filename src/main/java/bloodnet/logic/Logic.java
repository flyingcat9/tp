package bloodnet.logic;

import java.nio.file.Path;

import bloodnet.commons.core.GuiSettings;
import bloodnet.logic.commands.InputResponse;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Handle a user input and return the response.
     *
     * @param input The input as entered by the user.
     * @return the response from handling that input.
     * @throws CommandException If an error occurs during input handling.
     * @throws ParseException   If an error occurs during parsing.
     */
    InputResponse handle(String input) throws CommandException, ParseException;

    /**
     * Returns the BloodNet.
     *
     * @see Model#getBloodNet()
     */
    ReadOnlyBloodNet getBloodNet();

    /**
     * Returns an unmodifiable view of the filtered list of persons
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the filtered list of donation records
     */
    ObservableList<DonationRecord> getFilteredDonationRecordList();

    /**
     * Returns the user prefs' bloodnet file path.
     */
    Path getBloodNetFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
