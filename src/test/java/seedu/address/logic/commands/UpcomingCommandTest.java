package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class UpcomingCommandTest {

    private static final LocalDate DATE = LocalDate.now().plusDays(10);

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noLessons_throwsCommandException() {
        assertCommandFailure(new UpcomingCommand(), model, UpcomingCommand.MESSAGE_NO_UPCOMING_LESSON);
    }

    @Test
    public void execute_multipleLessons_showsOnlyEarliest() throws Exception {
        List<Person> students = List.copyOf(model.getAddressBook().getPersonList());
        Person earliest = withLesson(students.get(2), DATE, 9);
        model.setPerson(students.get(1), withLesson(students.get(1), DATE, 14));
        model.setPerson(students.get(2), earliest);
        model.setPerson(students.get(4), withLesson(students.get(4), DATE.plusDays(1), 8));

        CommandResult result = new UpcomingCommand().execute(model);

        assertEquals(List.of(earliest), model.getFilteredPersonList());
        assertEquals(String.format(UpcomingCommand.MESSAGE_SUCCESS, DATE + " 09:00"), result.getFeedbackToUser());
    }

    @Test
    public void execute_tiedLessons_showsAllTiedStudents() throws Exception {
        List<Person> students = List.copyOf(model.getAddressBook().getPersonList());
        Person first = withLesson(students.get(0), DATE, 9);
        Person second = withLesson(students.get(3), DATE, 9);
        model.setPerson(students.get(0), first);
        model.setPerson(students.get(3), second);

        new UpcomingCommand().execute(model);

        assertEquals(List.of(first, second), model.getFilteredPersonList());
    }

    @Test
    public void execute_activeFilter_searchesWholeAddressBook() throws Exception {
        List<Person> students = List.copyOf(model.getAddressBook().getPersonList());
        Person earliest = withLesson(students.get(0), DATE, 9);
        model.setPerson(students.get(0), earliest);
        model.updateFilteredPersonList(person -> person.isSamePerson(students.get(1)));

        new UpcomingCommand().execute(model);

        assertEquals(List.of(earliest), model.getFilteredPersonList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UpcomingCommand().execute(null));
    }

    private static Person withLesson(Person person, LocalDate date, int hour) {
        NextLesson lesson = new NextLesson(date, LocalTime.of(hour, 0), "Algebra", new LessonDuration("60"));
        return new PersonBuilder(person).withNextLesson(lesson).build();
    }
}
