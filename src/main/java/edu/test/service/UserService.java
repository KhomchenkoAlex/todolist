package edu.test.service;

import edu.test.dto.UserDto;
import edu.test.model.Task;
import edu.test.model.User;
import edu.test.repository.UserRepository;
import edu.test.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setTasks(addTasks(user, userDto.getTasks()));
        user.setRole(Role.valueOf(userDto.getRole()));
        return user;
    }

    private List<Task> addTasks(User user, List<String> taskNameList) {
        List<Task> tasks = new ArrayList<>();
        taskNameList.forEach(element -> {
            tasks.add(new Task(element, user));
        });
        return tasks;
    }

    public User getCurrentUser() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return getUserByUsername(userName).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username=%s was not found", userName)));
    }
}