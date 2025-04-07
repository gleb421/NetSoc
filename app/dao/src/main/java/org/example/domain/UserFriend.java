//package org.example.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "user_friends")
//@Getter
//@Setter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//public class UserFriend {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id")
//    private User friend;
//}
