package org.example.socialnetwork.entities;

import org.example.socialnetwork.entities.enums.Role;
import org.example.socialnetwork.entities.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder(setterPrefix = "add")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String username;
    private String email;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;
}
