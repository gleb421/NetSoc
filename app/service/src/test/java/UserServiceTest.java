//import org.example.UserService;
//import org.example.domain.User;
//import org.example.repositories.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Controller;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class UserServiceTest {
//
//    private final UserService userService;
//
//    private final UserRepository userRepository;
//
//    public UserServiceTest(@Autowired UserService userService, @Autowired UserRepository userRepository) {
//        this.userService = userService;
//        this.userRepository = userRepository;
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testSaveUser() {
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setActive(true);
//
//        User savedUser = userService.saveUser(user);
//
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getId()).isNotNull();
//        assertThat(savedUser.getUsername()).isEqualTo("testuser");
//
//        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUsername()).isEqualTo("testuser");
//    }
//}