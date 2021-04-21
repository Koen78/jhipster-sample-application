package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Overview.
 */
@Entity
@Table(name = "overview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Overview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_overview__gebruiker",
        joinColumns = @JoinColumn(name = "overview_id"),
        inverseJoinColumns = @JoinColumn(name = "gebruiker_id")
    )
    @JsonIgnoreProperties(value = { "objectIds" }, allowSetters = true)
    private Set<Gebruiker> gebruikers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_overview__document_type",
        joinColumns = @JoinColumn(name = "overview_id"),
        inverseJoinColumns = @JoinColumn(name = "document_type_id")
    )
    @JsonIgnoreProperties(value = { "objectIds" }, allowSetters = true)
    private Set<DocumentType> documentTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Overview id(Long id) {
        this.id = id;
        return this;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public Overview objectId(Long objectId) {
        this.objectId = objectId;
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return this.name;
    }

    public Overview name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Gebruiker> getGebruikers() {
        return this.gebruikers;
    }

    public Overview gebruikers(Set<Gebruiker> gebruikers) {
        this.setGebruikers(gebruikers);
        return this;
    }

    public Overview addGebruiker(Gebruiker gebruiker) {
        this.gebruikers.add(gebruiker);
        gebruiker.getObjectIds().add(this);
        return this;
    }

    public Overview removeGebruiker(Gebruiker gebruiker) {
        this.gebruikers.remove(gebruiker);
        gebruiker.getObjectIds().remove(this);
        return this;
    }

    public void setGebruikers(Set<Gebruiker> gebruikers) {
        this.gebruikers = gebruikers;
    }

    public Set<DocumentType> getDocumentTypes() {
        return this.documentTypes;
    }

    public Overview documentTypes(Set<DocumentType> documentTypes) {
        this.setDocumentTypes(documentTypes);
        return this;
    }

    public Overview addDocumentType(DocumentType documentType) {
        this.documentTypes.add(documentType);
        documentType.getObjectIds().add(this);
        return this;
    }

    public Overview removeDocumentType(DocumentType documentType) {
        this.documentTypes.remove(documentType);
        documentType.getObjectIds().remove(this);
        return this;
    }

    public void setDocumentTypes(Set<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Overview)) {
            return false;
        }
        return id != null && id.equals(((Overview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Overview{" +
            "id=" + getId() +
            ", objectId=" + getObjectId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
