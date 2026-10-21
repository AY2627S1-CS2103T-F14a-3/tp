package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.SchoolLevel;
import seedu.address.model.person.StudentMatchesCriteriaPredicate;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a {@code FilterCommand}.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the filter command.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SCHOOL_LEVEL, PREFIX_SUBJECT,
                PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty() || !hasCriteria(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SCHOOL_LEVEL, PREFIX_SUBJECT);
        Optional<SchoolLevel> schoolLevel = Optional.empty();
        if (argMultimap.getValue(PREFIX_SCHOOL_LEVEL).isPresent()) {
            schoolLevel = Optional.of(ParserUtil.parseSchoolLevel(argMultimap.getValue(PREFIX_SCHOOL_LEVEL).get()));
        }
        Optional<Subject> subject = Optional.empty();
        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            subject = Optional.of(ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get()));
        }
        Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new FilterCommand(new StudentMatchesCriteriaPredicate(schoolLevel, subject, tags));
    }

    private boolean hasCriteria(ArgumentMultimap argMultimap) {
        return argMultimap.getValue(PREFIX_SCHOOL_LEVEL).isPresent()
                || argMultimap.getValue(PREFIX_SUBJECT).isPresent()
                || !argMultimap.getAllValues(PREFIX_TAG).isEmpty();
    }
}
