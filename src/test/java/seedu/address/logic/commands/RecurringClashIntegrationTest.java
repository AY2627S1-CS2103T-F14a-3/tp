package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringLesson;
import seedu.address.testutil.PersonBuilder;

public class RecurringClashIntegrationTest {

    private static final LocalDate MONDAY = LocalDate.of(2099, 6, 15)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    private static final LessonDuration DURATION = new LessonDuration("90");
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void bothCommands_nextAndRecurringClashes_reportNextLessonClashFirst() {
        Person recurringStudent = model.getFilteredPersonList().get(1);
        Person nextStudent = model.getFilteredPersonList().get(2);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        NextLesson next = new NextLesson(MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        model.setPerson(recurringStudent, new PersonBuilder(recurringStudent).withRecurringLesson(rule).build());
        model.setPerson(nextStudent, new PersonBuilder(nextStudent).withNextLesson(next).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        String expectedError = String.format(Messages.MESSAGE_LESSON_CLASH, nextStudent.getName().fullName,
                next.date, next.time, next.duration.getDurationMinutes());
        NextLesson proposedNext = new NextLesson(MONDAY, LocalTime.of(15, 0), "Algebra", DURATION);
        RecurringLesson proposedRecurring =
                new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(15, 0), "Algebra", DURATION);

        // The recurring conflict appears earlier in the address book, but next-lesson clashes take precedence.
        assertCommandFailure(new NextLessonCommand(INDEX_FIRST_PERSON, proposedNext), model, expectedError);
        assertCommandFailure(new RecurringLessonCommand(INDEX_FIRST_PERSON, proposedRecurring), model, expectedError);
    }

    @Test
    public void nextLesson_hiddenRecurringConflict_rejectsWithoutChangingModel() {
        Person other = model.getFilteredPersonList().get(1);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        model.setPerson(other, new PersonBuilder(other).withRecurringLesson(rule).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        NextLesson proposed = new NextLesson(MONDAY, LocalTime.of(15, 0), "Algebra", DURATION);
        assertCommandFailure(new NextLessonCommand(INDEX_FIRST_PERSON, proposed), model,
                recurringError(other, rule));
    }

    @Test
    public void recurringLesson_hiddenNextLessonConflict_rejectsWithoutChangingModel() {
        Person other = model.getFilteredPersonList().get(1);
        NextLesson next = new NextLesson(MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        model.setPerson(other, new PersonBuilder(other).withNextLesson(next).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        RecurringLesson proposed = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(15, 0), "Algebra", DURATION);
        assertCommandFailure(new RecurringLessonCommand(INDEX_FIRST_PERSON, proposed), model,
                String.format(Messages.MESSAGE_LESSON_CLASH, other.getName().fullName,
                        next.date, next.time, next.duration.getDurationMinutes()));
    }

    @Test
    public void recurringLesson_hiddenRecurringConflict_rejectsWithoutChangingModel() {
        Person other = model.getFilteredPersonList().get(1);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.SUNDAY, LocalTime.of(23, 30), "Geometry", DURATION);
        model.setPerson(other, new PersonBuilder(other).withRecurringLesson(rule).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        RecurringLesson proposed = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(0, 15), "Algebra", DURATION);
        assertCommandFailure(new RecurringLessonCommand(INDEX_FIRST_PERSON, proposed), model,
                recurringError(other, rule));
    }

    @Test
    public void ownExactOccurrence_allowedThroughBothCommands() throws Exception {
        Person original = model.getFilteredPersonList().getFirst();
        NextLesson next = new NextLesson(MONDAY, LocalTime.of(14, 0), "Algebra", DURATION);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        new NextLessonCommand(INDEX_FIRST_PERSON, next).execute(model);
        new RecurringLessonCommand(INDEX_FIRST_PERSON, rule).execute(model);
        new NextLessonCommand(INDEX_FIRST_PERSON, next).execute(model);

        assertEquals(new PersonBuilder(original).withNextLesson(next).withRecurringLesson(rule).build(),
                model.getFilteredPersonList().getFirst());
    }

    @Test
    public void ownPartialOverlap_rejectedThroughBothCommands() {
        Person student = model.getFilteredPersonList().getFirst();
        NextLesson next = new NextLesson(MONDAY, LocalTime.of(14, 30), "Algebra", DURATION);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        Person scheduled = new PersonBuilder(student).withNextLesson(next).withRecurringLesson(rule).build();
        model.setPerson(student, scheduled);
        assertCommandFailure(new NextLessonCommand(INDEX_FIRST_PERSON, next), model, recurringError(student, rule));
        assertCommandFailure(new RecurringLessonCommand(INDEX_FIRST_PERSON, rule), model,
                String.format(Messages.MESSAGE_LESSON_CLASH, student.getName().fullName,
                        next.date, next.time, next.duration.getDurationMinutes()));
    }

    @Test
    public void bothConflictTypes_nextLessonErrorTakesPrecedenceForBothCommands() {
        Person nextStudent = model.getFilteredPersonList().get(1);
        Person recurringStudent = model.getFilteredPersonList().get(2);
        NextLesson next = new NextLesson(MONDAY, LocalTime.of(14, 0), "Geometry", DURATION);
        RecurringLesson rule = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 0), "Algebra", DURATION);
        model.setPerson(nextStudent, new PersonBuilder(nextStudent).withNextLesson(next).build());
        model.setPerson(recurringStudent, new PersonBuilder(recurringStudent).withRecurringLesson(rule).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        String expected = String.format(Messages.MESSAGE_LESSON_CLASH,
                nextStudent.getName().fullName, next.date, next.time, next.duration.getDurationMinutes());

        assertCommandFailure(new NextLessonCommand(INDEX_FIRST_PERSON, next), model, expected);
        assertCommandFailure(new RecurringLessonCommand(INDEX_FIRST_PERSON, rule), model, expected);
    }

    private String recurringError(Person student, RecurringLesson rule) {
        return String.format(Messages.MESSAGE_RECURRING_LESSON_CLASH, student.getName().fullName,
                rule.getDay(), rule.getTime(), rule.getDuration().getDurationMinutes());
    }
}
