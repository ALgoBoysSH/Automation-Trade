package com.example.autotrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String brokerUserId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String apiKey;

    @Column(nullable = false)
    private String encryptedApiSecret;

    @Column(nullable = false)
    private String encryptedAccessToken;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
