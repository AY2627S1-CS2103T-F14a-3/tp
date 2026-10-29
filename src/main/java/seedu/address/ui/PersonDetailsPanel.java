package seedu.address.ui;

import java.util.Comparator;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * Panel containing the full details of a person selected using the view command.
 */
public class PersonDetailsPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailsPanel.fxml";

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label schoolLevel;
    @FXML
    private Label subject;
    @FXML
    private Label note;
    @FXML
    private Label nextLesson;
    @FXML
    private Label nextLessonTopic;
    @FXML
    private Label recurringLesson;
    @FXML
    private Label recurringLessonTopic;
    @FXML
    private FlowPane tags;

    /**
     * Creates a panel that displays the full record of {@code person}.
     */
    public PersonDetailsPanel(Person person) {
        super(FXML);
        name.setText(person.getName().fullName);
        phone.setText("Phone: " + person.getPhone().value);
        email.setText("Email: " + person.getEmail().value);
        address.setText("Address: " + person.getAddress().value);
        schoolLevel.setText("Level: " + person.getSchoolLevel().value);
        subject.setText(PersonCardFormatter.formatSubject(person.getSubject()));

        PersonCardFormatter.LessonDisplay lessonDisplay =
                PersonCardFormatter.formatNextLesson(person.getNextLesson());
        nextLesson.setText(lessonDisplay.lessonText());
        setOptionalLabel(nextLessonTopic, lessonDisplay.topicText());

        PersonCardFormatter.LessonDisplay recurringLessonDisplay =
                PersonCardFormatter.formatRecurringLesson(person.getRecurringLesson());
        recurringLesson.setText(recurringLessonDisplay.lessonText());
        setOptionalLabel(recurringLessonTopic, recurringLessonDisplay.topicText());
        setOptionalLabel(note, PersonCardFormatter.formatNote(person.getNote()));

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private static void setOptionalLabel(Label label, Optional<String> text) {
        text.ifPresentOrElse(label::setText, () -> {
            label.setVisible(false);
            label.setManaged(false);
        });
    }
}
