package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.LessonUtil.futureLesson;
import static seedu.address.testutil.LessonUtil.withLesson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ClearNextLessonCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_clearsNextLessonAndPreservesStudentDetails() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NextLesson lesson = futureLesson(1, 14, 30, 60);
        Person studentWithDetails = new PersonBuilder(student).withSchoolLevel("Secondary 4").withSubject("Biology")
                .withNote("Needs extra practice").withTags("priority").withNextLesson(lesson).build();
        model.setPerson(student, studentWithDetails);

        Person updatedStudent = new PersonBuilder(studentWithDetails).withoutNextLesson().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithDetails, updatedStudent);

        assertCommandSuccess(new ClearNextLessonCommand(INDEX_FIRST_PERSON), model,
                String.format(ClearNextLessonCommand.MESSAGE_NEXT_LESSON_CLEARED, student.getName().fullName),
                expectedModel);
    }

    @Test
    public void execute_validIndexWithoutExistingLesson_reportsNoLessonAndResetsFilter() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertCommandSuccess(new ClearNextLessonCommand(INDEX_FIRST_PERSON), model,
                String.format(ClearNextLessonCommand.MESSAGE_NO_NEXT_LESSON, student.getName().fullName),
                expectedModel);
    }

    @Test
    public void execute_validIndexInFilteredList_clearsCorrectStudentAndResetsFilter() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person student = model.getFilteredPersonList().get(0);
        Person studentWithLesson = withLesson(student, futureLesson(1, 14, 30, 60));
        model.setPerson(student, studentWithLesson);

        Person updatedStudent = new PersonBuilder(studentWithLesson).withoutNextLesson().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithLesson, updatedStudent);

        assertCommandSuccess(new ClearNextLessonCommand(INDEX_FIRST_PERSON), model,
                String.format(ClearNextLessonCommand.MESSAGE_NEXT_LESSON_CLEARED, student.getName().fullName),
                expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        assertCommandFailure(new ClearNextLessonCommand(invalidIndex), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getPersonList().size());

        assertCommandFailure(new ClearNextLessonCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ClearNextLessonCommand command = new ClearNextLessonCommand(INDEX_FIRST_PERSON);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new ClearNextLessonCommand(INDEX_FIRST_PERSON)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(new ClearCommand()));
        assertFalse(command.equals(new ClearNextLessonCommand(INDEX_SECOND_PERSON)));
    }
}
