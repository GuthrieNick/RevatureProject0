import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.lang.System.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.bankingSystem.dao.AccountDao;
import com.revature.bankingSystem.services.AccountService;
import com.revature.log.Logging;

public class AccountServiceTest {
	@InjectMocks
	public AccountService aServ;
	
	@Mock
	public AccountDao aDao;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void transferSuccessTest() {
		when(aDao.transfer(anyInt(), anyInt(), anyDouble())).thenReturn(true);
		assertTrue(aDao.transfer(0, 1, 20.0));
		
		when(aDao.transfer(anyInt(),anyDouble())).thenReturn(true);
		assertTrue(aDao.transfer(0, 20.0));
	}
	
	@Test
	public void transferFailTest() {
		when(aDao.transfer(anyInt(), anyInt(), anyDouble())).thenReturn(false);
		assertFalse(aDao.transfer(0, 1, 20.0));
		
		when(aDao.transfer(anyInt(),anyDouble())).thenReturn(false);
		assertFalse(aDao.transfer(0, 20.0));
	}
}

/*
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.bankingSystem.dao.UserDao;
import com.revature.bankingSystem.models.User;
import com.revature.bankingSystem.services.UserService;
import com.revature.exceptions.InvalidCredentialsException;

public class UserServiceTest {
	@InjectMocks
	public UserService uServ;
	
	@Mock
	public UserDao uDao;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidLogin() throws InvalidCredentialsException {
		User user = new User(1, "test", "password", User.Level.Customer);
		when(uDao.getUserByUsername(anyString())).thenReturn(user);
		
		User loggedIn = uServ.logIn("test", "password");
		
		assertEquals(user.getId(), loggedIn.getId());
	}
	
	@Test(expected = InvalidCredentialsException.class)
	public void testInvalidLogin() throws InvalidCredentialsException {
		User user = new User(0, "test", "password", User.Level.Customer);
		when(uDao.getUserByUsername(anyString())).thenReturn(user);
		
		User loggedIn = uServ.logIn("wrongName", "password");
	}
	
	@Test(expected = InvalidCredentialsException.class)
	public void testInvalidPassword() throws InvalidCredentialsException {
		User user = new User(1, "test", "password", User.Level.Customer);
		when(uDao.getUserByUsername(anyString())).thenReturn(user);
		
		User loggedIn = uServ.logIn("test", "wrongpassword");
	}
}
*/