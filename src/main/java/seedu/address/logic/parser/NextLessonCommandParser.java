package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_TOPIC;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NextLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NextLesson;

/**
 * Parses input arguments and creates a {@code NextLessonCommand} object.
 */
public class NextLessonCommandParser implements Parser<NextLessonCommand> {

    private static final String MESSAGE_INVALID_DATE = "Lesson date must be a valid date in YYYY-MM-DD format.";
    private static final String MESSAGE_INVALID_TIME = "Lesson time must be a valid time in HH:mm format.";

    @Override
    public NextLessonCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_LESSON_DATE, PREFIX_LESSON_TIME, PREFIX_LESSON_TOPIC);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LESSON_DATE, PREFIX_LESSON_TIME, PREFIX_LESSON_TOPIC);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            String message = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE);
            throw new ParseException(message, pe);
        }

        if (argMultimap.getValue(PREFIX_LESSON_DATE).isEmpty()
                || argMultimap.getValue(PREFIX_LESSON_TIME).isEmpty()
                || argMultimap.getValue(PREFIX_LESSON_TOPIC).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextLessonCommand.MESSAGE_USAGE));
        }

        LocalDate date = parseDate(argMultimap.getValue(PREFIX_LESSON_DATE).orElseThrow());
        LocalTime time = parseTime(argMultimap.getValue(PREFIX_LESSON_TIME).orElseThrow());
        try {
            return new NextLessonCommand(index, new NextLesson(date, time,
                    argMultimap.getValue(PREFIX_LESSON_TOPIC).orElseThrow()));
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage(), e);
        }
    }

    private LocalDate parseDate(String date) throws ParseException {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ParseException(MESSAGE_INVALID_DATE);
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DATE, e);
        }
    }

    private LocalTime parseTime(String time) throws ParseException {
        if (!time.matches("\\d{2}:\\d{2}")) {
            throw new ParseException(MESSAGE_INVALID_TIME);
        }
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_TIME, e);
        }
    }
}
