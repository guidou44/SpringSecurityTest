package com.ken3d.threedfy.domain.dao;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface IRepository {

  <T extends Serializable> T select(final long id);

  <T extends Serializable> T select(T e, Function<Boolean, T> where);

  <T extends Serializable> List<T> selectAll();

  <T extends Serializable> List<T> selectAll(T e, Function<Boolean, T> where);

  <T extends Serializable> void create(final T entity);

  <T extends Serializable> void createMany(final List<T> entities);

  <T extends Serializable> T update(final T entity);

  <T extends Serializable> T updateMany(final List<T> entities);

  <T extends Serializable> void delete(final T entity);

  <T extends Serializable> void delete(final long entityId);
}
