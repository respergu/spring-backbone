package org.resthub.todo;
/**
 * Todo POJO.
 * @author REspergue
 *
 */
public class Todo {
    
    private String id;
    private String content;
    private Integer order;
    private Boolean done;
    
    public Todo() {
        done = false;
        order = 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
