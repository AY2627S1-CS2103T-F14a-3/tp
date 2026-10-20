package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearRecurringLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a {@code ClearRecurringLessonCommand} object.
 */
public class ClearRecurringLessonCommandParser implements Parser<ClearRecurringLessonCommand> {

    @Override
    public ClearRecurringLessonCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ClearRecurringLessonCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearRecurringLessonCommand.MESSAGE_USAGE), pe);
        }
    }
}
