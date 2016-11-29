package io.academic.entity;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface OaiRecordRepository extends PagingAndSortingRepository<OaiRecord, UUID> {

    List<OaiRecord> findById(UUID id);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
     OaiRecord findOne(UUID id);
     Iterable<OaiRecord> findAll();


}
