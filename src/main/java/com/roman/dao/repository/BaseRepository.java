package com.roman.dao.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T,K>{

    List<K> getAll();

    Optional<K> findById(T id);

    K save(K entity);

    K update(K entity);

    boolean delete(T id);
}
