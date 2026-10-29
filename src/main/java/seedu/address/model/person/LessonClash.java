package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * An immutable clash result identifying the student and the exact lesson found by the checker.
 * Both records are already immutable, so no student data is copied or looked up again by callers.
 *
 * @param <T> the conflicting lesson type, either {@link NextLesson} or {@link RecurringLesson}
 */
public record LessonClash<T>(Person student, T lesson) {
    /** Creates a result containing a non-null student and conflicting lesson. */
    public LessonClash {
        requireNonNull(student);
        requireNonNull(lesson);
    }
}
