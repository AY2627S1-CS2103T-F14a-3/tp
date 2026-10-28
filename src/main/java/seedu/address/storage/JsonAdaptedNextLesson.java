package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;

/**
 * Jackson-friendly version of {@link NextLesson}.
 */
class JsonAdaptedNextLesson {

    private static final String MESSAGE_MISSING_FIELD_FORMAT = "Next lesson's %s field is missing!";
    private static final String MESSAGE_INVALID_DATE_TIME = "Next lesson date and time are invalid.";

    @JsonProperty("date")
    private final String date;
    @JsonProperty("time")
    private final String time;
    @JsonProperty("topic")
    private final String topic;
    @JsonProperty("durationMinutes")
    private final Integer durationMinutes;

    /**
     * Constructs a {@code JsonAdaptedNextLesson} with the given lesson details.
     */
    @JsonCreator
    JsonAdaptedNextLesson(@JsonProperty("date") String date, @JsonProperty("time") String time,
            @JsonProperty("topic") String topic, @JsonProperty("durationMinutes") Integer durationMinutes) {
        this.date = date;
        this.time = time;
        this.topic = topic;
        this.durationMinutes = durationMinutes;
    }

    /**
     * Constructs a {@code JsonAdaptedNextLesson} from a {@code NextLesson}.
     */
    JsonAdaptedNextLesson(NextLesson source) {
        date = source.date.toString();
        time = source.time.toString();
        topic = source.topic;
        durationMinutes = source.duration.getDurationMinutes();
    }

    /**
     * Converts this Jackson-friendly object into its model representation.
     *
     * @throws IllegalValueException if the lesson details are malformed.
     */
    Optional<NextLesson> toModelType() throws IllegalValueException {
        return toModelType(Clock.systemDefaultZone());
    }

    /**
     * Restores a saved lesson using the supplied clock to decide whether it has ended.
     */
    Optional<NextLesson> toModelType(Clock clock) throws IllegalValueException {
        requireNonNull(clock);
        Clock loadClock = Clock.fixed(clock.instant(), clock.getZone());
        if (date == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "date"));
        }
        if (time == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "time"));
        }
        if (topic == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "topic"));
        }
        if (topic.isBlank()) {
            throw new IllegalValueException(NextLesson.MESSAGE_TOPIC_CONSTRAINTS);
        }

        final LocalDate modelDate;
        final LocalTime modelTime;
        final LessonDuration modelDuration;
        try {
            modelDate = LocalDate.parse(date);
            modelTime = LocalTime.parse(time);
            int storedDuration = durationMinutes == null
                    ? LessonDuration.DEFAULT_DURATION_MINUTES : durationMinutes;
            modelDuration = LessonDuration.fromStoredValue(storedDuration);
        } catch (DateTimeParseException exception) {
            throw new IllegalValueException(MESSAGE_INVALID_DATE_TIME, exception);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage(), exception);
        }

        try {
            NextLesson lesson = NextLesson.fromStoredValues(modelDate, modelTime, topic, modelDuration);
            if (!lesson.getEndDateTime().isAfter(LocalDateTime.now(loadClock))) {
                // A completed lesson no longer occupies its timetable interval.
                return Optional.empty();
            }
            return Optional.of(lesson);
        } catch (DateTimeException exception) {
            throw new IllegalValueException(MESSAGE_INVALID_DATE_TIME, exception);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage(), exception);
        }
    }
}
