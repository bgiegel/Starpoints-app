package fr.softeam.starpointsapp.domain;

import fr.softeam.starpointsapp.domain.enumeration.ActivityType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ActivityType type;

    /**
     * Définition du livrable
     *
     */
    @ApiModelProperty(value = ""
        + "Définition du livrable                                             "
        + "")
    @Column(name = "deliverable_definition")
    private String deliverableDefinition;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Level level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getDeliverableDefinition() {
        return deliverableDefinition;
    }

    public void setDeliverableDefinition(String deliverableDefinition) {
        this.deliverableDefinition = deliverableDefinition;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activity activity = (Activity) o;
        if(activity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", deliverableDefinition='" + deliverableDefinition + "'" +
            '}';
    }
}
