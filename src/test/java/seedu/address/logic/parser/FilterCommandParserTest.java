package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.SchoolLevel;
import seedu.address.model.person.StudentMatchesCriteriaPredicate;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_allCriteria_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.of(new Subject("Mathematics")),
                Set.of(new Tag("weekday"), new Tag("exam"))));

        assertParseSuccess(parser, " l/Secondary 4 s/Mathematics t/weekday t/exam ", expectedCommand);
    }

    @Test
    public void parse_singleCriteria_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.of(new Subject("Mathematics")), Set.of()));

        assertParseSuccess(parser, " s/Mathematics", expectedCommand);
    }

    @Test
    public void parse_blankSchoolLevel_throwsParseException() {
        assertParseFailure(parser, " l/", SchoolLevel.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/   ", SchoolLevel.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/ s/Mathematics", SchoolLevel.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_blankSubject_throwsParseException() {
        assertParseFailure(parser, " s/", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " s/   ", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/Secondary 4 s/", Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_blankTag_throwsParseException() {
        assertParseFailure(parser, " t/", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " t/   ", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " s/Mathematics t/", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " t/weekday t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, " t/weekday!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " t/week day", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " t/weekday t/exam!", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_preambleWithValidCriteria_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " students s/Mathematics", expectedMessage);
    }

    @Test
    public void parse_invalidArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " only words", expectedMessage);
        assertParseFailure(parser, " l/Secondary 4 l/Junior College 1", "Multiple values specified for the "
                + "following single-valued field(s): l/");
        assertParseFailure(parser, " s/Mathematics s/Physics", "Multiple values specified for the following "
                + "single-valued field(s): s/");
    }
}
