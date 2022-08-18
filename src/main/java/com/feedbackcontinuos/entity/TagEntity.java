package com.feedbackcontinuos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TAG")
    @SequenceGenerator(name = "SEQ_TAG", sequenceName = "seq_tags", allocationSize = 1)
    @Column(name = "id_tag")
    private Integer idTag;

    @Column(name = "name_tag")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "feedback_tags",
            joinColumns = @JoinColumn(name = "id_tag"),
            inverseJoinColumns = @JoinColumn(name = "id_feedback"))
    private List<FeedBackEntity> feedBackEntities;
}
