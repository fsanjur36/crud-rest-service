package com.example.demo;

import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudProjectApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrudProjectApplicationTests {
	
	@Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }
    
    @Test
    public void contextLoads() {

    }

    @Test
    public void testFindAllUsers(){
    	HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/v1/users",
        HttpMethod.GET, entity, String.class);  
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetUserById() {
    	User user = restTemplate.getForObject(getRootUrl() + "/api/v1/users/1", User.class);
        assertNotNull(user);
    }

    @Test
    public void testCreateUser() {
    	User user = new User();
    	user.setName("Pablo");
    	user.setBirthdate(LocalDate.of(1978, 2, 21));
        ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/v1/users", user, User.class);
        assertNotNull(postResponse);
        assertEquals(postResponse.getStatusCode(),HttpStatus.CREATED);
    }

    @Test
    public void testUpdateUser() {
        int id = 1;
        User user = restTemplate.getForObject(getRootUrl() + "/api/v1/users/" + id, User.class);
        user.setName("Fernando");
        user.setBirthdate(LocalDate.of(1978, 2, 20));
        restTemplate.put(getRootUrl() + "/api/v1/users/" + id, user);
        User updatedUser = restTemplate.getForObject(getRootUrl() + "/api/v1/users/" + id, User.class);
        assertNotNull(updatedUser);
    }

    @Test
    public void testDeleteUser() {
         int id = 2;
         User user = restTemplate.getForObject(getRootUrl() + "/api/v1/users/" + id, User.class);
         assertNotNull(user);
         restTemplate.delete(getRootUrl() + "/api/v1/users/" + id);
         try {
        	 user = restTemplate.getForObject(getRootUrl() + "/api/v1/users/" + id, User.class);
         } catch (final UserNotFoundException e) {
        	 assertEquals(e.getMessage(), "User not found for this id :: " + id);
         }
    }


}
