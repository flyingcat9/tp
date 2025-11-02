package bloodnet.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_BLOOD_TYPE = new Prefix("b/");
    public static final Prefix PREFIX_DATE_OF_BIRTH = new Prefix("d/");
    public static final Prefix PREFIX_PERSON_INDEX_ONE_BASED = new Prefix("p/");
    public static final Prefix PREFIX_DONATION_DATE = new Prefix("d/");
    public static final Prefix PREFIX_BLOOD_VOLUME = new Prefix("v/");

    public static final String DATE_FORMAT = "(DD-MM-YYYY)";
    public static final String POSITIVE_INTEGER_FORMAT = "(MUST_BE_A_POSITIVE_WHOLE_NUMBER)";
    public static final String MILLILITRE_FORMAT = "(IN_MILLILITRES)";
}
