package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearNextLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a {@code ClearNextLessonCommand} object.
 */
public class ClearNextLessonCommandParser implements Parser<ClearNextLessonCommand> {

    @Override
    public ClearNextLessonCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ClearNextLessonCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearNextLessonCommand.MESSAGE_USAGE), pe);
        }
    }
}
