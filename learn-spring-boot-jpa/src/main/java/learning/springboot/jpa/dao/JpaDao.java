package learning.springboot.jpa.dao;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaDao implements Dao {

	@Autowired
	private EntityManagerFactory emf;

	private Session currentSession() {
		//
		// 注意：此处不能用 sessionFactory.getCurrentSession();
		// 因为 JpaTransactionManager 把 EntityManagerHolder 放入 thread 调用是：
		//
		// TransactionSynchronizationManager.bindResource(
		// obtainEntityManagerFactory(), txObject.getEntityManagerHolder());
		//
		EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(emf);
		Assert.notNull(emHolder, "Fail to get Hibernate Session from the current thread.");
		Session session = (Session) emHolder.getEntityManager();
		log.debug("Got Hibernate Session [{}] from the current thread.", session);
		return session;
	}

	@Override
	public void flush() {
		this.currentSession().flush();
	}

	@Override
	public <T> T get(Class<T> entityClass, Object id) {
		return this.currentSession().find(entityClass, id);
	}

	@Override
	public void save(Object entity) {
		this.currentSession().persist(entity);
	}

	@Override
	public void update(Object entity) {
		this.currentSession().merge(entity);
	}

	@Override
	public void delete(Object entity) {
		this.currentSession().delete(entity);
	}

}
