package io.academic.entity;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OaiRecordRepository extends PagingAndSortingRepository<OaiRecord, Long> {
    List<OaiRecord> findById(Long Id);
}
