package com.cooperative.model;

import com.cooperative.VoteTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"agenda_id", "user_id"})
})
public class Vote implements Serializable {

    @Serial
    private static final long serialVersionUID = 4330081576853404892L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteTypeEnum voteType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;
}
