package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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

    /**
     * Constructs a {@code JsonAdaptedNextLesson} with the given lesson details.
     */
    @JsonCreator
    JsonAdaptedNextLesson(@JsonProperty("date") String date, @JsonProperty("time") String time,
            @JsonProperty("topic") String topic) {
        this.date = date;
        this.time = time;
        this.topic = topic;
    }

    /**
     * Constructs a {@code JsonAdaptedNextLesson} from a {@code NextLesson}.
     */
    JsonAdaptedNextLesson(NextLesson source) {
        date = source.date.toString();
        time = source.time.toString();
        topic = source.topic;
    }

    /**
     * Converts this Jackson-friendly object into its model representation.
     *
     * @throws IllegalValueException if the lesson details are malformed.
     */
    Optional<NextLesson> toModelType() throws IllegalValueException {
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
        try {
            modelDate = LocalDate.parse(date);
            modelTime = LocalTime.parse(time);
        } catch (DateTimeParseException exception) {
            throw new IllegalValueException(MESSAGE_INVALID_DATE_TIME, exception);
        }

        if (NextLesson.isDateTimeInPast(modelDate, modelTime)) {
            // A past appointment is no longer the student's next lesson.
            return Optional.empty();
        }

        try {
            return Optional.of(new NextLesson(modelDate, modelTime, topic));
        } catch (IllegalArgumentException exception) {
            if (NextLesson.isDateTimeInPast(modelDate, modelTime)) {
                return Optional.empty();
            }
            throw new IllegalValueException(exception.getMessage(), exception);
        }
    }
}
