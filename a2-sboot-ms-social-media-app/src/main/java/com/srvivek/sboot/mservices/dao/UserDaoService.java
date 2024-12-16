package com.srvivek.sboot.mservices.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.srvivek.sboot.mservices.bean.User;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();

	private static int usersCount = 0;

	static {
		users.add(new User(++usersCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++usersCount, "Rahul", LocalDate.now().minusYears(25)));
		users.add(new User(++usersCount, "Nandini", LocalDate.now().minusYears(20)));
	}

	/**
	 * Fetch all users.
	 * 
	 * @return
	 */
	public List<User> findAll() {
		return users;
	}

	/**
	 * Find user with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public User findById(Integer id) {

		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}

	/**
	 * Save user.
	 * 
	 * @param user
	 * @return
	 */
	public User save(User user) {

		user.setId(++usersCount);
		users.add(user);

		return findById(user.getId());
	}

	/**
	 * Delete user with given id.
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(Integer id) {

		final Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.removeIf(predicate);
	}
}
