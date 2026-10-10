package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Adds, replaces, or clears the remark of a person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds or replaces the remark of the person "
            + "identified by the index number used in the last person listing. "
            + "Use an empty remark to clear the existing remark.\n"
            + "Parameters: INDEX (must be a positive integer) r/REMARK\n"
            + "Example: " + COMMAND_WORD + " 1 r/Likes to swim";
    public static final String MESSAGE_REMARK_UPDATED = "Updated remark for %1$s: %2$s";
    public static final String MESSAGE_REMARK_CLEARED = "Cleared remark for %1$s.";

    private final Index index;
    private final Remark remark;

    /**
     * Creates a command to replace the remark for the person at {@code index}.
     *
     * @param index displayed index of the person to update
     * @param remark new remark value
     */
    public RemarkCommand(Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);
        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> displayedPersons = model.getFilteredPersonList();
        if (index.getZeroBased() >= displayedPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = displayedPersons.get(index.getZeroBased());
        Person updatedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(), person.getAddress(),
                person.getSchoolLevel(), person.getNote(), remark, person.getNextLesson(), person.getTags());
        model.setPerson(person, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (remark.value.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_REMARK_CLEARED, person.getName().fullName));
        }
        return new CommandResult(String.format(MESSAGE_REMARK_UPDATED, person.getName().fullName, remark.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemarkCommand otherRemarkCommand)) {
            return false;
        }
        return index.equals(otherRemarkCommand.index) && remark.equals(otherRemarkCommand.remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("remark", remark)
                .toString();
    }
}
