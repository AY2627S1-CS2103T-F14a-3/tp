package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.person.StudentNote;

public class NoteCommandParserTest {

    private final NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_validIndexAndNote_success() {
        String note = "Needs extra time for algebra";
        assertParseSuccess(parser, "1 note/" + note,
                new NoteCommand(INDEX_FIRST_PERSON, new StudentNote(note)));
    }

    @Test
    public void parse_emptyNote_success() {
        assertParseSuccess(parser, "1 note/", new NoteCommand(INDEX_FIRST_PERSON, new StudentNote("")));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "zero note/Some note",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
    }
}
