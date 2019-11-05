package com.example.demo.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAPI {

	@Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) throws UserNotFoundException {
    	User user = userService.findById(id)
    	           .orElseThrow(() -> new UserNotFoundException("User not found for this id --> " + id));
    	return ResponseEntity.ok().body(user);
    }
    
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User userCreated = userService.save(user);
    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
    			      .buildAndExpand(userCreated.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User userDetails) throws UserNotFoundException {
    	User user = userService.findById(id)
    	           .orElseThrow(() -> new UserNotFoundException("User not found for this id --> " + id));
    	user.setName(userDetails.getName());
    	user.setBirthdate(userDetails.getBirthdate());
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws UserNotFoundException {
    	userService.findById(id)
    	           .orElseThrow(() -> new UserNotFoundException("User not found for this id --> " + id));
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
	
}
