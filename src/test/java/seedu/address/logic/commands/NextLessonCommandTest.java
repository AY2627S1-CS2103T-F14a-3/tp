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
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class NextLessonCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_setsNextLessonAndPreservesStudentDetails() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson oldLesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Old topic");
        Person studentWithDetails = new PersonBuilder(student).withNote("Needs extra practice")
                .withTags("priority").withNextLesson(oldLesson).build();
        model.setPerson(student, studentWithDetails);

        NextLesson newLesson = new NextLesson(LocalDate.now().plusDays(2), LocalTime.of(16, 0), "Quadratic equations");
        Person updatedStudent = new PersonBuilder(studentWithDetails).withNextLesson(newLesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithDetails, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, newLesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        newLesson.date, newLesson.time, newLesson.topic), expectedModel);
    }

    @Test
    public void execute_validIndexWithoutExistingLesson_setsNextLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        lesson.date, lesson.time, lesson.topic), expectedModel);
    }

    @Test
    public void execute_validIndexInFilteredList_updatesCorrectStudentAndResetsFilter() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person student = model.getFilteredPersonList().get(0);
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        Person updatedStudent = new PersonBuilder(student).withNextLesson(lesson).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), model,
                String.format(NextLessonCommand.MESSAGE_NEXT_LESSON_UPDATED, student.getName().fullName,
                        lesson.date, lesson.time, lesson.topic), expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        assertCommandFailure(new NextLessonCommand(invalidIndex, lesson), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getPersonList().size());
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        assertCommandFailure(new NextLessonCommand(INDEX_SECOND_PERSON, lesson), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        NextLessonCommand command = new NextLessonCommand(INDEX_FIRST_PERSON, lesson);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new NextLessonCommand(INDEX_FIRST_PERSON, lesson)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(new ClearCommand()));
        assertFalse(command.equals(new NextLessonCommand(INDEX_SECOND_PERSON, lesson)));
        assertFalse(command.equals(new NextLessonCommand(INDEX_FIRST_PERSON,
                new NextLesson(LocalDate.now().plusDays(2), LocalTime.of(14, 30), "Algebra"))));
    }
}
