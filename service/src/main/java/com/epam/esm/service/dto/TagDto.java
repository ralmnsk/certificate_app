package com.epam.esm.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class TagDto extends Dto<Long> {

    @NotNull
    @Size(min = 2, max = 190, message
            = "Name must be between 2 and 190 characters")
    private String name;

    private Set<CertificateDto> certificateDtos = new HashSet<CertificateDto>();

    public TagDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CertificateDto> getCertificateDtos() {
        return certificateDtos;
    }

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
