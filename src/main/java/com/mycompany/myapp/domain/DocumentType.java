package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentType.
 */
@Entity
@Table(name = "document_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "code")
    private String code;

    @ManyToMany(mappedBy = "documentTypes")
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

    public DocumentType id(Long id) {
        this.id = id;
        return this;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public DocumentType objectId(Long objectId) {
        this.objectId = objectId;
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getCode() {
        return this.code;
    }

    public DocumentType code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Overview> getObjectIds() {
        return this.objectIds;
    }

    public DocumentType objectIds(Set<Overview> overviews) {
        this.setObjectIds(overviews);
        return this;
    }

    public DocumentType addObjectId(Overview overview) {
        this.objectIds.add(overview);
        overview.getDocumentTypes().add(this);
        return this;
    }

    public DocumentType removeObjectId(Overview overview) {
        this.objectIds.remove(overview);
        overview.getDocumentTypes().remove(this);
        return this;
    }

    public void setObjectIds(Set<Overview> overviews) {
        if (this.objectIds != null) {
            this.objectIds.forEach(i -> i.removeDocumentType(this));
        }
        if (overviews != null) {
            overviews.forEach(i -> i.addDocumentType(this));
        }
        this.objectIds = overviews;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", objectId=" + getObjectId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
