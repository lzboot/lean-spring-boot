package learning.springboot.jpa.dao;

public interface Dao {

	void flush();

	<T> T get(Class<T> entityType, Object id);

	void save(Object entity);

	void update(Object entity);

	void delete(Object entity);

}
