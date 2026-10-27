package seedu.address.ui;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

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
    private Label academicInfo;
    @FXML
    private Label nextLesson;
    @FXML
    private Label recurringLesson;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText("Phone: " + person.getPhone().value);
        setOptionalLabel(academicInfo, PersonCardFormatter.formatSummaryAcademicInfo(
                person.getSchoolLevel(), person.getSubject()));
        setOptionalLabel(nextLesson, PersonCardFormatter.formatNextLessonSummary(person.getNextLesson()));
        setOptionalLabel(recurringLesson,
                PersonCardFormatter.formatRecurringLessonSummary(person.getRecurringLesson()));
    }

    /**
     * Sets the label text when present, or removes the label from the layout when absent.
     */
    private static void setOptionalLabel(Label label, Optional<String> text) {
        text.ifPresentOrElse(label::setText, () -> {
            label.setVisible(false);
            label.setManaged(false);
        });
    }
}
