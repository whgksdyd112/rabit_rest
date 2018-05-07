package com.cyhee.rabit.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/v1/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<Iterable<User>>(userRepository.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<UserApiErrorType> addUser(@RequestBody User user) {    	
        try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(UserApiErrorType.DUPLICATE_USER_EMAIL, HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable int id) {
    	Optional<User> userOpt = userRepository.findById(id);
    	if(userOpt.isPresent())
    		return new ResponseEntity<User>(userOpt.get(), HttpStatus.OK);
    	return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ResponseEntity<UserApiErrorType> updateUser(@PathVariable int id, @RequestBody User user) {
    	Optional<User> userOpt = userRepository.findById(id);
    	if(!userOpt.isPresent())
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	
    	user.setId(id);
        try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(UserApiErrorType.INVALID_DATA_FORM, HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(HttpStatus.CREATED); 
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
    	Optional<User> userOpt = userRepository.findById(id);
    	if(!userOpt.isPresent())
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	
        userRepository.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
