package br.com.tobias.todolist.analytics;

import java.time.LocalDateTime;
import java.util.List;

public record AnalyticsReport(
        long total,
        long completed,
        long remaining,
        double completionRate,
        LocalDateTime lastFinishedAt,
        List<FinishedTask> finishedTasks) {

    public record FinishedTask(String title, LocalDateTime finishedAt) {}
}
