package org.example.socialnetwork.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "add")
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private UUID id;
    private String token;
    private String ipAddress;
    private LocalDateTime expires;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
