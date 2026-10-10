package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_updatesRemarkAndPreservesOtherFields() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person target = new PersonBuilder(original).withNote("Existing note")
                .withNextLesson(new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra")).build();
        model.setPerson(original, target);
        Person updated = new PersonBuilder(target).withRemark("Likes baseball").build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);
        expectedModel.updateFilteredPersonList(person -> true);
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes baseball"));

        assertCommandSuccess(command, model,
                String.format(RemarkCommand.MESSAGE_REMARK_UPDATED, target.getName().fullName, "Likes baseball"),
                expectedModel);
        assertTrue(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).equals(updated));
    }

    @Test
    public void execute_emptyRemark_clearsRemark() {
        Person target = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withRemark("Existing remark").build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), target);
        Person updated = new PersonBuilder(target).withRemark("").build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(target, updated);
        expectedModel.updateFilteredPersonList(person -> true);

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("")), model,
                String.format(RemarkCommand.MESSAGE_REMARK_CLEARED, target.getName().fullName),
                expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        assertCommandFailure(new RemarkCommand(Index.fromOneBased(100), new Remark("A remark")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkCommand first = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes baseball"));
        assertTrue(first.equals(first));
        assertTrue(first.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes baseball"))));
        assertFalse(first.equals(null));
        assertFalse(first.equals(new ClearCommand()));
        assertFalse(first.equals(new RemarkCommand(INDEX_SECOND_PERSON, new Remark("Likes baseball"))));
        assertFalse(first.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Other"))));
    }
}
