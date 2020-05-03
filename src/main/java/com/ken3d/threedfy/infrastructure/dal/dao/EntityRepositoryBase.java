package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.infrastructure.dal.dao.exceptions.InvalidEntityIdException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.hibernate.Session;

public abstract class EntityRepositoryBase<B extends Serializable> {

  private static final String SELECT_QUERY = "from ";

  @PersistenceContext
  private Session session;

  public void setSession(Session session) {
    this.session = session;
  }

  public <T extends B> Optional<T> select(Class<T> type, final int id) {
    return Optional.ofNullable(session.get(type, id));
  }

  public <T extends B> Optional<T> select(Class<T> type, Predicate<T> where) {
    List<T> allEntities = selectAll(type, where);
    System.out.println("----------------DEBUG----------------------------");
    System.out.println("Request made");
    System.out.println("----------------DEBUG----------------------------");
    return singleOrEmpty(allEntities);
  }

  public <T extends B> List<T> selectAll(Class<T> type) {
    String tableName = type.getAnnotation(Table.class).name();
    return session.createQuery(SELECT_QUERY + tableName, type).list();
  }

  public <T extends B> List<T> selectAll(Class<T> type, Predicate<T> where) {
    return selectAll(type).stream().filter(e -> tryWhere(e, where)).collect(Collectors.toList());
  }

  public <T extends B> T create(final T entity) {
    session.saveOrUpdate(entity);
    return entity;
  }

  public <T extends B> List<T> createMany(final List<T> entities) {
    for (T entity : entities) {
      entity = create(entity);
    }
    return entities;
  }

  @SuppressWarnings("unchecked")
  public <T extends B> T update(final T entity) {
    return (T) session.merge(entity);
  }

  public <T extends B> List<T> updateMany(final List<T> entities) {
    for (T entity : entities) {
      entity = update(entity);
    }
    return entities;
  }

  public <T extends B> void delete(final T entity) {
    session.delete(entity);
  }

  public <T extends B> void delete(Class<T> type, final int entityId) {
    Optional<T> entity = select(type, entityId);
    if (entity.isPresent()) {
      delete(entity.get());
    } else {
      throw new InvalidEntityIdException();
    }
  }

  private <T extends B> Optional<T> singleOrEmpty(List<T> list) {
    return list.stream().distinct().map(Optional::ofNullable).reduce(Optional.empty(),
        (a, b) -> a.isPresent() ^ b.isPresent() ? b : Optional.empty());
  }

  private <T extends B> Boolean tryWhere(T entity, Predicate<T> where) {
    try {
      return where.test(entity);
    } catch (Exception e) {
      return false;
    }
  }

}
