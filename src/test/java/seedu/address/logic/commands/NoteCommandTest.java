package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentNote;
import seedu.address.testutil.PersonBuilder;

public class NoteCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_addsNote() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String noteText = "Needs extra time for algebra";
        Person updatedStudent = new PersonBuilder(student).withNote(noteText).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote(noteText)), model,
                String.format(NoteCommand.MESSAGE_NOTE_UPDATED, student.getName().fullName, noteText), expectedModel);
        assertEquals(new StudentNote(noteText),
                model.getAddressBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getNote());
    }

    @Test
    public void execute_existingNote_replacesNote() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person studentWithOldNote = new PersonBuilder(student).withNote("Old note").build();
        model.setPerson(student, studentWithOldNote);

        String newNote = "Updated learning needs";
        Person updatedStudent = new PersonBuilder(studentWithOldNote).withNote(newNote).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithOldNote, updatedStudent);

        assertCommandSuccess(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote(newNote)), model,
                String.format(NoteCommand.MESSAGE_NOTE_UPDATED, student.getName().fullName, newNote), expectedModel);
    }

    @Test
    public void execute_emptyNote_clearsNote() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person studentWithNote = new PersonBuilder(student).withNote("Old note").build();
        model.setPerson(student, studentWithNote);

        Person updatedStudent = new PersonBuilder(studentWithNote).withNote("").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentWithNote, updatedStudent);

        assertCommandSuccess(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("")), model,
                String.format(NoteCommand.MESSAGE_NOTE_CLEARED, student.getName().fullName), expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        assertCommandFailure(new NoteCommand(invalidIndex, new StudentNote("A note")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexInFilteredList_updatesCorrectStudentAndResetsFilter() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person student = model.getFilteredPersonList().get(0);
        Person updatedStudent = new PersonBuilder(student).withNote("Filtered student note").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, updatedStudent);

        assertCommandSuccess(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("Filtered student note")), model,
                String.format(NoteCommand.MESSAGE_NOTE_UPDATED, student.getName().fullName,
                        "Filtered student note"), expectedModel);
    }

    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertTrue(INDEX_SECOND_PERSON.getZeroBased() < model.getAddressBook().getPersonList().size());
        assertCommandFailure(new NoteCommand(INDEX_SECOND_PERSON, new StudentNote("A note")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        NoteCommand command = new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("A note"));

        assertTrue(command.equals(command));
        assertTrue(command.equals(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("A note"))));
        assertFalse(command.equals(null));
        assertFalse(command.equals(new ClearCommand()));
        assertFalse(command.equals(new NoteCommand(INDEX_SECOND_PERSON, new StudentNote("A note"))));
        assertFalse(command.equals(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("Other note"))));
    }
}
