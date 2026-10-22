package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class StudentMatchesCriteriaPredicateTest {

    @Test
    public void equals() {
        StudentMatchesCriteriaPredicate firstPredicate = new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.empty(), Set.of());
        StudentMatchesCriteriaPredicate secondPredicate = new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.of(new Subject("Mathematics")), Set.of());

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.empty(), Set.of())));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_noCriteria_returnsTrue() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.empty(), Optional.empty(), Set.of());

        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_matchingCriteria_returnsTrue() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.of(new Subject("Mathematics")),
                Set.of(new Tag("weekday"), new Tag("exam")));
        Person person = new PersonBuilder().withSchoolLevel("Secondary 4").withSubject("Mathematics")
                .withTags("weekday", "exam", "home").build();

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_someRequiredTagsMissing_returnsFalse() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.of(new Subject("Mathematics")),
                Set.of(new Tag("weekday"), new Tag("exam")));
        Person person = new PersonBuilder().withSchoolLevel("Secondary 4").withSubject("Mathematics")
                .withTags("weekday", "home").build();

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_nonMatchingCriterion_returnsFalse() {
        Person person = new PersonBuilder().withSchoolLevel("Secondary 4").withSubject("Mathematics")
                .withTags("weekday").build();

        assertFalse(new StudentMatchesCriteriaPredicate(Optional.of(new SchoolLevel("Junior College 1")),
                Optional.empty(), Set.of()).test(person));
        assertFalse(new StudentMatchesCriteriaPredicate(Optional.empty(), Optional.of(new Subject("Physics")), Set.of())
                .test(person));
        assertFalse(new StudentMatchesCriteriaPredicate(Optional.empty(), Optional.empty(), Set.of(new Tag("exam")))
                .test(person));
    }

    @Test
    public void toStringMethod() {
        StudentMatchesCriteriaPredicate predicate = new StudentMatchesCriteriaPredicate(
                Optional.of(new SchoolLevel("Secondary 4")), Optional.of(new Subject("Mathematics")),
                Set.of(new Tag("weekday")));

        String expected = StudentMatchesCriteriaPredicate.class.getCanonicalName()
                + "{schoolLevel=Optional[Secondary 4], subject=Optional[Mathematics], tags=[[weekday]]}";
        assertEquals(expected, predicate.toString());
    }
}
