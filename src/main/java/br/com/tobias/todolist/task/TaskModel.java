package br.com.tobias.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID userID;

    @Column(length = 30)
    private String title;

    private String priority;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @CreationTimestamp
    private LocalDateTime createAt;
    
    public void validateDates() {
        var now = LocalDateTime.now();

        if (startAt == null || endAt == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (now.isAfter(startAt) || now.isAfter(endAt)) {
            throw new IllegalArgumentException("Dates must be in the future");
        }

        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public void validateTitle() {
        if (title != null && title.length() > 30) {
            throw new IllegalArgumentException("Title must be 30 characters or less");
        }
    }

    public void validate() {
        validateDates();
        validateTitle();
    }
}
