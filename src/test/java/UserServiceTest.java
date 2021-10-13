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
		when(uDao.getUserByUsername(anyString())).thenReturn(null);
		
		User loggedIn = uServ.logIn("wrongName", "password");
	}
	
	@Test(expected = InvalidCredentialsException.class)
	public void testInvalidPassword() throws InvalidCredentialsException {
		User user = new User(1, "test", "password", User.Level.Customer);
		when(uDao.getUserByUsername(anyString())).thenReturn(user);
		
		User loggedIn = uServ.logIn("test", "wrongpassword");
	}
}
