package org.resthub.todo;

import java.util.ArrayList;
import java.util.List;

import org.resthub.todo.hibernate.Todo;
import org.resthub.todo.hibernate.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller @RequestMapping("/api/todo")
public class TodoController {

	@Autowired
    private TodoRepository todoRepository;
    
    static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @RequestMapping(method = RequestMethod.GET, produces = "application/json") @ResponseBody
    public List<Todo> findAll() {
    	List<Todo> ret = new ArrayList<Todo>();
    	List<org.resthub.todo.hibernate.Todo> hibTodos = todoRepository.findAll();
    	for (org.resthub.todo.hibernate.Todo todo : hibTodos) {
    		ret.add(getTodo(todo));
		}
        return ret;
    }

	private Todo getTodo(org.resthub.todo.hibernate.Todo todo) {
		Todo newTodo = new Todo();
		newTodo.setId(todo.getId());
		newTodo.setContent(todo.getContent());
		newTodo.setDone(todo.getDone());
		newTodo.setOrder(todo.getOrder());
		return newTodo;
	}
	
	private org.resthub.todo.hibernate.Todo getHibernateTodo(org.resthub.todo.Todo todo) {
		org.resthub.todo.hibernate.Todo newTodo = new org.resthub.todo.hibernate.Todo();
		newTodo.setId(todo.getId() != null ? Integer.parseInt(todo.getId()) : null);
		newTodo.setContent(todo.getContent());
		newTodo.setDone(todo.getDone());
		newTodo.setOrder(todo.getOrder());
		return newTodo;
	}
    
    @RequestMapping(value = "{id}",method = RequestMethod.GET ) @ResponseBody
    
    public final Todo get( @PathVariable( "id" ) final String id ){
    	org.resthub.todo.hibernate.Todo todo = todoRepository.findById(Integer.parseInt(id));
        return getTodo(todo);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json") @ResponseStatus(HttpStatus.CREATED) @ResponseBody
    public Todo create(@RequestBody org.resthub.todo.Todo todo) {
        Assert.notNull(todo);
		return todoRepository.create(getHibernateTodo(todo));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT) @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody org.resthub.todo.Todo todo, @PathVariable String id) {
        Assert.isTrue(todo.getId().equals(id));
        todoRepository.update(getHibernateTodo(todo));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        todoRepository.remove(Integer.parseInt(id));
    }
}
