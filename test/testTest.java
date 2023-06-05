package test;

import org.junit.Test;

import static org.junit.Assert.*;

public class testTest {

    @Test
    public void testAddition() {
        int a = 2;
        int b = 3;
        int result = a + b;
        assertEquals(5, result);
    }

    @Test
    public void testDivision() {
        int a = 10;
        int b = 2;
        int result = a / b;
        assertEquals(5, result);
    }
}
