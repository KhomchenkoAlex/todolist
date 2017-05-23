package edu.test.controller;

import edu.test.dto.TaskDto;
import edu.test.model.Task;
import edu.test.model.User;
import edu.test.repository.TaskRepository;
import edu.test.service.TaskService;
import edu.test.service.UserService;
import edu.test.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
public class TodoController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;


    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/todolist")
    public String getTasks(Model model) {
        User user = userService.getCurrentUser();
        List<Task> result = taskRepository.findByUserId(user.getId());
        model.addAttribute("inProgress", taskService.getTaskAmountByStatus(user.getId(), Status.IN_PROGRESS));
        model.addAttribute("done", taskService.getTaskAmountByStatus(user.getId(), Status.COMPLETE));
        model.addAttribute("user", user.getUsername());
        model.addAttribute("tasks", result);
        model.addAttribute("taskDto", new TaskDto());
        model.addAttribute("total", result.size());
        return "todolist";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute("taskDto") TaskDto taskDto) {
        taskService.addNewTask(taskDto);
        return "redirect:/todolist";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") Long taskId) {
        taskRepository.delete(taskId);
        return "redirect:/todolist";
    }

    @GetMapping("/setTaskStatus/{id}")
    public String setTaskStatus(@PathVariable("id") Long taskId, @RequestParam String status) {
        taskService.changeTaskStatus(taskId, status);
        return "redirect:/todolist";
    }
}

