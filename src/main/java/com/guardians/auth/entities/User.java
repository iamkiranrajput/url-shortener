package com.guardians.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guardians.model.UrlMapping;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "The name field cant be blank")
    private String name;
    @NotBlank(message = "The name field cant be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "The name field cant be blank")
    @Column(unique = true)
    @Email(message = "Please enter email in proper format")
    private String email;

    @NotBlank(message = "The name field cant be blank")
    @Size(min = 8, message = "The password must have at least 8 Characters")
    private String password;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isEnabled=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isAccountNonExpired=true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // This prevents serialization of the urls field
    private List<UrlMapping> urls;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
