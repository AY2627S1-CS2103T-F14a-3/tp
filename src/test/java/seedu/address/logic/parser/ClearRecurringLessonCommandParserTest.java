package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearRecurringLessonCommand;

public class ClearRecurringLessonCommandParserTest {

    private final ClearRecurringLessonCommandParser parser = new ClearRecurringLessonCommandParser();

    @Test
    public void parse_validInput_success() {
        assertParseSuccess(parser, "1", new ClearRecurringLessonCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_missingMalformedOrExtraInput_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ClearRecurringLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "zero", expectedMessage);
        assertParseFailure(parser, "0", expectedMessage);
        assertParseFailure(parser, "-1", expectedMessage);
        assertParseFailure(parser, "1 extra", expectedMessage);
    }
}
