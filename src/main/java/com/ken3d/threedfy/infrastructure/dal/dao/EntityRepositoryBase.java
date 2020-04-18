package com.ken3d.threedfy.infrastructure.dal.dao;

import org.hibernate.SessionFactory;

public abstract class EntityRepositoryBase {

  private final SessionFactory sessionFactory;

  public EntityRepositoryBase(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  //  public <T extends Serializable> T select(final long id) {
  //
  //  }
  //
  //  public <T extends Serializable> T select(T e, Function<Boolean, T> where) {
  //
  //  }
  //
  //  public <T extends Serializable> List<T> selectAll() {
  //
  //  }
  //
  //  public <T extends Serializable> List<T> selectAll(T e, Function<Boolean, T> where) {
  //
  //  }
  //
  //  public <T extends Serializable> void create(final T entity) {
  //
  //  }
  //
  //  public <T extends Serializable> void createMany(final List<T> entities) {
  //
  //  }
  //
  //  public <T extends Serializable> T update(final T entity) {
  //
  //  }
  //
  //  public <T extends Serializable> T updateMany(final List<T> entities) {
  //
  //  }
  //
  //  public <T extends Serializable> void delete(final T entity) {
  //
  //  }
  //
  //  public <T extends Serializable> void delete(final long entityId) {
  //
  //  }

  private void executeTransaction() {

    //    Session session = factory.openSession();
    //    Transaction tx = null;
    //    try {
    //      tx = session.beginTransaction();
    //      List employees = session.createQuery("FROM Employee").list();
    //      for (Iterator iterator = employees.iterator(); iterator.hasNext();){
    //        Employee employee = (Employee) iterator.next();
    //        System.out.print("First Name: " + employee.getFirstName());
    //        System.out.print("  Last Name: " + employee.getLastName());
    //        System.out.println("  Salary: " + employee.getSalary());
    //      }
    //      tx.commit();
    //    } catch (HibernateException e) {
    //      if (tx!=null) tx.rollback();
    //      e.printStackTrace();
    //    } finally {
    //      session.close();
    //    }

  }
}
