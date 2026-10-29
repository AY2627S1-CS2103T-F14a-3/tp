package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringLesson;
import seedu.address.model.person.TimetableClashChecker;

/**
 * Adds or replaces the recurring weekly lesson for a student.
 */
public class RecurringLessonCommand extends Command {
    public static final String COMMAND_WORD = "recurringlesson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets or replaces the recurring weekly lesson for "
            + "the student at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) day/DAY time/HH:mm dur/MINUTES topic/TOPIC\n"
            + "DAY must be a day of the week, such as MONDAY.\n"
            + "Example: " + COMMAND_WORD + " 1 day/MONDAY time/16:30 dur/90 topic/Algebra";
    public static final String MESSAGE_RECURRING_LESSON_UPDATED =
            "Updated recurring lesson for %1$s to every %2$s at %3$s for %4$s: %5$s";

    private static final Logger logger = LogsCenter.getLogger(RecurringLessonCommand.class);

    private final Index index;
    private final RecurringLesson recurringLesson;

    /**
     * Creates a command to set the recurring lesson for the student at {@code index}.
     *
     * @param index displayed index of the student to update
     * @param recurringLesson new recurring lesson
     */
    public RecurringLessonCommand(Index index, RecurringLesson recurringLesson) {
        requireNonNull(index);
        requireNonNull(recurringLesson);
        this.index = index;
        this.recurringLesson = recurringLesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person student = getStudent(model);
        List<Person> allStudents = model.getAddressBook().getPersonList();
        validateNoNextLessonClash(student, allStudents);
        validateNoRecurringLessonClash(student, allStudents);

        Person updatedStudent = student.withRecurringLesson(Optional.of(recurringLesson));
        model.setPerson(student, updatedStudent);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_RECURRING_LESSON_UPDATED, student.getName().fullName,
                recurringLesson.getDay(), recurringLesson.getTime(),
                Messages.formatLessonDuration(recurringLesson.getDuration().getDurationMinutes()),
                recurringLesson.getTopic()));
    }

    /** Returns the student at the displayed index, or rejects an index outside the displayed list. */
    private Person getStudent(Model model) throws CommandException {
        List<Person> displayedStudents = model.getFilteredPersonList();
        if (index.getZeroBased() >= displayedStudents.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return displayedStudents.get(index.getZeroBased());
    }

    /** Rejects a next-lesson clash unless it is an exact occurrence of this student's proposed schedule. */
    private void validateNoNextLessonClash(Person student, List<Person> allStudents) throws CommandException {
        Optional<Person> nextConflict = TimetableClashChecker.findNextLessonClash(student, recurringLesson,
                allStudents);
        if (nextConflict.isPresent()) {
            Person conflictingStudent = nextConflict.orElseThrow();
            NextLesson lesson = conflictingStudent.getNextLesson().orElseThrow();
            logger.info("Rejected recurring lesson: next-lesson overlap detected; student data unchanged.");
            throw new CommandException(String.format(Messages.MESSAGE_LESSON_CLASH,
                    conflictingStudent.getName().fullName, lesson.date, lesson.time,
                    lesson.duration.getDurationMinutes()));
        }
    }

    /** Rejects a clash with any student's recurring lesson, excluding the schedule being replaced. */
    private void validateNoRecurringLessonClash(Person student, List<Person> allStudents) throws CommandException {
        Optional<Person> recurringConflict = TimetableClashChecker.findRecurringLessonClash(student, recurringLesson,
                allStudents);
        if (recurringConflict.isPresent()) {
            Person conflictingStudent = recurringConflict.orElseThrow();
            RecurringLesson lesson = conflictingStudent.getRecurringLesson().orElseThrow();
            logger.info("Rejected recurring lesson: recurring timetable overlap detected; student data unchanged.");
            throw new CommandException(String.format(Messages.MESSAGE_RECURRING_LESSON_CLASH,
                    conflictingStudent.getName().fullName, lesson.getDay(), lesson.getTime(),
                    lesson.getDuration().getDurationMinutes()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RecurringLessonCommand otherRecurringLessonCommand)) {
            return false;
        }
        return index.equals(otherRecurringLessonCommand.index)
                && recurringLesson.equals(otherRecurringLessonCommand.recurringLesson);
    }
}
