package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class NextLessonCommandTest {

    private static final LessonDuration DURATION = new LessonDuration("90");

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_clashWithHiddenStudent_rejectsWithoutChangingDataOrFilter() {
        Person other = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        NextLesson existing = new NextLesson(LocalDate.of(2099, 6, 15), LocalTime.of(14, 0), "Geometry", DURATION);
        model.setPerson(other, new PersonBuilder(other).withNextLesson(existing).build());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        NextLesson proposed = new NextLesson(existing.date, LocalTime.of(15, 0), "Algebra",
                new LessonDuration("30"));
        assertCommandFailure(new NextLessonCommand(INDEX_FIRST_PERSON, proposed), model,
                String.format(NextLessonCommand.MESSAGE_LESSON_CLASH, other.getName().fullName,
                        existing.date, existing.time, existing.duration.getDurationMinutes()));
    }

    @Test
    public void execute_replacingOwnOverlappingLesson_succeeds() {
        Person student = model.getFilteredPersonList().getFirst();
        NextLesson oldLesson = new NextLesson(LocalDate.of(2099, 6, 15), LocalTime.of(14, 0), "Geometry", DURATION);
        Person original = new PersonBuilder(student).withNextLesson(oldLesson).build();
        model.setPerson(student, original);
        NextLesson proposed = new NextLesson(oldLesson.date, LocalTime.of(14, 30), "Algebra", DURATION);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(original, new PersonBuilder(original).withNextLesson(proposed).build());

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, proposed), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        proposed.date, proposed.time, proposed.duration.getDurationMinutes(), "minutes",
                        proposed.topic),
                expectedModel);
    }

    @Test
    public void execute_oneMinuteDuration_successMessageUsesSingularMinute() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson lesson = new NextLesson(
                LocalDate.of(2099, 6, 15), LocalTime.of(14, 0), "Minimum", new LessonDuration("1"));
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);
        String expectedMessage = "Updated next lesson for " + student.getName().fullName
                + " to 2099-06-15 at 14:00 for 1 minute: Minimum";

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleMinutesDuration_successMessageUsesPluralMinutes() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson lesson = new NextLesson(
                LocalDate.of(2099, 6, 15), LocalTime.of(14, 0), "Short lesson", new LessonDuration("2"));
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);
        String expectedMessage = "Updated next lesson for " + student.getName().fullName
                + " to 2099-06-15 at 14:00 for 2 minutes: Short lesson";

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndex_setsNextLessonAndPreservesStudentDetails() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson oldLesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Old topic", DURATION);
        Person studentWithDetails = new PersonBuilder(student).withNote("Needs extra practice")
                .withSubject("Biology").withTags("priority").withNextLesson(oldLesson).build();
        model.setPerson(student, studentWithDetails);

        NextLesson newLesson = new NextLesson(
                LocalDate.now().plusDays(2), LocalTime.of(16, 0), "Quadratic equations", DURATION);
        Person updatedStudent = new PersonBuilder(studentWithDetails).withNextLesson(newLesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithDetails, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, newLesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        newLesson.date, newLesson.time, DURATION.getDurationMinutes(), "minutes", newLesson.topic),
                expectedModel);
    }

    @Test
    public void execute_validIndexWithoutExistingLesson_setsNextLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson lesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra", DURATION);
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        lesson.date, lesson.time, DURATION.getDurationMinutes(), "minutes", lesson.topic),
                expectedModel);
    }

    @Test
    public void execute_validIndexInFilteredList_updatesCorrectStudentAndResetsFilter() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person student = model.getFilteredPersonList().get(0);
        NextLesson lesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra", DURATION);
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        lesson.date, lesson.time, DURATION.getDurationMinutes(), "minutes", lesson.topic),
                expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NextLesson lesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra", DURATION);
        assertCommandFailure(new NextLessonCommand(invalidIndex, lesson), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getPersonList().size());
        NextLesson lesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra", DURATION);
        assertCommandFailure(new NextLessonCommand(INDEX_SECOND_PERSON, lesson), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        NextLesson lesson = new NextLesson(
                LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra", DURATION);
        NextLessonCommand command = new NextLessonCommand(INDEX_FIRST_PERSON, lesson);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new NextLessonCommand(INDEX_FIRST_PERSON, lesson)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(new ClearCommand()));
        assertFalse(command.equals(new NextLessonCommand(INDEX_SECOND_PERSON, lesson)));
        assertFalse(command.equals(new NextLessonCommand(INDEX_FIRST_PERSON,
                new NextLesson(LocalDate.now().plusDays(2), LocalTime.of(14, 30), "Algebra", DURATION))));
    }
}
