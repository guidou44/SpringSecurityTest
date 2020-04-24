package com.ken3d.threedfy.domain.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IEntityRepository<BaseType extends Serializable> {

  <T extends BaseType> Optional<T> select(Class<T> type, final int id);

  <T extends BaseType> Optional<T> select(Class<T> type, Predicate<T> where);

  <T extends BaseType> List<T> selectAll(Class<T> type);

  <T extends BaseType> List<T> selectAll(Class<T> type, Predicate<T> where);

  <T extends BaseType> T create(final T entity);

  <T extends BaseType> List<T> createMany(final List<T> entities);

  <T extends BaseType> T update(final T entity);

  <T extends BaseType> List<T> updateMany(final List<T> entities);

  <T extends BaseType> void delete(final T entity);

  <T extends BaseType> void delete(Class<T> type, final int entityId);
}
