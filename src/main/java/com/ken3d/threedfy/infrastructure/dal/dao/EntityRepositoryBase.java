package com.ken3d.threedfy.infrastructure.dal.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EntityRepositoryBase<BaseType extends Serializable> {

  private static final String SELECT_QUERY = "from ";
  private final SessionFactory sessionFactory;

  @Autowired
  public EntityRepositoryBase(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public <T extends BaseType> Optional<T> select(Class<T> type, final long id) {
    return Optional.ofNullable(getCurrentSession().get(type, id));
  }

  public <T extends BaseType> Optional<T> select(Class<T> type, Function<T, Boolean> where) {
    return selectAll(type).stream().filter(where::apply).reduce((a, b) -> null);
  }

  public <T extends BaseType> List<T> selectAll(Class<T> type) {
    return getCurrentSession().createQuery(SELECT_QUERY + type.getName(), type).list();
  }

  public <T extends BaseType> List<T> selectAll(Class<T> type, Function<T, Boolean> where) {
    return selectAll(type).stream().filter(where::apply).collect(Collectors.toList());
  }

  public <T extends BaseType> T create(final T entity) {
    getCurrentSession().saveOrUpdate(entity);
    return entity;
  }

  public <T extends BaseType> List<T> createMany(final List<T> entities) {
    for (T entity : entities) {
      entity = create(entity);
    }
    return entities;
  }

  @SuppressWarnings("unchecked")
  public <T extends BaseType> T update(final T entity) {
    return (T) getCurrentSession().merge(entity);
  }

  public <T extends BaseType> List<T> updateMany(final List<T> entities) {
    for (T entity : entities) {
      entity = update(entity);
    }
    return entities;
  }

  public <T extends BaseType> void delete(final T entity) {
    getCurrentSession().delete(entity);
  }

  public <T extends BaseType> void delete(Class<T> type, final long entityId) {
    select(type, entityId).ifPresent(this::delete);
  }

  protected Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }
}
