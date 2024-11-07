package org.cws.web.software.engineer.task.persistence.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long    id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User    user;

    @Column(name = "token", nullable = false, unique = true)
    private String  token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

}
