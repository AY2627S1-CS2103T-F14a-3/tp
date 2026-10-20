package seedu.address.storage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.RecurringLesson;

/**
 * Jackson-friendly version of {@link RecurringLesson}.
 */
class JsonAdaptedRecurringLesson {
    private static final String MESSAGE_MISSING_FIELD_FORMAT = "Recurring lesson's %s field is missing!";
    private static final String MESSAGE_INVALID_DAY = "Recurring lesson day is invalid.";
    private static final String MESSAGE_INVALID_TIME = "Recurring lesson time is invalid.";

    @JsonProperty("day")
    private final String day;
    @JsonProperty("time")
    private final String time;
    @JsonProperty("topic")
    private final String topic;

    /**
     * Constructs a {@code JsonAdaptedRecurringLesson} with the given lesson details.
     */
    @JsonCreator
    JsonAdaptedRecurringLesson(@JsonProperty("day") String day, @JsonProperty("time") String time,
            @JsonProperty("topic") String topic) {
        this.day = day;
        this.time = time;
        this.topic = topic;
    }

    /**
     * Constructs a {@code JsonAdaptedRecurringLesson} from a {@code RecurringLesson}.
     */
    JsonAdaptedRecurringLesson(RecurringLesson source) {
        day = source.getDay().name();
        time = source.getTime().toString();
        topic = source.getTopic();
    }

    /**
     * Converts this Jackson-friendly object into its model representation.
     *
     * @throws IllegalValueException if the recurring lesson details are malformed.
     */
    RecurringLesson toModelType() throws IllegalValueException {
        if (day == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "day"));
        }
        if (time == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "time"));
        }
        if (topic == null) {
            throw new IllegalValueException(String.format(MESSAGE_MISSING_FIELD_FORMAT, "topic"));
        }
        if (topic.isBlank()) {
            throw new IllegalValueException(RecurringLesson.MESSAGE_TOPIC_CONSTRAINTS);
        }

        final DayOfWeek modelDay;
        final LocalTime modelTime;
        try {
            modelDay = DayOfWeek.valueOf(day);
            modelTime = LocalTime.parse(time);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(MESSAGE_INVALID_DAY, exception);
        } catch (DateTimeParseException exception) {
            throw new IllegalValueException(MESSAGE_INVALID_TIME, exception);
        }

        try {
            return new RecurringLesson(modelDay, modelTime, topic);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException(exception.getMessage(), exception);
        }
    }
}
