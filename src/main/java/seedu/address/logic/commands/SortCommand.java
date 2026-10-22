package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts all students by their next scheduled lesson, placing students without a scheduled lesson last.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all students by their next scheduled lesson, "
            + "earliest first. Students without a scheduled lesson are listed last.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Sorted all students by next lesson.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortPersons(Person.COMPARATOR_BY_NEXT_LESSON);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
