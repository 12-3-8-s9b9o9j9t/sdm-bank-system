package bank.interest;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class TestAInterestStrategy {
    

    @Test
    public void test_getDurationInYears() {
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.of(2018, 12, 31);
        double duration = AInterestStrategy.getDurationInYears(startDate, endDate);
        assertEquals(1.0, duration, 0.01);
    }

    @Test
    public void test_getDurationInYears_2() {
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 1);
        double duration = AInterestStrategy.getDurationInYears(startDate, endDate);
        assertEquals(4.0, duration, 0.01);
    }

    @Test
    public void test_getDurationInYears_3() {
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.of(2018, 7, 1);
        double duration = AInterestStrategy.getDurationInYears(startDate, endDate);
        assertEquals(0.5, duration, 0.01);
    }
}
