package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;

/**
 * Shows the student(s) with the earliest upcoming lesson.
 */
public class UpcomingCommand extends Command {

    public static final String COMMAND_WORD = "upcoming";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the student(s) with the earliest "
            + "upcoming lesson.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Showing student(s) with the earliest upcoming lesson at %1$s.";
    public static final String MESSAGE_NO_UPCOMING_LESSON = "No upcoming lessons are scheduled.";

    private final Clock clock;

    /**
     * Creates a command using the computer's local time zone.
     */
    public UpcomingCommand() {
        this(Clock.systemDefaultZone());
    }

    /**
     * Creates a command whose upcoming-lesson boundary uses the supplied clock.
     */
    public UpcomingCommand(Clock clock) {
        this.clock = requireNonNull(clock);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime earliest = model.getAddressBook().getPersonList().stream()
                .map(Person::getNextLesson)
                .flatMap(Optional::stream)
                .map(NextLesson::getDateTime)
                .filter(dateTime -> !dateTime.isBefore(now))
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new CommandException(MESSAGE_NO_UPCOMING_LESSON));

        Predicate<Person> startsAtEarliest = person -> person.getNextLesson()
                .map(NextLesson::getDateTime)
                .filter(earliest::equals)
                .isPresent();
        model.updateFilteredPersonList(startsAtEarliest);
        return new CommandResult(String.format(MESSAGE_SUCCESS, earliest.toLocalDate() + " " + earliest.toLocalTime()));
    }
}
