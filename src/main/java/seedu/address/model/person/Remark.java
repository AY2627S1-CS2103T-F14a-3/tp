package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents an optional remark associated with a person.
 */
public class Remark {

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param value remark text; an empty string means that no remark is recorded
     */
    public Remark(String value) {
        requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Remark otherRemark && value.equals(otherRemark.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
