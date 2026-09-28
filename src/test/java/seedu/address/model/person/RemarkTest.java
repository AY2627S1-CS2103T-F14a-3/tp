package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void equals() {
        Remark remark = new Remark("Needs extra practice");

        assertTrue(remark.equals(new Remark("Needs extra practice")));
        assertTrue(remark.equals(remark));
        assertFalse(remark.equals(null));
        assertFalse(remark.equals("Needs extra practice"));
        assertFalse(remark.equals(new Remark("Different remark")));
    }

    @Test
    public void toString_returnsValue() {
        assertEquals("Needs extra practice", new Remark("Needs extra practice").toString());
    }
}
