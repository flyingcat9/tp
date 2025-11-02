package bloodnet;

import static bloodnet.model.util.SampleDataUtil.getSampleBloodNet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import bloodnet.commons.core.Config;
import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.core.Version;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.commons.util.ConfigUtil;
import bloodnet.commons.util.StringUtil;
import bloodnet.logic.Logic;
import bloodnet.logic.LogicManager;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;
import bloodnet.model.util.SampleDataUtil;
import bloodnet.storage.BloodNetStorage;
import bloodnet.storage.JsonBloodNetStorage;
import bloodnet.storage.JsonUserPrefsStorage;
import bloodnet.storage.Storage;
import bloodnet.storage.StorageManager;
import bloodnet.storage.UserPrefsStorage;
import bloodnet.ui.Ui;
import bloodnet.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 2, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing BloodNet ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        BloodNetStorage bloodNetStorage = new JsonBloodNetStorage(userPrefs.getBloodNetFilePath());
        storage = new StorageManager(bloodNetStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s bloodnet and {@code userPrefs}. <br>
     * The data from the sample bloodnet will be used instead if {@code storage}'s bloodnet is not found,
     * or an empty bloodnet will be used instead if errors occur when reading {@code storage}'s bloodnet.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getBloodNetFilePath());

        Optional<ReadOnlyBloodNet> bloodNetOptional;
        ReadOnlyBloodNet initialData;
        try {
            bloodNetOptional = storage.readBloodNet();
            if (!bloodNetOptional.isPresent()) {
                logger.info("Creating a new data file " + storage.getBloodNetFilePath()
                        + " populated with a sample BloodNet.");

                // We need to save the sample BloodNet data to the data file
                // right away so that their UUIDs will be populated by the storage layer.
                storage.saveBloodNet(getSampleBloodNet());

                // Then, we need to read the data via the storage layer again
                // because it will populate the person's name in every Donation Record
                bloodNetOptional = storage.readBloodNet();
            }
            initialData = bloodNetOptional.orElseGet(SampleDataUtil::getSampleBloodNet);

        } catch (AccessDeniedException e) {
            logger.warning(String.format(StorageManager.FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()));
            initialData = new BloodNet();
        } catch (IOException ioe) {
            logger.warning(String.format(StorageManager.FILE_OPS_ERROR_FORMAT, ioe.getMessage()));
            initialData = new BloodNet();
        } catch (DataLoadingException e) {
            logger.warning("Data file at " + storage.getBloodNetFilePath() + " could not be loaded."
                    + " Will be starting with an empty BloodNet.");
            initialData = new BloodNet();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                    + " Using default config properties.");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting BloodNet " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping BloodNet ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
