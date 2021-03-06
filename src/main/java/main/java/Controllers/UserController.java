package main.java.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import main.java.Classes.User;
import main.java.Services.Users.IUserService;

@RestController
public class UserController {
	@Autowired
	private IUserService userService;
	
	@GetMapping("all-users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> list = userService.getAllUsers();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}
	
	@GetMapping("user")
	public ResponseEntity<User> getUserById(@RequestParam("id") String id) {
		User user = userService.getUserById(Integer.parseInt(id));
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping("user")
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder builder) {
		boolean flag = userService.createUser(user);
		if (flag == false) {
		     return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/user?id={id}").buildAndExpand(user.getUserId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@PutMapping("user")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		userService.updateUser(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@DeleteMapping("user")
	public ResponseEntity<Void> deleteUser(@RequestParam("id") String id) {
		userService.deleteUser(Integer.parseInt(id));
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("login")
	public ResponseEntity<User> loginVerify(@RequestParam Map<String, String> requestParams) {
		String username = requestParams.get("username");
		String password = requestParams.get("password");
		boolean flag = userService.verifyLogin(username, password);
		if (flag == false) {
		     return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		User user = userService.getUserWithLogin(username, password);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
} 