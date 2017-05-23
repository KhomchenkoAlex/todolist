package edu.test;

import com.esotericsoftware.yamlbeans.YamlReader;
import edu.test.dto.UserDto;
import edu.test.model.User;
import edu.test.repository.UserRepository;
import edu.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;

@Component
public class Bootstrap {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    private void initUsers() {

        try {
            Resource resource = new ClassPathResource("init/initial_users.yml");
            YamlReader reader = new YamlReader(new FileReader(resource.getFile()));
            while (true) {
                UserDto userDto = reader.read(UserDto.class);
                if (userDto == null) break;
                User user = userService.createUser(userDto);
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
