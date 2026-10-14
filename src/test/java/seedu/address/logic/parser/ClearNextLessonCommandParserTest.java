package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearNextLessonCommand;

public class ClearNextLessonCommandParserTest {

    private final ClearNextLessonCommandParser parser = new ClearNextLessonCommandParser();

    @Test
    public void parse_validInput_success() {
        assertParseSuccess(parser, "1", new ClearNextLessonCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_missingMalformedOrExtraInput_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearNextLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "zero", expectedMessage);
        assertParseFailure(parser, "0", expectedMessage);
        assertParseFailure(parser, "1 extra", expectedMessage);
    }
}
