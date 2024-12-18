package org.example.socialnetwork.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.socialnetwork.entities.enums.PrivacyStatus;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "add")
@Table(name = "user_profile")
public class UserProfile {

    @Id
    private UUID id;
    private String name;

    @Lob
    private String avatarUrl;

    @Column(length = 500)
    private String description;
    private String country;
    private String city;
    private String jobTitle;

    @Column(length = 500)
    private String contactInformation;

    @Enumerated(EnumType.STRING)
    private PrivacyStatus privacyStatus = PrivacyStatus.OPEN;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
