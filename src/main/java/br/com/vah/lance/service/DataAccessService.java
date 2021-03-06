package br.com.vah.lance.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.usrdbvah.Servico;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import br.com.vah.lance.util.PaginatedSearchParam;

/**
 * Implementation of the generic Data Access Servico All CRUD (create, read,
 * update, delete) basic data access operations for any persistent object are
 * performed in this class.
 *
 * @author Emre Simtay <emre@simtay.com>
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DataAccessService<T extends BaseEntity> implements Serializable {

  public static final String ID = "id";

  @PersistenceContext
  private EntityManager em;

  public DataAccessService() {
  }

  private Class<T> type;

  /**
   * Default constructor
   *
   * @param type entity class
   */
  public DataAccessService(Class<T> type) {
    this.type = type;
  }

  /**
   * Creates a new record in the database
   *
   * @param t
   * @return
   */
  public T create(T t) {
    this.em.persist(t);
    this.em.flush();
    this.em.refresh(t);
    return t;
  }

  /**
   * Retrieves an entity instance that was previously persisted to the
   * database
   *
   * @param id
   * @return
   */
  public T find(Object id) {
    return this.em.find(this.type, id);
  }

  /**
   * Removes the record that is associated with the entity instance
   *
   * @param id
   * @param id
   */
  public void delete(Object id) {
    Object ref = this.em.getReference(this.type, id);
    this.em.remove(ref);
  }

  /**
   * Removes the number of entries from a table
   *
   * @param items
   * @return
   */
  public boolean deleteItems(T[] items) {
    for (T item : items) {
      em.remove(em.merge(item));
    }
    return true;
  }

  /**
   * Updates the entity instance
   *
   * @param item
   * @return the object that is updated
   */
  public T update(T item) {
    return this.em.merge(item);
  }

  /**
   * Updates a list of entity instance
   *
   * @param items
   * @return
   */
  public List<T> update(List<T> items) {
    List<T> persistedList = new ArrayList<>();
    for (T item : items) {
      persistedList.add(this.em.merge(item));
    }
    return persistedList;
  }

  /**
   * Returns the number of records that meet the criteria
   *
   * @param namedQueryName
   * @return List
   */
  public List findWithNamedQuery(String namedQueryName) {
    return this.em.createNamedQuery(namedQueryName).getResultList();
  }

  /**
   * Returns the number of records that meet the criteria
   *
   * @param namedQueryName
   * @param parameters
   * @return List
   */
  public List findWithNamedQuery(String namedQueryName, Map parameters) {
    return findWithNamedQuery(namedQueryName, parameters, 0);
  }

  /**
   * Returns the number of records with result limit
   *
   * @param queryName
   * @param resultLimit
   * @return List
   */
  public List findWithNamedQuery(String queryName, int resultLimit) {
    return this.em.createNamedQuery(queryName).setMaxResults(resultLimit).getResultList();
  }

  /**
   * Returns the number of records that meet the criteria
   *
   * @param sql
   * @return List
   */
  public List<T> findByNativeQuery(String sql) {
    return this.em.createNativeQuery(sql, type).getResultList();
  }

  /**
   * Returns the number of total records
   *
   * @param namedQueryName
   * @return int
   */
  public int countTotalRecord(String namedQueryName) {
    Query query = em.createNamedQuery(namedQueryName);
    Number result = (Number) query.getSingleResult();
    return result.intValue();
  }

  /**
   * Returns the number of records that meet the criteria with parameter map
   * and result limit
   *
   * @param namedQueryName
   * @param parameters
   * @param resultLimit
   * @return List
   */
  public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit) {
    Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
    Query query = this.em.createNamedQuery(namedQueryName);
    if (resultLimit > 0) {
      query.setMaxResults(resultLimit);
    }
    for (Map.Entry<String, Object> entry : rawParameters) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  /**
   * Returns the number of records that will be used with lazy loading /
   * pagination
   *
   * @param namedQueryName
   * @param start
   * @param end
   * @return List
   */
  public List findWithNamedQuery(String namedQueryName, int start, int end) {
    Query query = this.em.createNamedQuery(namedQueryName);
    query.setMaxResults(end - start);
    query.setFirstResult(start);
    return query.getResultList();
  }

  public Criteria createCriteria() {
    Session session = em.unwrap(Session.class);
    return session.createCriteria(type);
  }

  public List<T> list() {
    Session session = em.unwrap(Session.class);
    Criteria criteria = session.createCriteria(Servico.class);
    return criteria.list();
  }

  /**
   * Create criteria
   *
   * @param params
   * @return
   */
  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = em.unwrap(Session.class);
    Criteria criteria = session.createCriteria(type);
    Disjunction disjunction = Restrictions.disjunction();
    Conjunction conjunction = Restrictions.conjunction();
    for (Map.Entry<String, Object> par : params.getParams().entrySet()) {
      Object value = par.getValue();
      if (value != null) {
        if (String.class.equals(value.getClass())) {
          disjunction.add(Restrictions.ilike(par.getKey(), (String) value, MatchMode.ANYWHERE));
        } else if (Integer.class.equals(value.getClass()) || Long.class.equals(value.getClass())) {
          disjunction.add(Restrictions.eq(par.getKey(), value));
        } else if (Object[].class.equals(value.getClass())) {
          conjunction.add(Restrictions.in(par.getKey(), (Object[]) value));
        }
      }
    }
    criteria.add(disjunction);
    criteria.add(conjunction);
    return criteria;
  }

  /**
   * Paginated search
   *
   * @param params
   * @return
   */
  public List<T> paginatedSearch(PaginatedSearchParam params) {
    Criteria selectCriteria = createCriteria(params);

    selectCriteria.setFirstResult(params.getFirst());
    selectCriteria.setMaxResults(params.getPageSize());

    if (params.getOrderBy() != null) {
      if (params.getAsc()) {
        selectCriteria.addOrder(Order.asc(params.getOrderBy()));
      } else {
        selectCriteria.addOrder(Order.desc(params.getOrderBy()));
      }
    }

    for (String relation : params.getRelations()) {
      selectCriteria.setFetchMode(relation, FetchMode.SELECT);
    }

    return selectCriteria.list();
  }

  /**
   * Paginated count
   *
   * @param params
   * @return
   */
  public int paginatedCount(PaginatedSearchParam params) {
    Criteria criteria = createCriteria(params);
    criteria.setProjection(Projections.rowCount());
    return ((Number) criteria.uniqueResult()).intValue();
  }

  public List<T> getListByNamedQuery(String namedQuery, Map<String, Object> params) {
    Query query = getEm().createNamedQuery(namedQuery);
    for (String key : params.keySet()) {
      query.setParameter(key, params.get(key));
    }
    return query.getResultList();
  }

  public EntityManager getEm() {
    return em;
  }

  public List<T> persistList(List<T> list) {

    List<T> attachedList = new ArrayList<>();

    for (T item : list) {
      if (item.getId() == null) {
        attachedList.add(create(item));
      } else {
        attachedList.add(update(item));
      }
    }

    return attachedList;

  }
}