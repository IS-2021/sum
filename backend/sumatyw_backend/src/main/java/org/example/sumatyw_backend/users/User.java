package org.example.sumatyw_backend.users;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.favourites.Favourite;
import org.example.sumatyw_backend.opinions.Opinion;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "user_details")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String firstName;
    private String secondName;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String phoneNumber;

    private boolean blocked;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Restaurant restaurant;

    @ManyToOne(optional = true)
    @JoinColumn(name = "address_id", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<Opinion> opinions;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Favourite> favourites;

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
