package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final int MAX_LENGTH = 100;
    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters, spaces, hyphens, and apostrophes, "
                    + "should not be blank, and should use hyphens and apostrophes only between "
                    + "alphanumeric characters. Names can be at most " + MAX_LENGTH + " characters long";

    /*
     * Each hyphen or apostrophe must join two alphanumeric name segments. Spaces may separate name segments,
     * while the first character must be alphanumeric.
     */
    public static final String VALIDATION_REGEX =
            "[\\p{L}\\p{N}]+(?:['-][\\p{L}\\p{N}]+)*(?: +[\\p{L}\\p{N}]+(?:['-][\\p{L}\\p{N}]+)*)* *";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.codePointCount(0, test.length()) <= MAX_LENGTH && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if both names are equivalent for duplicate detection.
     * Comparison is case-insensitive, ignores surrounding spaces, and treats consecutive spaces as one space.
     */
    public boolean isSameName(Name otherName) {
        if (otherName == null) {
            return false;
        }

        return normalizeForComparison(fullName).equals(normalizeForComparison(otherName.fullName));
    }

    private static String normalizeForComparison(String name) {
        return name.trim().replaceAll(" +", " ").toLowerCase(Locale.ROOT);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
