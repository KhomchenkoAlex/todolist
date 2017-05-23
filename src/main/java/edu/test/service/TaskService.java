package edu.test.service;

import edu.test.dto.TaskDto;
import edu.test.model.Task;
import edu.test.model.User;
import edu.test.repository.TaskRepository;
import edu.test.util.Priority;
import edu.test.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public void addNewTask(TaskDto taskDto) {
        User user = userService.getCurrentUser();
        Task task = new Task();
        task.setTaskName(taskDto.getTaskName());
        task.setDescription(taskDto.getDescription());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setUser(user);
        taskRepository.save(task);
    }

    public void changeTaskStatus(Long taskId, String taskStatus) {
        Task task = taskRepository.findById(taskId);
        task.setStatus(Status.valueOf(taskStatus));
        taskRepository.save(task);
    }

    public long getTaskAmountByStatus(Long userId, Status taskStatus) {
        return taskRepository.findByUserId(userId).stream().filter(task -> task.getStatus() == taskStatus).count();
    }
}
