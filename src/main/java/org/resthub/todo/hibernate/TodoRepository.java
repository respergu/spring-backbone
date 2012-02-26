package org.resthub.todo.hibernate;

import java.util.List;

public interface TodoRepository {

    
    Todo create(Todo todo);
    
    Todo update(Todo todo);
    
    Todo findById(Integer id);

    List<Todo> findAll();
    
    void remove(Integer id);
    
    public void removeAll();
    
}
