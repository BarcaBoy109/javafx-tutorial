import java.util.ArrayList;
import java.util.List;

public class Duke {
    public static void main(String[] args) {
        System.out.println("Hello!");
    }

    private String commandType;
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        commandType = null;
        if (input == null || input.trim().isEmpty()) {
            return "Please tell me what you want to do.";
        }

        String command = input.trim();
        String[] parts = command.split("\\s+", 2);
        String action = parts[0].toLowerCase();

        switch (action) {
        case "todo":
            if (parts.length == 1 || parts[1].trim().isEmpty()) {
                return "The description of a todo cannot be empty.";
            }
            Task task = new Task(parts[1].trim());
            tasks.add(task);
            commandType = "AddCommand";
            return "Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size() + " task(s) in the list.";
        case "list":
            return listTasks();
        case "mark":
            return updateMark(parts, true);
        case "unmark":
            return updateMark(parts, false);
        case "delete":
            return delete(parts);
        default:
            return "I can only handle todo, list, mark, unmark, and delete commands.";
        }
    }

    private String listTasks() {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }
        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return response.toString().trim();
    }

    private String updateMark(String[] parts, boolean done) {
        Task task = taskAt(parts);
        if (task == null) {
            return "Please provide a valid task number.";
        }
        task.done = done;
        commandType = "ChangeMarkCommand";
        return done ? "Nice! I've marked this task as done:\n  " + task
                : "Okay, I've marked this task as not done:\n  " + task;
    }

    private String delete(String[] parts) {
        Task task = taskAt(parts);
        if (task == null) {
            return "Please provide a valid task number.";
        }
        tasks.remove(task);
        commandType = "DeleteCommand";
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + tasks.size() + " task(s) in the list.";
    }

    private Task taskAt(String[] parts) {
        if (parts.length < 2) {
            return null;
        }
        try {
            int index = Integer.parseInt(parts[1].trim()) - 1;
            return index >= 0 && index < tasks.size() ? tasks.get(index) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static class Task {
        private final String description;
        private boolean done;

        Task(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "[" + (done ? "X" : " ") + "] " + description;
        }
    }
    public String getCommandType() {
        return commandType;
    }
}
