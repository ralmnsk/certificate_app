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

//    @Query("select t from Tag t where t.certificates.id = :certificateId")
////    @Query("select o.tags from Certificate o where  o.id = :certificateId")
////        @Query("select t from Tag t join Certificate.tags c on t.id= c.id where  c.id = :certificateId")
////    @Query(value = "select id, deleted, name from tag join cert_tag ct on tag.id = ct.tag_id where  certificate_id = :certificateId", nativeQuery = true)
//    Page<Tag> getAllByCertificateId(@Param("certificateId") Long certificateId, Pageable pageable);

    @Query("select t from Tag t where t.name = :name")
    Optional<Tag> getByName(@Param("name") String name);

    @Modifying
    @Query(value = "delete from cert_tag where tag_id = :tagId", nativeQuery = true)
    void removeFromRelationByTagId(@Param("tagId") Integer tagId);
}
