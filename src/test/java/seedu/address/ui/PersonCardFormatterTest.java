package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.NextLesson;
import seedu.address.model.person.StudentNote;

public class PersonCardFormatterTest {

    @Test
    public void formatNextLesson_scheduledLesson_returnsFormattedLesson() {
        NextLesson nextLesson = new NextLesson(
                LocalDate.of(2099, 10, 16), LocalTime.of(16, 0), "Quadratic equations");

        PersonCardFormatter.LessonDisplay display =
                PersonCardFormatter.formatNextLesson(Optional.of(nextLesson));

        assertEquals("Next lesson: 16 Oct 2099, 4:00 PM", display.lessonText());
        assertEquals(Optional.of("Topic: Quadratic equations"), display.topicText());
    }

    @Test
    public void formatNextLesson_noLesson_returnsNotScheduled() {
        PersonCardFormatter.LessonDisplay display = PersonCardFormatter.formatNextLesson(Optional.empty());

        assertEquals("Next lesson: Not scheduled", display.lessonText());
        assertTrue(display.topicText().isEmpty());
    }

    @Test
    public void formatNote_nonBlankNote_returnsFormattedNote() {
        StudentNote note = new StudentNote("Needs more practice");

        assertEquals(Optional.of("Note: Needs more practice"), PersonCardFormatter.formatNote(note));
    }

    @Test
    public void formatNote_blankNote_returnsEmpty() {
        assertTrue(PersonCardFormatter.formatNote(new StudentNote("")).isEmpty());
    }
}
