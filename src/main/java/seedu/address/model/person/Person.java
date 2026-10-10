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
    private final StudentNote note;
    private final Remark remark;
    private final Optional<NextLesson> nextLesson;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, new SchoolLevel(SchoolLevel.DEFAULT_VALUE), new StudentNote(""), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, new StudentNote(""), new Remark(""), Optional.empty(), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, StudentNote note,
            Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, note, new Remark(""), Optional.empty(), tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, StudentNote note,
            Optional<NextLesson> nextLesson, Set<Tag> tags) {
        this(name, phone, email, address, schoolLevel, note, new Remark(""), nextLesson, tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, SchoolLevel schoolLevel, StudentNote note,
            Remark remark, Optional<NextLesson> nextLesson, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, schoolLevel, note, remark, nextLesson, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.schoolLevel = schoolLevel;
        this.note = note;
        this.remark = remark;
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

    public StudentNote getNote() {
        return note;
    }

    public Remark getRemark() {
        return remark;
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
                && note.equals(otherPerson.note)
                && remark.equals(otherPerson.remark)
                && nextLesson.equals(otherPerson.nextLesson)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, schoolLevel, note, remark, nextLesson, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("schoolLevel", schoolLevel)
                .add("note", note)
                .add("remark", remark)
                .add("nextLesson", nextLesson)
                .add("tags", tags)
                .toString();
    }

}
