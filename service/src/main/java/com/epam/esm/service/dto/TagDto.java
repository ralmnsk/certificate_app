package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Tag dto.
 */
public class TagDto extends Dto<Integer> {
    @JsonView(Profile.PublicView.class)
    @NotNull
    @Size(min = 2, max = 128, message
            = "Name must be between 2 and 128 characters")
    private String name;

    private Set<CertificateDto> certificateDtos = new HashSet<CertificateDto>();

    /**
     * Instantiates a new Tag dto.
     */
    public TagDto() {
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets certificate dtos.
     *
     * @return the certificate dtos
     */
    public Set<CertificateDto> getCertificateDtos() {
        return certificateDtos;
    }

    /**
     * Sets certificate dtos.
     *
     * @param certificateDtos the certificate dtos
     */
    public void setCertificateDtos(Set<CertificateDto> certificateDtos) {
        this.certificateDtos = certificateDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDto tagDto = (TagDto) o;

        return name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", certificateDtos=" + certificateDtos +
                '}';
    }
}
