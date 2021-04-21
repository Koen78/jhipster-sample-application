package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Gebruiker.
 */
@Entity
@Table(name = "gebruiker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gebruiker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "naam")
    private String naam;

    @ManyToMany(mappedBy = "gebruikers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gebruikers", "documentTypes" }, allowSetters = true)
    private Set<Overview> objectIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gebruiker id(Long id) {
        this.id = id;
        return this;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public Gebruiker objectId(Long objectId) {
        this.objectId = objectId;
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getNaam() {
        return this.naam;
    }

    public Gebruiker naam(String naam) {
        this.naam = naam;
        return this;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Set<Overview> getObjectIds() {
        return this.objectIds;
    }

    public Gebruiker objectIds(Set<Overview> overviews) {
        this.setObjectIds(overviews);
        return this;
    }

    public Gebruiker addObjectId(Overview overview) {
        this.objectIds.add(overview);
        overview.getGebruikers().add(this);
        return this;
    }

    public Gebruiker removeObjectId(Overview overview) {
        this.objectIds.remove(overview);
        overview.getGebruikers().remove(this);
        return this;
    }

    public void setObjectIds(Set<Overview> overviews) {
        if (this.objectIds != null) {
            this.objectIds.forEach(i -> i.removeGebruiker(this));
        }
        if (overviews != null) {
            overviews.forEach(i -> i.addGebruiker(this));
        }
        this.objectIds = overviews;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gebruiker)) {
            return false;
        }
        return id != null && id.equals(((Gebruiker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gebruiker{" +
            "id=" + getId() +
            ", objectId=" + getObjectId() +
            ", naam='" + getNaam() + "'" +
            "}";
    }
}
