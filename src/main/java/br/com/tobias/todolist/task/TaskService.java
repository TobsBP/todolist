package br.com.tobias.todolist.task;

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
        return taskRepository.save(taskModel);
    }
}
