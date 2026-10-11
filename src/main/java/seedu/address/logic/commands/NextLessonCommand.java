package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;

/**
 * Adds or replaces the next lesson for a student.
 */
public class NextLessonCommand extends Command {

    public static final String COMMAND_WORD = "nextlesson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets or replaces the next lesson for the student "
            + "at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) date/YYYY-MM-DD time/HH:mm topic/TOPIC\n"
            + "Example: " + COMMAND_WORD + " 1 date/2026-10-11 time/14:30 topic/Algebra";
    public static final String MESSAGE_NEXT_LESSON_UPDATED = "Updated next lesson for %1$s to %2$s at %3$s: %4$s";

    private final Index index;
    private final NextLesson nextLesson;

    /**
     * Creates a command to set the next lesson for the student at {@code index}.
     *
     * @param index displayed index of the student to update
     * @param nextLesson new next lesson
     */
    public NextLessonCommand(Index index, NextLesson nextLesson) {
        requireNonNull(index);
        requireNonNull(nextLesson);
        this.index = index;
        this.nextLesson = nextLesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> displayedStudents = model.getFilteredPersonList();
        if (index.getZeroBased() >= displayedStudents.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = displayedStudents.get(index.getZeroBased());
        Person updatedStudent = new Person(student.getName(), student.getPhone(), student.getEmail(),
                student.getAddress(), student.getSchoolLevel(), student.getNote(), Optional.of(nextLesson),
                student.getTags());
        model.setPerson(student, updatedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                nextLesson.date, nextLesson.time, nextLesson.topic));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NextLessonCommand otherNextLessonCommand)) {
            return false;
        }
        return index.equals(otherNextLessonCommand.index) && nextLesson.equals(otherNextLessonCommand.nextLesson);
    }
}
