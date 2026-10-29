package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.LessonUtil.futureLesson;
import static seedu.address.testutil.LessonUtil.withLesson;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class SortCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_mixedLessons_scheduledStudentsFirstInChronologicalOrder() {
        List<Person> students = List.copyOf(model.getAddressBook().getPersonList());
        Person later = withLesson(students.get(0), futureLesson(11, 9, 0, 60));
        Person earlier = withLesson(students.get(3), futureLesson(10, 15, 0, 60));
        Person sameDayLater = withLesson(students.get(5), futureLesson(10, 18, 0, 60));
        model.setPerson(students.get(0), later);
        model.setPerson(students.get(3), earlier);
        model.setPerson(students.get(5), sameDayLater);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sortPersons(Person.COMPARATOR_BY_NEXT_LESSON);

        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(List.of(earlier, sameDayLater, later), model.getFilteredPersonList().subList(0, 3));
    }

    @Test
    public void execute_studentsWithoutLessons_keepRelativeOrderAfterScheduledStudents() {
        List<Person> students = List.copyOf(model.getAddressBook().getPersonList());
        Person scheduled = withLesson(students.get(4), futureLesson(10, 10, 0, 60));
        model.setPerson(students.get(4), scheduled);

        new SortCommand().execute(model);

        List<Person> expectedUnscheduled = students.stream().filter(person -> !person.isSamePerson(scheduled)).toList();
        List<Person> sorted = model.getFilteredPersonList();
        assertEquals(scheduled, sorted.get(0));
        assertEquals(expectedUnscheduled, sorted.subList(1, sorted.size()));
    }

    @Test
    public void execute_activeFilter_showsAllStudents() {
        model.updateFilteredPersonList(person -> false);

        new SortCommand().execute(model);

        assertEquals(model.getAddressBook().getPersonList().size(), model.getFilteredPersonList().size());
    }
}
