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
import java.util.Optional;

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
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        Person studentWithDetails = new PersonBuilder(student).withSchoolLevel("Secondary 4").withSubject("Biology")
                .withNote("Needs extra practice").withTags("priority").withNextLesson(lesson).build();
        model.setPerson(student, studentWithDetails);

        Person updatedStudent = new Person(studentWithDetails.getName(), studentWithDetails.getPhone(),
                studentWithDetails.getEmail(), studentWithDetails.getAddress(), studentWithDetails.getSchoolLevel(),
                studentWithDetails.getSubject(), studentWithDetails.getNote(), Optional.empty(),
                studentWithDetails.getTags());
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
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        Person studentWithLesson = new PersonBuilder(student).withNextLesson(lesson).build();
        model.setPerson(student, studentWithLesson);

        Person updatedStudent = new Person(studentWithLesson.getName(), studentWithLesson.getPhone(),
                studentWithLesson.getEmail(), studentWithLesson.getAddress(), studentWithLesson.getSchoolLevel(),
                studentWithLesson.getSubject(), studentWithLesson.getNote(), Optional.empty(),
                studentWithLesson.getTags());
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
