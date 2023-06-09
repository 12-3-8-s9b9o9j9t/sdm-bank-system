package bank;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.exception.BankAlreadyRegisteredAtIbpaException;
import bank.ibpa.InterBankPaymentAgencyMediator;

public class TestBank {

    private Bank bank = null;
    private InterBankPaymentAgencyMediator mockIbpa = null;

    @Before
    public void setUp() {
        bank = new Bank("TestBANK");
        mockIbpa = mock(InterBankPaymentAgencyMediator.class);
    }

    @Test(expected = BankAlreadyRegisteredAtIbpaException.class)
    public void test_registerAtIbpa_alreadyRegistered() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        Field fIbpas = bank.getClass().getDeclaredField("ibpas");
        fIbpas.setAccessible(true);
        Map<String, InterBankPaymentAgencyMediator> ibpas = 
            (HashMap<String, InterBankPaymentAgencyMediator>) fIbpas.get(bank);
        ibpas.put("TESTBANK0", mockIbpa);
        bank.registerAtIbpa(mockIbpa);
    }

    @Test
    public void test_registerAtIbpa() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        bank.registerAtIbpa(mockIbpa);
        verify(mockIbpa, times(1)).notify(bank, "register");
    }

    @After
    public void tearDown() {
        bank = null;
        mockIbpa = null;
    }
    
}
