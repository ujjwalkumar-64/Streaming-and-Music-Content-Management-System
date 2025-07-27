package com.example.registrationProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String permission_name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "permissions",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "userPermissions")
    @JsonIgnore
    private List<User> users;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime updatedAt;

}
