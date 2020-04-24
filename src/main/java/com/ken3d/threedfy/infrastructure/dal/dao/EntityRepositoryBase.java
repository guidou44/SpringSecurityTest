package com.ken3d.threedfy.infrastructure.dal.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

  public <T extends BaseType> Optional<T> select(Class<T> type, final int id) {
    return Optional.ofNullable(getCurrentSession().get(type, id));
  }

  public <T extends BaseType> Optional<T> select(Class<T> type, Predicate<T> where) {
    List<T> allEntities = selectAll(type, where);
    return singleOrEmpty(allEntities);
  }

  public <T extends BaseType> List<T> selectAll(Class<T> type) {
    return getCurrentSession().createQuery(SELECT_QUERY + type.getName(), type).list();
  }

  public <T extends BaseType> List<T> selectAll(Class<T> type, Predicate<T> where) {
    return selectAll(type).stream().filter(e -> tryWhere(e, where)).collect(Collectors.toList());
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

  public <T extends BaseType> void delete(Class<T> type, final int entityId) {
    select(type, entityId).ifPresent(this::delete);
  }

  protected Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  private <T extends BaseType> Optional<T> singleOrEmpty(List<T> list) {
    return list.stream().distinct().map(Optional::ofNullable).reduce(Optional.empty(),
        (a, b) -> a.isPresent() ^ b.isPresent() ? b : Optional.empty());
  }

  private <T extends BaseType> Boolean tryWhere(T entity, Predicate<T> where) {
    try {
      return where.test(entity);
    } catch (Exception e) {
      return false;
    }
  }

}
