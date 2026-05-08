package br.com.tobias.todolist.analytics;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.tobias.todolist.task.ITaskRepository;
import br.com.tobias.todolist.task.TaskModel;

@Service
public class AnalyticsService {

    private final ITaskRepository taskRepository;

    public AnalyticsService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public AnalyticsReport reportFor(UUID userID) {
        List<TaskModel> tasks = taskRepository.findByUserID(userID);

        long total = tasks.size();
        long completed = tasks.stream().filter(TaskModel::isCompleted).count();
        long remaining = total - completed;
        double rate = total == 0 ? 0.0 : (double) completed / total;

        List<AnalyticsReport.FinishedTask> finished = tasks.stream()
                .filter(t -> t.isCompleted() && t.getFinishedAt() != null)
                .sorted(Comparator.comparing(TaskModel::getFinishedAt).reversed())
                .map(t -> new AnalyticsReport.FinishedTask(t.getTitle(), t.getFinishedAt()))
                .toList();

        var lastFinishedAt = finished.isEmpty() ? null : finished.get(0).finishedAt();

        return new AnalyticsReport(total, completed, remaining, rate, lastFinishedAt, finished);
    }
}
