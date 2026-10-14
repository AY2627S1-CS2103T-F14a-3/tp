package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final SchoolLevel schoolLevel;
    private final Subject subject;
    private final StudentNote note;
    private final Optional<NextLesson> nextLesson;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, new SchoolLevel(SchoolLevel.DEFAULT_VALUE),
                new Subject(Subject.DEFAULT_VALUE), new StudentNote(""), Optional.empty(), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, new Subject(Subject.DEFAULT_VALUE), new StudentNote(""),
                Optional.empty(), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, StudentNote note,
            Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, new Subject(Subject.DEFAULT_VALUE), note,
                Optional.empty(), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, StudentNote note,
            Optional<NextLesson> nextLesson, Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, new Subject(Subject.DEFAULT_VALUE), note, nextLesson, tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, Subject subject,
            StudentNote note, Optional<NextLesson> nextLesson, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, schoolLevel, subject, note, nextLesson, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.schoolLevel = schoolLevel;
        this.subject = subject;
        this.note = note;
        this.nextLesson = nextLesson;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public SchoolLevel getSchoolLevel() {
        return schoolLevel;
    }

    public Subject getSubject() {
        return subject;
    }

    public StudentNote getNote() {
        return note;
    }

    /**
     * Returns the next lesson, if one has been scheduled.
     */
    public Optional<NextLesson> getNextLesson() {
        return nextLesson;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && schoolLevel.equals(otherPerson.schoolLevel)
                && subject.equals(otherPerson.subject)
                && note.equals(otherPerson.note)
                && nextLesson.equals(otherPerson.nextLesson)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, schoolLevel, subject, note, nextLesson, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("schoolLevel", schoolLevel)
                .add("subject", subject)
                .add("note", note)
                .add("nextLesson", nextLesson)
                .add("tags", tags)
                .toString();
    }

}
