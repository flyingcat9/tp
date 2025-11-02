package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.DATE_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static bloodnet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static bloodnet.logic.parser.CliSyntax.PREFIX_NAME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PHONE;
import static java.util.Objects.requireNonNull;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.person.Person;

/**
 * Adds a person to BloodNet.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Adds a donor to BloodNet.",
            "Parameters: "
                    + PREFIX_NAME + "NAME "
                    + PREFIX_PHONE + "PHONE "
                    + PREFIX_EMAIL + "EMAIL "
                    + PREFIX_BLOOD_TYPE + "BLOOD_TYPE" + " "
                    + PREFIX_DATE_OF_BIRTH + "DATE_OF_BIRTH_" + DATE_FORMAT, "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_BLOOD_TYPE + "A+ "
            + PREFIX_DATE_OF_BIRTH + "30-03-2004");

    public static final String MESSAGE_SUCCESS = "New donor added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This donor already exists in BloodNet";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new InputResponse(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
