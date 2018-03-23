import com.turboconsulting.DAO.AccountDao;
import com.turboconsulting.DAO.ExperimentDao;
import com.turboconsulting.DAO.VisitorDao;
import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.LoginDetails;
import com.turboconsulting.Service.ConsentService;
import com.turboconsulting.Service.ConsentServiceInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@RunWith(SpringRunner.class)
public class AccountServiceTests {

    @Autowired
    private ConsentService consentService;

    @MockBean
    private AccountDao accountDao;

    @MockBean
    private ExperimentDao experimentDao;
    @MockBean
    private VisitorDao visitorDao;


    @TestConfiguration
    static class ConsentServiceImplTestContextConfiguration {

        @Bean
        public ConsentServiceInterface consentService(){
            return new ConsentService();
        }

    }

    @Before
    public void setup() {
        ArrayList<Account> accounts = new ArrayList<>();
        Account newAccount = new Account("Harry", "harry@bristol.ac.uk", "password");
        newAccount.setAccountId(1);
        accounts.add(newAccount);
        Mockito.when(accountDao.findByAccountId(newAccount.getAccountId())).thenReturn(newAccount);
        Mockito.when(accountDao.findByEmail(newAccount.getEmail())).thenReturn(newAccount);


        newAccount = new Account("Finn", "finn@bristol.ac.uk", "password");
        newAccount.setAccountId(2);
        accounts.add(newAccount);
        Mockito.when(accountDao.findByAccountId(newAccount.getAccountId())).thenReturn(newAccount);
        Mockito.when(accountDao.findByEmail(newAccount.getEmail())).thenReturn(newAccount);

        newAccount = new Account("Yeap", "yeap@bristol.ac.uk", "password");
        newAccount.setAccountId(3);
        accounts.add(newAccount);
        Mockito.when(accountDao.findByAccountId(newAccount.getAccountId())).thenReturn(newAccount);
        Mockito.when(accountDao.findByEmail(newAccount.getEmail())).thenReturn(newAccount);

        Mockito.when(accountDao.save(any(Account.class))).thenAnswer(AdditionalAnswers.<Account>returnsFirstArg());


        Mockito.when(accountDao.findAll()).thenReturn(accounts);
    }

    @Test
    public void getAccount_withValidId() {
        Account found = consentService.getAccount(1);
        assertEquals(found.getName(), "Harry");
        found = consentService.getAccount(2);
        assertEquals(found.getName(), "Finn");
        found = consentService.getAccount(3);
        assertEquals(found.getName(), "Yeap");

    }
    @Test
    public void getAccount_withInvalidId() {
        assertEquals(consentService.getAccount(-1), null);
        assertEquals(consentService.getAccount(0), null);
        assertEquals(consentService.getAccount(1000), null);

    }

    @Test
    public void getAccountId_withValidEmail() {
        assertEquals(1, consentService.getAccountID("harry@bristol.ac.uk"));
        assertEquals(2, consentService.getAccountID("finn@bristol.ac.uk"));

    }
    @Test
    public void getAccountId_withInvalidEmail() {
        assertEquals(-1, consentService.getAccountID("leechay@bristol.ac.uk"));
        assertEquals(-1, consentService.getAccountID("tony@bristol.ac.uk"));

    }

    @Test
    public void checkAccountLogin_withValidLogin()  {
        LoginDetails loginDetails = new LoginDetails("harry@bristol.ac.uk", "password");
        assertTrue(consentService.checkAccountLogin(loginDetails));
        loginDetails = new LoginDetails("yeap@bristol.ac.uk", "password");
        assertTrue(consentService.checkAccountLogin(loginDetails));
    }
    @Test
    public void checkAccountLogin_withInvalidLogin()  {
        LoginDetails loginDetails = new LoginDetails("leechay@bristol.ac.uk", "password");
        assertFalse(consentService.checkAccountLogin(loginDetails));
        loginDetails = new LoginDetails("yeap@bristol.ac.uk", "password2");
        assertFalse(consentService.checkAccountLogin(loginDetails));
    }


    @Test
    public void addNewAccount_success()  {
        Account a = new Account("Harry", "harry@bristol.ac.uk", "password");
        assertTrue(consentService.addNewAccount(a));
    }


}
