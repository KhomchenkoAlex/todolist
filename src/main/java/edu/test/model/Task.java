package edu.test.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.test.util.Priority;
import edu.test.util.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String taskName;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String taskName, User user) {
        this.taskName = taskName;
        this.user = user;
        this.status = Status.NOT_STARTED;
        this.priority = Priority.LOW;
    }

    public Task() {
        this.status = Status.NOT_STARTED;
        this.priority = Priority.LOW;
    }
}
