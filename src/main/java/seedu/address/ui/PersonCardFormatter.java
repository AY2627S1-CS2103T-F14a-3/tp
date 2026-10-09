package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import seedu.address.model.person.NextLesson;
import seedu.address.model.person.StudentNote;

/**
 * Formats tutoring information for display in a student card.
 */
final class PersonCardFormatter {

    private static final DateTimeFormatter LESSON_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);

    private PersonCardFormatter() {}

    /**
     * Formats the next lesson, including its optional topic.
     */
    static LessonDisplay formatNextLesson(Optional<NextLesson> nextLesson) {
        requireNonNull(nextLesson);
        if (nextLesson.isEmpty()) {
            return new LessonDisplay("Next lesson: Not scheduled", Optional.empty());
        }

        NextLesson lesson = nextLesson.get();
        LocalDateTime lessonDateTime = LocalDateTime.of(lesson.date, lesson.time);
        String lessonText = "Next lesson: " + lessonDateTime.format(LESSON_DATE_TIME_FORMATTER);
        return new LessonDisplay(lessonText, Optional.of("Topic: " + lesson.topic));
    }

    /**
     * Formats a non-blank student note, or returns an empty value when there is no note to display.
     */
    static Optional<String> formatNote(StudentNote note) {
        requireNonNull(note);
        return note.value.isBlank() ? Optional.empty() : Optional.of("Note: " + note.value);
    }

    /**
     * Contains the text displayed for a student's next lesson.
     */
    record LessonDisplay(String lessonText, Optional<String> topicText) {
        LessonDisplay {
            requireNonNull(lessonText);
            requireNonNull(topicText);
        }
    }
}
