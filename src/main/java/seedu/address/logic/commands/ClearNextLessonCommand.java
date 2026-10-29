package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the scheduled next lesson for a student.
 */
public class ClearNextLessonCommand extends Command {

    public static final String COMMAND_WORD = "clearlesson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the scheduled next lesson for the student "
            + "at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_NEXT_LESSON_CLEARED = "Cleared next lesson for %1$s.";
    public static final String MESSAGE_NO_NEXT_LESSON = "No next lesson is scheduled for %1$s.";

    private final Index index;

    /**
     * Creates a command to clear the next lesson for the student at {@code index}.
     *
     * @param index displayed index of the student to update
     */
    public ClearNextLessonCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person student = getDisplayedStudent(model, index);
        boolean hasNextLesson = student.getNextLesson().isPresent();
        if (hasNextLesson) {
            Person updatedStudent = student.withNextLesson(Optional.empty());
            model.setPerson(student, updatedStudent);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String message = hasNextLesson ? MESSAGE_NEXT_LESSON_CLEARED : MESSAGE_NO_NEXT_LESSON;
        return new CommandResult(String.format(message, student.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClearNextLessonCommand otherClearNextLessonCommand)) {
            return false;
        }
        return index.equals(otherClearNextLessonCommand.index);
    }

    @Override
    public int hashCode() {
        return index.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
