package com.ken3d.threedfy.domain.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface IEntityRepository<BaseType extends Serializable> {

  <T extends BaseType> Optional<T> select(Class<T> type, final long id);

  <T extends BaseType> Optional<T> select(Class<T> type, Function<T, Boolean> where);

  <T extends BaseType> List<T> selectAll(Class<T> type);

  <T extends BaseType> List<T> selectAll(Class<T> type, Function<T, Boolean> where);

  <T extends BaseType> T create(final T entity);

  <T extends BaseType> List<T> createMany(final List<T> entities);

  <T extends BaseType> T update(final T entity);

  <T extends BaseType> List<T> updateMany(final List<T> entities);

  <T extends BaseType> void delete(final T entity);

  <T extends BaseType> void delete(Class<T> type, final long entityId);
}
