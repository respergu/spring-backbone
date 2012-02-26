package org.resthub.todo.hibernate;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.LockModeType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	public Todo create(Todo todo) {
		return hibernateTemplate.merge(todo);
	}

	public Todo update(Todo todo) {
		return hibernateTemplate.merge(todo);
	}

	public Todo findById(Integer id) {
		return hibernateTemplate.get(Todo.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Todo> findAll() {
		return hibernateTemplate.find("from Todo");
	}

	public void remove(Integer id) {
		hibernateTemplate.delete(new Todo(id));
	}

	@SuppressWarnings("unchecked")
	public void removeAll() {
		hibernateTemplate.execute((new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
            	session.update("delete from Todo");
            	return null;
            }
        }));
	}

}
