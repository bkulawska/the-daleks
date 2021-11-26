import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JUnitTest {

    @Test
    void exampleTest() {
        assertEquals(1, 1);
        assertNotEquals(420, 1337);
    }

    @Test
    void exampleTestWithMock() {
        var listMock = mock(LinkedList.class);

        when(listMock.isEmpty()).thenReturn(false);

        assertFalse(listMock.isEmpty());
    }
}
