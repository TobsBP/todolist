package br.com.tobias.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel create(TaskModel taskModel, UUID userID) {
        taskModel.setUserID(userID);
        taskModel.validate();
        return taskRepository.save(taskModel);
    }

    public List<TaskModel> findByUser(UUID userID) {
        return taskRepository.findByUserID(userID);
    }

    public TaskModel update(UUID id, TaskModel taskModel, UUID userID) {
        taskModel.setId(id);
        taskModel.setUserID(userID);
        if (taskModel.isCompleted() && taskModel.getFinishedAt() == null) {
            taskModel.setFinishedAt(LocalDateTime.now());
        }
        return taskRepository.save(taskModel);
    }
}
