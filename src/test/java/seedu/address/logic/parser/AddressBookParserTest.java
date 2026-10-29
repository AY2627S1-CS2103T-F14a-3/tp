package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearNextLessonCommand;
import seedu.address.logic.commands.ClearRecurringLessonCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NextLessonCommand;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.commands.RecurringLessonCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UpcomingCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringLesson;
import seedu.address.model.person.SchoolLevel;
import seedu.address.model.person.StudentMatchesCriteriaPredicate;
import seedu.address.model.person.StudentNote;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_unicodeWhitespaceAcademicFields_throwsParseException() {
        for (String blank : List.of("\u2003", "\u3000", " \t\u2003\u3000 ")) {
            for (String command : List.of(
                    "add n/Alpha p/90000001 e/alpha@example.com a/Block One",
                    "edit 1", "filter")) {
                assertThrows(ParseException.class, SchoolLevel.MESSAGE_CONSTRAINTS, () ->
                        parser.parseCommand(command + " l/" + blank));
                assertThrows(ParseException.class, Subject.MESSAGE_CONSTRAINTS, () ->
                        parser.parseCommand(command + " s/" + blank));
            }
        }
    }


    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearlesson() throws Exception {
        ClearNextLessonCommand command = (ClearNextLessonCommand) parser.parseCommand(
                ClearNextLessonCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ClearNextLessonCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_clearrecurringlesson() throws Exception {
        ClearRecurringLessonCommand command = (ClearRecurringLessonCommand) parser.parseCommand(
                ClearRecurringLessonCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ClearRecurringLessonCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        FilterCommand command = (FilterCommand) parser.parseCommand("filter l/Secondary 4 s/Mathematics t/weekday");
        assertEquals(new FilterCommand(new StudentMatchesCriteriaPredicate(Optional.of(new SchoolLevel("Secondary 4")),
                Optional.of(new Subject("Mathematics")), Set.of(new Tag("weekday")))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
    }

    @Test
    public void parseCommand_upcoming() throws Exception {
        assertTrue(parser.parseCommand(UpcomingCommand.COMMAND_WORD) instanceof UpcomingCommand);
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_note() throws Exception {
        String note = "Prefers extra algebra practice";
        NoteCommand command = (NoteCommand) parser.parseCommand("note " + INDEX_FIRST_PERSON.getOneBased()
                + " note/" + note);
        assertEquals(new NoteCommand(INDEX_FIRST_PERSON, new StudentNote(note)), command);
    }

    @Test
    public void parseCommand_nextlesson() throws Exception {
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra",
                new LessonDuration("90"));
        NextLessonCommand command = (NextLessonCommand) parser.parseCommand("nextlesson "
                + INDEX_FIRST_PERSON.getOneBased() + " date/" + lesson.date
                + " time/14:30 dur/90 topic/" + lesson.topic);
        assertEquals(new NextLessonCommand(INDEX_FIRST_PERSON, lesson), command);
    }

    @Test
    public void parseCommand_recurringlesson() throws Exception {
        RecurringLesson lesson =
                new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(16, 30), "Algebra",
                        new LessonDuration("90"));
        RecurringLessonCommand command = (RecurringLessonCommand) parser.parseCommand("recurringlesson "
                + INDEX_FIRST_PERSON.getOneBased() + " day/MONDAY time/16:30 dur/90 topic/Algebra");
        assertEquals(new RecurringLessonCommand(INDEX_FIRST_PERSON, lesson), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
