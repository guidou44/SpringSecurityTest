package com.ken3d.threedfy.domain.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IEntityRepository<B extends Serializable> {

  <T extends B> Optional<T> select(Class<T> type, final int id);

  <T extends B> Optional<T> select(Class<T> type, Predicate<T> where);

  <T extends B> List<T> selectAll(Class<T> type);

  <T extends B> List<T> selectAll(Class<T> type, Predicate<T> where);

  <T extends B> T create(final T entity);

  <T extends B> List<T> createMany(final List<T> entities);

  <T extends B> T update(final T entity);

  <T extends B> List<T> updateMany(final List<T> entities);

  <T extends B> void delete(final T entity);

  <T extends B> void delete(Class<T> type, final int entityId);
}
