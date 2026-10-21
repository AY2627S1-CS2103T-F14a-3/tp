package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.StudentMatchesCriteriaPredicate;

/**
 * Filters and lists students who match the supplied academic-information and tag criteria.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters students by school level, subject, "
            + "and/or tags, then displays the matching students as a list with index numbers.\n"
            + "Parameters: [l/SCHOOL_LEVEL] [s/SUBJECT] [t/TAG]...\n"
            + "Example: " + COMMAND_WORD + " l/Secondary 4 s/Mathematics t/weekday";

    private final StudentMatchesCriteriaPredicate predicate;

    /**
     * Creates a filter command that uses the specified matching criteria.
     */
    public FilterCommand(StudentMatchesCriteriaPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand otherFilterCommand)) {
            return false;
        }

        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
