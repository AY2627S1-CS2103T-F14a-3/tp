package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCHOOL_LEVEL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.LessonUtil.futureLesson;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    private static final NextLesson NEXT_LESSON = new NextLesson(LocalDate.of(2099, 6, 15),
            LocalTime.of(14, 30), "Algebra", new LessonDuration("60"));
    private static final RecurringLesson RECURRING_LESSON = new RecurringLesson(DayOfWeek.MONDAY,
            LocalTime.of(14, 30), "Mathematics", new LessonDuration("90"));

    @Test
    public void withNote_replacesNote_preservesOtherFieldsAndOriginal() {
        Person original = detailedPersonBuilder().withNextLesson(NEXT_LESSON)
                .withRecurringLesson(RECURRING_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withNote(new StudentNote("New note"));

        assertNotSame(original, updated);
        assertEquals(new PersonBuilder(original).withNote("New note").build(), updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void withNote_emptyNote_clearsOnlyNote() {
        Person original = detailedPersonBuilder().withNextLesson(NEXT_LESSON)
                .withRecurringLesson(RECURRING_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withNote(new StudentNote(""));

        assertNotSame(original, updated);
        assertEquals(new PersonBuilder(original).withNote("").build(), updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void withNextLesson_setsLesson_preservesOtherFieldsAndOriginal() {
        Person original = detailedPersonBuilder().withRecurringLesson(RECURRING_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withNextLesson(Optional.of(NEXT_LESSON));

        assertNotSame(original, updated);
        assertEquals(new PersonBuilder(original).withNextLesson(NEXT_LESSON).build(), updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void withNextLesson_emptyOptional_clearsOnlyNextLesson() {
        Person expected = detailedPersonBuilder().withRecurringLesson(RECURRING_LESSON).build();
        Person original = new PersonBuilder(expected).withNextLesson(NEXT_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withNextLesson(Optional.empty());

        assertNotSame(original, updated);
        assertEquals(expected, updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void withRecurringLesson_setsLesson_preservesOtherFieldsAndOriginal() {
        Person original = detailedPersonBuilder().withNextLesson(NEXT_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withRecurringLesson(Optional.of(RECURRING_LESSON));

        assertNotSame(original, updated);
        assertEquals(new PersonBuilder(original).withRecurringLesson(RECURRING_LESSON).build(), updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void withRecurringLesson_emptyOptional_clearsOnlyRecurringLesson() {
        Person expected = detailedPersonBuilder().withNextLesson(NEXT_LESSON).build();
        Person original = new PersonBuilder(expected).withRecurringLesson(RECURRING_LESSON).build();
        Person snapshot = new PersonBuilder(original).build();

        Person updated = original.withRecurringLesson(Optional.empty());

        assertNotSame(original, updated);
        assertEquals(expected, updated);
        assertEquals(snapshot, original);
    }

    @Test
    public void copyMethods_null_throwsNullPointerException() {
        Person original = detailedPersonBuilder().withNextLesson(NEXT_LESSON)
                .withRecurringLesson(RECURRING_LESSON).build();

        assertThrows(NullPointerException.class, () -> original.withNote(null));
        assertThrows(NullPointerException.class, () -> original.withNextLesson(null));
        assertThrows(NullPointerException.class, () -> original.withRecurringLesson(null));
    }

    /** Returns a builder with non-default academic, note, and tag values to detect accidental field resets. */
    private PersonBuilder detailedPersonBuilder() {
        return new PersonBuilder(ALICE).withSchoolLevel("Junior College 1").withSubject("Biology")
                .withNote("Needs extra practice").withTags("priority", "friend");
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void getNextLesson_noLesson_returnsEmpty() {
        assertEquals(Optional.empty(), new PersonBuilder().build().getNextLesson());
    }

    @Test
    public void getNextLesson_lessonScheduled_returnsLesson() {
        NextLesson lesson = futureLesson(1, 14, 30, 60);
        Person person = new PersonBuilder().withNextLesson(lesson).build();

        assertEquals(Optional.of(lesson), person.getNextLesson());
    }

    @Test
    public void getRecurringLesson_noLesson_returnsEmpty() {
        assertEquals(Optional.empty(), new PersonBuilder().build().getRecurringLesson());
    }

    @Test
    public void getRecurringLesson_lessonScheduled_returnsLesson() {
        RecurringLesson lesson = new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 30), "Mathematics",
                new LessonDuration("60"));
        Person person = new PersonBuilder().withRecurringLesson(lesson).build();

        assertEquals(Optional.of(lesson), person.getRecurringLesson());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has repeated internal spaces, all other attributes same -> returns true
        String nameWithRepeatedSpaces = VALID_NAME_BOB.replace(" ", "  ");
        editedBob = new PersonBuilder(BOB).withName(nameWithRepeatedSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different school level -> returns false
        editedAlice = new PersonBuilder(ALICE).withSchoolLevel(VALID_SCHOOL_LEVEL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different subject -> returns false
        editedAlice = new PersonBuilder(ALICE).withSubject("English").build();
        assertFalse(ALICE.equals(editedAlice));

        // different note -> returns false
        editedAlice = new PersonBuilder(ALICE).withNote("Needs extra time for algebra").build();
        assertFalse(ALICE.equals(editedAlice));

        // different next lesson -> returns false
        editedAlice = new PersonBuilder(ALICE).withNextLesson(futureLesson(1, 14, 30, 60)).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withRecurringLesson(
                new RecurringLesson(DayOfWeek.MONDAY, LocalTime.of(14, 30), "Mathematics",
                        new LessonDuration("60"))).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", schoolLevel=" + ALICE.getSchoolLevel() + ", subject=" + ALICE.getSubject()
                + ", note=" + ALICE.getNote()
                + ", nextLesson=" + ALICE.getNextLesson() + ", recurringLesson=" + ALICE.getRecurringLesson()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
