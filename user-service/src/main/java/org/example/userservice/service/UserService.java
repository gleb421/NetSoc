package org.example.userservice.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.common.event.UserCreatedEvent;
import org.example.userservice.kafka.UserProducer;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private UserProducer producer; // добавили Kafka

    public User saveUser(User user) {
        User saved = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(saved.getId(), saved.getUsername());
        producer.sendUserCreated(event);
        return saved;
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Добавляет друга, если такой ещё не присутствует в коллекции.
     * При сохранении обновляется только коллекция текущего пользователя, что позволяет вставить в БД только новую связь.
     */
    @CacheEvict(value = "userFriends", key = "#currentUserId")
    @Transactional
    public void addFriendManually(Long currentUserId, Long friendId) {
        String sql = """
               INSERT INTO user_friends(user_id, friend_id)
               VALUES (:uId, :fId)
               ON CONFLICT DO NOTHING
            """;

        entityManager.createNativeQuery(sql)
                .setParameter("uId", currentUserId)
                .setParameter("fId", friendId)
                .executeUpdate();

        entityManager.createNativeQuery(sql)
                .setParameter("uId", friendId)
                .setParameter("fId", currentUserId)
                .executeUpdate();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entityManager.refresh(currentUser);
    }


    @Cacheable(value = "userFriends", key = "#id")
    public Set<User> getFriends(Long id) {
        System.out.println("⛏ Загружаем друзей из БД, а не из кэша...");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFriends();
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserWithFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Hibernate.initialize(user.getFriends());
        return user;
    }
}
