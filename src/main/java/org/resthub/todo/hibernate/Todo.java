package org.resthub.todo.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Hibernate POJO
 * @author respergu
 *
 */
@Entity
@Table(name="TODO")
public class Todo {
	    
	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Integer id;
	    @Column
	    private String content;
	    @Column(name="TODO_ORDER")
	    private Integer order;
	    @Column
	    private Boolean done;
	    
	    public Todo() {
	    } 
	    
	    public Todo(Integer id) {
	    	this.id = id;
	    } 

	    public Todo(String content) {
	        this.content = content;
	        done = false;
	        order = 0;
	    }    

	    public String getContent() {
	        return content;
	    }

	    public void setContent(String content) {
	        this.content = content;
	    }

	    public Boolean getDone() {
	        return done;
	    }

	    public void setDone(Boolean done) {
	        this.done = done;
	    }

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Integer getOrder() {
	        return order;
	    }

	    public void setOrder(Integer order) {
	        this.order = order;
	    }

	    public String toString() {
	        return "Todo{" + "id=" + id + ", content=" + content + ", order=" + order + ", done=" + done + '}';
	    }
}
