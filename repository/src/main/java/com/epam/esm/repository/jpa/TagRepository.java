package com.epam.esm.repository.jpa;

import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Integer> {

    //    @Query("select t from Tag t join Certificate c where c.id = : certificateId")
    @Query("select o.tags from Certificate o where  o.id = :certificateId")
    Page<Tag> getAllByCertificateId(@Param("certificateId") Long certificateId, Pageable pageable);

    @Query("select t from Tag t where t.name = :name")
    Optional<Tag> getByName(@Param("name") String name);

    @Modifying
    @Query(value = "delete from cert_tag where tag_id = :tagId", nativeQuery = true)
    void removeFromRelationByTagId(@Param("tagId") Integer tagId);
}
