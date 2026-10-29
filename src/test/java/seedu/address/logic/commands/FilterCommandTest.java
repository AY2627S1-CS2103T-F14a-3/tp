package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.getStudentsListedOverview;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.SchoolLevel;
import seedu.address.model.person.StudentMatchesCriteriaPredicate;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private final Model model = new ModelManager(createAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(createAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FilterCommand firstCommand = new FilterCommand(new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.empty(), Set.of()));
        FilterCommand secondCommand = new FilterCommand(new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.of(new Subject("Mathematics")), Set.of()));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new FilterCommand(new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.empty(), Set.of()))));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
    }

    @Test
    public void execute_matchingCriteria_listsMatchingStudents() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.of(new Subject("Mathematics")),
                Set.of(new Tag("weekday")));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, getStudentsListedOverview(1), expectedModel);
        assertEquals("Alice", model.getFilteredPersonList().getFirst().getName().fullName);
    }

    @Test
    public void execute_nonMatchingCriteria_listsNoStudents() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.of(new Subject("Chemistry")), Set.of());
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, getStudentsListedOverview(0), expectedModel);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void toStringMethod() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.of(new Subject("Mathematics")), Set.of());
        FilterCommand command = new FilterCommand(predicate);

        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    private AddressBook createAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(new PersonBuilder().withName("Alice").withSchoolLevel("Secondary 4")
                .withSubject("Mathematics").withTags("weekday").build());
        addressBook.addPerson(new PersonBuilder().withName("Ben").withSchoolLevel("Secondary 4")
                .withSubject("Physics").withTags("weekend").build());
        return addressBook;
    }
}
