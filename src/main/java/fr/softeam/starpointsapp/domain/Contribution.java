package fr.softeam.starpointsapp.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Contribution.
 */
@Entity
@Table(name = "contribution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Date de rédaction de l’article, date de la vraie présentation
     *
     */
    @ApiModelProperty(value = ""
        + "Date de rédaction de l’article, date de la vraie présentation      "
        + "")
    @Column(name = "deliverable_date")
    private LocalDate deliverableDate;

    /**
     * Lien de l'article du blog par exemple
     *
     */
    @ApiModelProperty(value = ""
        + "Lien de l'article du blog par exemple                              "
        + "")
    @Column(name = "deliverable_url")
    private String deliverableUrl;

    /**
     * Nom de l'article ou de la présentation
     *
     */
    @ApiModelProperty(value = ""
        + "Nom de l'article ou de la présentation                             "
        + "")
    @Column(name = "deliverable_name")
    private String deliverableName;

    @Column(name = "comment")
    private String comment;

    /**
     * Date pour le plan / Teaser
     *
     */
    @ApiModelProperty(value = ""
        + "Date pour le plan / Teaser                                         "
        + "")
    @Column(name = "preparatory_date_1")
    private LocalDate preparatoryDate1;

    /**
     * Date pour la presentation a blanc
     *
     */
    @ApiModelProperty(value = ""
        + "Date pour la presentation a blanc                                  "
        + "")
    @Column(name = "preparatory_date_2")
    private LocalDate preparatoryDate2;

    @OneToOne
    @JoinColumn
    private Activity activity;

    @OneToOne
    @JoinColumn
    private Community community;

    @ManyToOne
    private Person author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDeliverableDate() {
        return deliverableDate;
    }

    public void setDeliverableDate(LocalDate deliverableDate) {
        this.deliverableDate = deliverableDate;
    }

    public String getDeliverableUrl() {
        return deliverableUrl;
    }

    public void setDeliverableUrl(String deliverableUrl) {
        this.deliverableUrl = deliverableUrl;
    }

    public String getDeliverableName() {
        return deliverableName;
    }

    public void setDeliverableName(String deliverableName) {
        this.deliverableName = deliverableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getPreparatoryDate1() {
        return preparatoryDate1;
    }

    public void setPreparatoryDate1(LocalDate preparatoryDate1) {
        this.preparatoryDate1 = preparatoryDate1;
    }

    public LocalDate getPreparatoryDate2() {
        return preparatoryDate2;
    }

    public void setPreparatoryDate2(LocalDate preparatoryDate2) {
        this.preparatoryDate2 = preparatoryDate2;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person person) {
        this.author = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contribution contribution = (Contribution) o;
        if(contribution.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contribution.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contribution{" +
            "id=" + id +
            ", deliverableDate='" + deliverableDate + "'" +
            ", deliverableUrl='" + deliverableUrl + "'" +
            ", deliverableName='" + deliverableName + "'" +
            ", comment='" + comment + "'" +
            ", preparatoryDate1='" + preparatoryDate1 + "'" +
            ", preparatoryDate2='" + preparatoryDate2 + "'" +
            '}';
    }
}
