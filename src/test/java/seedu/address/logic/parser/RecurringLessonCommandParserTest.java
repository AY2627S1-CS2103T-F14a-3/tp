package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TOPIC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RecurringLessonCommand;
import seedu.address.model.person.RecurringLesson;

public class RecurringLessonCommandParserTest {

    private static final String VALID_TIME = "16:30";
    private static final String VALID_TOPIC = "Algebra and geometry";
    private static final String MESSAGE_INVALID_DAY =
            "Lesson day must be a valid day of the week, such as MONDAY.";
    private static final String MESSAGE_INVALID_TIME = "Lesson time must be a valid time in HH:mm format.";

    private final RecurringLessonCommandParser parser = new RecurringLessonCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, "1 day/MONDAY time/" + VALID_TIME + " topic/" + VALID_TOPIC,
                getExpectedCommand(DayOfWeek.MONDAY));
    }

    @Test
    public void parse_lowercaseDay_success() {
        assertParseSuccess(parser, "1 day/monday time/" + VALID_TIME + " topic/" + VALID_TOPIC,
                getExpectedCommand(DayOfWeek.MONDAY));
    }

    @Test
    public void parse_mixedCaseDay_success() {
        assertParseSuccess(parser, "1 day/Monday time/" + VALID_TIME + " topic/" + VALID_TOPIC,
                getExpectedCommand(DayOfWeek.MONDAY));
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RecurringLessonCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1 time/16:30 topic/Algebra", expectedMessage);
        assertParseFailure(parser, "1 day/MONDAY topic/Algebra", expectedMessage);
        assertParseFailure(parser, "1 day/MONDAY time/16:30", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "zero day/MONDAY time/16:30 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RecurringLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "day/MONDAY time/16:30 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RecurringLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDay_failure() {
        assertParseFailure(parser, "1 day/FUNDAY time/16:30 topic/Algebra", MESSAGE_INVALID_DAY);
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, "1 day/MONDAY time/4pm topic/Algebra", MESSAGE_INVALID_TIME);
        assertParseFailure(parser, "1 day/MONDAY time/24:00 topic/Algebra", MESSAGE_INVALID_TIME);
    }

    @Test
    public void parse_blankTopic_failure() {
        assertParseFailure(parser, "1 day/MONDAY time/16:30 topic/ ",
                RecurringLesson.MESSAGE_TOPIC_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 day/MONDAY day/TUESDAY time/16:30 topic/Algebra",
                getErrorMessageForDuplicatePrefixes(PREFIX_LESSON_DAY));
        assertParseFailure(parser, "1 day/MONDAY time/16:30 time/17:30 topic/Algebra",
                getErrorMessageForDuplicatePrefixes(PREFIX_LESSON_TIME));
        assertParseFailure(parser, "1 day/MONDAY time/16:30 topic/Algebra topic/Geometry",
                getErrorMessageForDuplicatePrefixes(PREFIX_LESSON_TOPIC));
    }

    private RecurringLessonCommand getExpectedCommand(DayOfWeek day) {
        return new RecurringLessonCommand(INDEX_FIRST_PERSON,
                new RecurringLesson(day, LocalTime.of(16, 30), VALID_TOPIC));
    }
}
