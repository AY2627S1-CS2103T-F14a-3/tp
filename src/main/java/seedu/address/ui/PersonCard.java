package seedu.address.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final DateTimeFormatter LESSON_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label schoolLevel;
    @FXML
    private Label note;
    @FXML
    private Label nextLesson;
    @FXML
    private Label nextLessonTopic;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        schoolLevel.setText("Level: " + person.getSchoolLevel().value);
        person.getNextLesson().ifPresentOrElse(lesson -> {
            LocalDateTime lessonDateTime = LocalDateTime.of(lesson.date, lesson.time);
            nextLesson.setText("Next lesson: " + lessonDateTime.format(LESSON_DATE_TIME_FORMATTER));
            nextLessonTopic.setText("Topic: " + lesson.topic);
        }, () -> {
            nextLesson.setText("Next lesson: Not scheduled");
            nextLessonTopic.setVisible(false);
            nextLessonTopic.setManaged(false);
        });
        String noteText = person.getNote().value;
        note.setText("Note: " + noteText);
        if (noteText.isBlank()) {
            note.setVisible(false);
            note.setManaged(false);
        }
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
