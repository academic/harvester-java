package io.academic.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface OaiDataProviderRepository extends PagingAndSortingRepository<OaiDataProvider, UUID> {


    List<OaiDataProvider> findByName(String name);

    @Override
    Page<OaiDataProvider> findAll(Pageable pageable);
}
