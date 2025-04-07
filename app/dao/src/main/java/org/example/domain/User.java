package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    // Это поле оставляем, если оно используется для другой логики (например, для хранения последнего друга)
    @ManyToOne
    @JoinColumn(name = "friend_of_id")
    private User lastFriend;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    /**
     * Добавляет друга только в коллекцию текущего пользователя.
     * Обратите внимание, что мы НЕ добавляем текущего пользователя в коллекцию друга,
     * чтобы избежать двойного сохранения и повторной вставки связи.
     */
    public void addFriend(User friend) {
        this.friends.add(friend);
    }

    /**
     * Удаляет друга из коллекции текущего пользователя.
     * Здесь также изменяем только свою коллекцию.
     */
    public void removeFriend(User friend) {
        this.friends.remove(friend);
    }
}
