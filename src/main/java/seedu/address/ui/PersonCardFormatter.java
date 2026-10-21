package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

import seedu.address.model.person.NextLesson;
import seedu.address.model.person.RecurringLesson;
import seedu.address.model.person.StudentNote;
import seedu.address.model.person.Subject;

/**
 * Formats tutoring information for display in a student card.
 */
final class PersonCardFormatter {

    private static final DateTimeFormatter LESSON_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter LESSON_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private PersonCardFormatter() {}

    /**
     * Formats the next lesson, including its optional topic.
     */
    static LessonDisplay formatNextLesson(Optional<NextLesson> nextLesson) {
        requireNonNull(nextLesson);
        if (nextLesson.isEmpty()) {
            return new LessonDisplay("Not scheduled", Optional.empty());
        }

        NextLesson lesson = nextLesson.get();
        LocalDateTime lessonDateTime = LocalDateTime.of(lesson.date, lesson.time);
        String lessonText = lessonDateTime.format(LESSON_DATE_TIME_FORMATTER)
                + " (" + lesson.duration.getDurationMinutes() + " min)";
        return new LessonDisplay(lessonText, Optional.of("Topic: " + lesson.topic));
    }

    /**
     * Formats the recurring lesson, including its topic.
     */
    static LessonDisplay formatRecurringLesson(Optional<RecurringLesson> recurringLesson) {
        requireNonNull(recurringLesson);
        if (recurringLesson.isEmpty()) {
            return new LessonDisplay("Not scheduled", Optional.empty());
        }

        RecurringLesson lesson = recurringLesson.get();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String lessonText = "Every " + day + " at " + lesson.getTime().format(LESSON_TIME_FORMATTER)
                + " (" + lesson.getDuration().getDurationMinutes() + " min)";
        return new LessonDisplay(lessonText, Optional.of("Topic: " + lesson.getTopic()));
    }

    /**
     * Formats a non-blank student note, or returns an empty value when there is no note to display.
     */
    static Optional<String> formatNote(StudentNote note) {
        requireNonNull(note);
        return note.value.isBlank() ? Optional.empty() : Optional.of("Note: " + note.value);
    }

    /**
     * Formats the subject for display in a student card.
     */
    static String formatSubject(Subject subject) {
        requireNonNull(subject);
        return "Subject: " + subject.value;
    }

    /**
     * Contains the schedule and topic text displayed for a student's lesson.
     */
    record LessonDisplay(String lessonText, Optional<String> topicText) {
        LessonDisplay {
            requireNonNull(lessonText);
            requireNonNull(topicText);
        }
    }
}
