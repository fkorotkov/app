package com.github.steroidteam.todolist.model.todo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    @Test
    public void taskCorrectlyThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Task nullTask = new Task(null);
                });
    }

    @Test
    public void taskIsCorrectlyCreated() {
        String body = "Body!";
        Integer id = 87879;
        Task dumbTask = new Task(body);

        assertEquals(body, dumbTask.getBody());

        Task dumbTask2 = new Task("This is a different body !");

        assertTrue(dumbTask.equals(dumbTask));
        assertFalse(dumbTask.equals(null));
        assertFalse(dumbTask.equals("Different Class"));
        assertFalse(dumbTask.equals(dumbTask2));
    }

    @Test
    public void getWorks() {
        String newBody = "New Body!";
        Task dumbTask = new Task("body");
        dumbTask.setBody(newBody);

        assertEquals(newBody, dumbTask.getBody());
    }

    @Test
    public void serializationOfaTaskIsCorrect() {
        String body = "Body!";
        Integer id = 97873;
        Task dumbTask = new Task("Body!");

        assertEquals("Task{body='Body!'}", dumbTask.toString());
    }

    @Test
    public void equalsWorks() {
        assertEquals(new Task("Do something"), new Task("Do something"));
        assertNotEquals(new Task("Do something"), new Task("Do something else"));
        assertNotEquals(null, new Task(""));
    }
}
