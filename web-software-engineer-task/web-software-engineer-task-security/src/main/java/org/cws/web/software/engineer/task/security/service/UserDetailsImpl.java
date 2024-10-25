package org.cws.web.software.engineer.task.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.cws.web.software.engineer.task.persistence.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {

    /**
     * 
     */
    private static final long                      serialVersionUID = -8205239870163303601L;

    private Long                                   id;

    private String                                 username;

    private String                                 email;

    @JsonIgnore
    private String                                 password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        //@formatter:off
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                                                            .stream()
                                                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                                            .toList();
        //@formatter:on

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserDetailsImpl other = (UserDetailsImpl) obj;
        return Objects.equals(id, other.id);
    }


}
