package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
 * Adds or replaces the next lesson for a student.
 */
public class NextLessonCommand extends Command {

    public static final String COMMAND_WORD = "nextlesson";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets or replaces the next lesson for the student "
            + "at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) date/YYYY-MM-DD time/HH:mm "
            + "dur/MINUTES topic/TOPIC\n"
            + "Example: " + COMMAND_WORD
            + " 1 date/2026-12-11 time/14:30 dur/90 topic/Algebra";
    public static final String MESSAGE_NEXT_LESSON_UPDATED =
            "Updated next lesson for %1$s to %2$s at %3$s for %4$s: %5$s";
    public static final String MESSAGE_LESSON_CLASH = "Cannot schedule lesson: conflicts with %1$s's next lesson "
            + "on %2$s at %3$s for %4$d minutes.";
            "Updated next lesson for %1$s to %2$s at %3$s for %4$d minutes: %5$s";

    private static final Logger logger = LogsCenter.getLogger(NextLessonCommand.class);

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
        Optional<Person> conflict = TimetableClashChecker.findNextLessonClash(student, nextLesson,
                model.getAddressBook().getPersonList());
        if (conflict.isPresent()) {
            Person conflictingStudent = conflict.orElseThrow();
            NextLesson conflictingLesson = conflictingStudent.getNextLesson().orElseThrow();
            logger.info("Rejected next lesson: timetable overlap detected; student data unchanged.");
            throw new CommandException(String.format(Messages.MESSAGE_LESSON_CLASH,
                    conflictingStudent.getName().fullName, conflictingLesson.date, conflictingLesson.time,
                    conflictingLesson.duration.getDurationMinutes()));
        }
        Optional<Person> recurringConflict = TimetableClashChecker.findRecurringLessonClash(student, nextLesson,
                model.getAddressBook().getPersonList());
        if (recurringConflict.isPresent()) {
            Person conflictingStudent = recurringConflict.orElseThrow();
            RecurringLesson lesson = conflictingStudent.getRecurringLesson().orElseThrow();
            logger.info("Rejected next lesson: recurring timetable overlap detected; student data unchanged.");
            throw new CommandException(String.format(Messages.MESSAGE_RECURRING_LESSON_CLASH,
                    conflictingStudent.getName().fullName, lesson.getDay(), lesson.getTime(),
                    lesson.getDuration().getDurationMinutes()));
        }
        Person updatedStudent = new Person(student.getName(), student.getPhone(), student.getEmail(),
                student.getAddress(), student.getSchoolLevel(), student.getSubject(), student.getNote(),
                Optional.of(nextLesson), student.getRecurringLesson(), student.getTags());
        model.setPerson(student, updatedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                nextLesson.date, nextLesson.time,
                Messages.formatLessonDuration(nextLesson.duration.getDurationMinutes()), nextLesson.topic));
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
