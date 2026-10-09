package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentNote;

/**
 * Adds, replaces, or clears a free-form note on a student record.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds or replaces the note for the student at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) note/NOTE\n"
            + "Use note/ with no text to clear the existing note.\n"
            + "Example: " + COMMAND_WORD + " 1 note/Needs extra time for algebra";
    public static final String MESSAGE_NOTE_UPDATED = "Updated note for %1$s: %2$s";
    public static final String MESSAGE_NOTE_CLEARED = "Cleared note for %1$s.";

    private final Index index;
    private final StudentNote note;

    /**
     * Creates a command to replace the note for the student at {@code index}.
     *
     * @param index displayed index of the student to update
     * @param note new note value
     */
    public NoteCommand(Index index, StudentNote note) {
        requireNonNull(index);
        requireNonNull(note);
        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> displayedStudents = model.getFilteredPersonList();
        if (index.getZeroBased() >= displayedStudents.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = displayedStudents.get(index.getZeroBased());
        Person updatedStudent = new Person(student.getName(), student.getPhone(), student.getEmail(),
                student.getAddress(), student.getSchoolLevel(), note, student.getNextLesson(), student.getTags());

        model.setPerson(student, updatedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (note.value.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NOTE_CLEARED, student.getName().fullName));
        }
        return new CommandResult(String.format(MESSAGE_NOTE_UPDATED, student.getName().fullName, note.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NoteCommand otherNoteCommand)) {
            return false;
        }
        return index.equals(otherNoteCommand.index) && note.equals(otherNoteCommand.note);
    }
}
