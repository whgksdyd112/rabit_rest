package com.cyhee.rabit.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.cyhee.rabit.user.dao.UserRepository;
import com.cyhee.rabit.user.model.User;
import com.cyhee.rabit.user.model.UserStatus;
import com.cyhee.rabit.user.service.BasicUserService;
import com.cyhee.rabit.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({BasicUserService.class, BCryptPasswordEncoder.class})
public class UserTests {
	@Autowired
	private TestEntityManager entityManger;
	@Autowired
	private UserRepository repository;	
	@Autowired
	private UserService userService;
	
	User user1;
	User user2;
	User user3;
	Date now;
	Date after;
	
	@Before
	public void setup() {
		now = new Date();
		user1 = new User("email1","password1","user1","name1","010-1234-1234", now);
		user2 = new User("email2","password2","user2","name2","010-1234-1234", now);
		user3 = new User("email3","password3","user3","name3","010-1234-1234", now);
	}

	@Test
	public void createAndGet() throws InterruptedException {
		now = new Date();
		userService.addUser(user1);
		after = new Date(now.getTime() + 1000);
		
		Optional<User> userOpt = repository.findByEmail("email1");
		
		User user = userOpt.get();
		System.out.println(user.getCreateDate());
		System.out.println(user.getLastUpdated());
		assertThat(user)
			.isEqualTo(user1)
			.extracting(User::getId, User::getCreateDate, User::getLastUpdated, User::getStatus)
				.doesNotContainNull()
				.contains(UserStatus.PENDING);
		assertThat(user)
			.extracting(User::getCreateDate, User::getLastUpdated)
			.allSatisfy(date -> {
                assertThat(now.compareTo((Date) date)).isNotPositive();
                assertThat(after.compareTo((Date) date)).isPositive();
              });
		assertThat(user.getPassword())
			.isNotEqualTo("password1");
	}
	
	
	@Test
	public void deleteAndGet() {
		userService.addUser(user1);		
		
		Optional<User> userOpt = repository.findByEmail("email1");
		
		User user = userOpt.get();
		userService.deleteUser(user.getId());
		
		userOpt = repository.findByEmail("email1");
		assertThat(userOpt.isPresent())
			.isEqualTo(false);
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void createAndGetAll() {
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		
		Iterable<User> userList = repository.findAll();
		assertThat(userList)
			.hasSize(3);

		Pageable pageable = new PageRequest(1, 2);
		Page<User> userPage = repository.findAll(pageable);
		assertThat(userPage)
			.hasSize(1)
			.containsExactly(user3);
	}
	
	/*@Test
	public void update() {
		userService.addUser(user1);
		userService.addUser(user2);	
		user1.setEmail("email2");
		user1.setName("updatedName");	
		
		Optional<User> userOpt = repository.findByEmail("email1");
		User user = userOpt.get();
		assertThat(user.getName())
			.isNotEqualTo("name1")
			.isEqualTo("updatedName");
		
		user1.setEmail("email2");
		user1.setUsername("username2");
		entityManger.persist(user1);
	}*/
}