package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the recurring weekly lesson for a student.
 */
public class ClearRecurringLessonCommand extends Command {

    public static final String COMMAND_WORD = "clearrecurringlesson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the recurring lesson for the student at "
            + "INDEX.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_RECURRING_LESSON_CLEARED = "Cleared recurring lesson for %1$s.";
    public static final String MESSAGE_NO_RECURRING_LESSON = "No recurring lesson is scheduled for %1$s.";

    private final Index index;

    /**
     * Creates a command to clear the recurring lesson for the student at {@code index}.
     *
     * @param index displayed index of the student to update
     */
    public ClearRecurringLessonCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person student = getDisplayedStudent(model, index);
        boolean hasRecurringLesson = student.getRecurringLesson().isPresent();
        if (hasRecurringLesson) {
            Person updatedStudent = student.withRecurringLesson(Optional.empty());
            model.setPerson(student, updatedStudent);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String message = hasRecurringLesson ? MESSAGE_RECURRING_LESSON_CLEARED : MESSAGE_NO_RECURRING_LESSON;
        return new CommandResult(String.format(message, student.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClearRecurringLessonCommand otherClearRecurringLessonCommand)) {
            return false;
        }
        return index.equals(otherClearRecurringLessonCommand.index);
    }
}
