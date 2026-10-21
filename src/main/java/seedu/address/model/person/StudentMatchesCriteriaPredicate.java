package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests whether a {@code Person} matches all supplied academic-information and tag criteria.
 */
public class StudentMatchesCriteriaPredicate implements Predicate<Person> {
    private final Optional<SchoolLevel> schoolLevel;
    private final Optional<Subject> subject;
    private final Set<Tag> tags;

    /**
     * Creates a predicate with optional school-level and subject criteria, and tag criteria.
     */
    public StudentMatchesCriteriaPredicate(Optional<SchoolLevel> schoolLevel, Optional<Subject> subject,
            Set<Tag> tags) {
        this.schoolLevel = requireNonNull(schoolLevel);
        this.subject = requireNonNull(subject);
        this.tags = Set.copyOf(requireNonNull(tags));
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        return schoolLevel.map(person.getSchoolLevel()::equals).orElse(true)
                && subject.map(person.getSubject()::equals).orElse(true)
                && person.getTags().containsAll(tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StudentMatchesCriteriaPredicate otherPredicate)) {
            return false;
        }

        return schoolLevel.equals(otherPredicate.schoolLevel)
                && subject.equals(otherPredicate.subject)
                && tags.equals(otherPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("schoolLevel", schoolLevel)
                .add("subject", subject)
                .add("tags", tags)
                .toString();
    }
}
