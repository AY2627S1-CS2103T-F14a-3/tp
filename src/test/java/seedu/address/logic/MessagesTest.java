package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MessagesTest {

    @Test
    public void getStudentsListedOverview_zeroStudents_usesPluralForm() {
        assertEquals("0 students listed!", Messages.getStudentsListedOverview(0));
    }

    @Test
    public void getStudentsListedOverview_oneStudent_usesSingularForm() {
        assertEquals("1 student listed!", Messages.getStudentsListedOverview(1));
    }

    @Test
    public void getStudentsListedOverview_multipleStudents_usesPluralForm() {
        assertEquals("2 students listed!", Messages.getStudentsListedOverview(2));
    }
}
