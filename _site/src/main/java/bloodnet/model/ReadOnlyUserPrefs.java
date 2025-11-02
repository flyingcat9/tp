package bloodnet.model;

import java.nio.file.Path;

import bloodnet.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getBloodNetFilePath();

}
