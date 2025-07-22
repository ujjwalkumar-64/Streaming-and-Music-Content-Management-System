package com.example.registrationProject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime updatedAt;

    private String imageUrl;

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    @ToString.Exclude
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permission",
            joinColumns = @JoinColumn(name= "user_id"),
            inverseJoinColumns = @JoinColumn(name= "permission_id")
    )
    @ToString.Exclude
    private List<Permission> userPermissions;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Set<GrantedAuthority> authorities= new HashSet<>();


        if(role.getStatus()==Status.active){
            List<Permission> rolePermissions = role.getPermissions();
            rolePermissions.stream().filter((permission -> permission.getStatus()==Status.active))
                    .forEach((permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermission_name()))));
            authorities.add(new SimpleGrantedAuthority(role.getRole()));

        }
        if(userPermissions!=null){
            userPermissions.stream().filter((permission -> permission.getStatus()==Status.active))
                    .forEach((permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermission_name()))));

        }

        return authorities;
    }


}
