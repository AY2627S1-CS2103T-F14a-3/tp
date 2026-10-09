package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Phone;
import seedu.address.model.person.SchoolLevel;
import seedu.address.model.person.StudentNote;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_SCHOOL_LEVEL = "Secondary 4";
    private static final String INVALID_SCHOOL_LEVEL = " ";
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_validSchoolLevel_returnsPersonWithSchoolLevel() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, VALID_TAGS);

        assertEquals(VALID_SCHOOL_LEVEL, person.toModelType().getSchoolLevel().value);
    }

    @Test
    public void toModelType_missingSchoolLevel_usesDefaultSchoolLevel() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS);

        assertEquals(SchoolLevel.DEFAULT_VALUE, person.toModelType().getSchoolLevel().value);
    }

    @Test
    public void toModelType_noteIsPreserved() throws Exception {
        String note = "Needs extra time for algebra";
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(new PersonBuilder(BENSON).withNote(note).build());

        assertEquals(new StudentNote(note), adapted.toModelType().getNote());
    }

    @Test
    public void toModelType_missingNote_usesEmptyNote() throws Exception {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, VALID_TAGS);

        assertEquals(new StudentNote(""), adapted.toModelType().getNote());
    }

    @Test
    public void toModelType_nextLessonIsPreserved() throws Exception {
        NextLesson lesson = new NextLesson(LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Algebra");
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(new PersonBuilder(BENSON).withNextLesson(lesson).build());
        String json = JsonUtil.toJsonString(adapted);
        JsonAdaptedPerson reloaded = JsonUtil.fromJsonString(json, JsonAdaptedPerson.class);

        assertEquals(Optional.of(lesson), reloaded.toModelType().getNextLesson());
    }

    @Test
    public void toModelType_missingNextLesson_usesEmptyLesson() throws Exception {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, VALID_TAGS);

        assertEquals(Optional.empty(), adapted.toModelType().getNextLesson());
    }

    @Test
    public void toModelType_expiredNextLesson_clearsLesson() throws Exception {
        JsonAdaptedNextLesson expiredLesson = new JsonAdaptedNextLesson(
                LocalDate.now().minusDays(1).toString(), "14:30", "Algebra");
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, null, expiredLesson, VALID_TAGS);

        assertEquals(Optional.empty(), adapted.toModelType().getNextLesson());
    }

    @Test
    public void toModelType_invalidNextLessonDate_throwsIllegalValueException() {
        JsonAdaptedNextLesson invalidLesson = new JsonAdaptedNextLesson("tomorrow", "14:30", "Algebra");
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, null, invalidLesson, VALID_TAGS);

        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_blankNextLessonTopic_throwsIllegalValueException() {
        JsonAdaptedNextLesson invalidLesson = new JsonAdaptedNextLesson(
                LocalDate.now().plusDays(1).toString(), "14:30", "  ");
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SCHOOL_LEVEL, null, invalidLesson, VALID_TAGS);

        assertThrows(IllegalValueException.class, NextLesson.MESSAGE_TOPIC_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidSchoolLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                INVALID_SCHOOL_LEVEL, VALID_TAGS);

        assertThrows(IllegalValueException.class, SchoolLevel.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
