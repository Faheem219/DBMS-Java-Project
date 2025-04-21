package com.faheemtrading.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, K> {
    Optional<T> get(K id);
    List<T>     getAll();
    boolean     insert(T t);
    boolean     update(T t);
    boolean     delete(K id);
}
