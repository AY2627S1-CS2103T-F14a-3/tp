package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_DURATION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NextLessonCommand;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;

public class NextLessonCommandParserTest {

    private final NextLessonCommandParser parser = new NextLessonCommandParser();

    @Test
    public void parse_validInput_success() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(14, 30);
        String topic = "Algebra and geometry";
        LessonDuration duration = new LessonDuration("90");

        assertParseSuccess(parser, "1 date/" + date + " time/14:30 dur/90 topic/" + topic,
                new NextLessonCommand(INDEX_FIRST_PERSON, new NextLesson(date, time, topic, duration)));
    }

    @Test
    public void parse_topicContainsCommandPrefixes_success() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(14, 30);
        String topic = "Compare date/ formats, time/ notation, dur/ values, and topic/ labels";
        LessonDuration duration = new LessonDuration("90");

        assertParseSuccess(parser, "1 date/" + date + " time/14:30 dur/90 topic/" + topic,
                new NextLessonCommand(INDEX_FIRST_PERSON, new NextLesson(date, time, topic, duration)));
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        assertParseFailure(parser, "1 date/2026-10-11 time/14:30 dur/90",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 date/2026-10-11 dur/90 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 time/14:30 dur/90 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 date/2026-10-11 time/14:30 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "zero date/2026-10-11 time/14:30 dur/90 topic/Algebra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 date/2026-02-30 time/14:30 dur/90 topic/Algebra",
                "Lesson date must be a valid date in YYYY-MM-DD format.");
        assertParseFailure(parser, "1 date/11-10-2026 time/14:30 dur/90 topic/Algebra",
                "Lesson date must be a valid date in YYYY-MM-DD format.");
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, "1 date/2026-10-11 time/2:30pm dur/90 topic/Algebra",
                "Lesson time must be a valid time in HH:mm format.");
        assertParseFailure(parser, "1 date/2026-10-11 time/24:00 dur/90 topic/Algebra",
                "Lesson time must be a valid time in HH:mm format.");
    }

    @Test
    public void parse_pastLesson_failure() {
        assertParseFailure(parser, "1 date/" + LocalDate.now().minusDays(1)
                        + " time/14:30 dur/90 topic/Algebra",
                NextLesson.MESSAGE_DATE_TIME_CONSTRAINTS);
    }

    @Test
    public void parse_blankTopic_failure() {
        assertParseFailure(parser, "1 date/" + LocalDate.now().plusDays(1) + " time/14:30 dur/90 topic/ ",
                NextLesson.MESSAGE_TOPIC_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDuration_failure() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        assertParseFailure(parser, "1 date/" + futureDate + " time/14:30 dur/0 topic/Algebra",
                LessonDuration.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 date/" + futureDate + " time/14:30 dur/ninety topic/Algebra",
                LessonDuration.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 date/2026-10-11 date/2026-10-12 time/14:30 dur/90 topic/Algebra",
                getErrorMessageForDuplicatePrefixes(PREFIX_LESSON_DATE));
        assertParseFailure(parser, "1 date/2026-10-11 time/14:30 dur/60 dur/90 topic/Algebra",
                getErrorMessageForDuplicatePrefixes(PREFIX_LESSON_DURATION));
    }
}
