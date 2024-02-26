package SamuelBot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
        createNewFile();
    }

    public List<Task> loadTasksFromFile() {
        List<Task> tasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String type = parts[0].trim();
                    String status = parts[1].trim(); // Updated variable name
                    String description = parts[2].trim();
                    switch (type) {
                        case "T":
                            tasks.add(new Todo(description)); // SamuelBot.Todo does not take status as parameter
                            break;
                        case "D":
                            if (parts.length >= 4) {
                                String by = parts[3].trim();
                                tasks.add(new Deadline(description, by)); // Updated constructor
                            } else {
                                System.out.println("Incomplete input for deadline task.");
                            }
                            break;
                        case "E":
                            if (parts.length >= 5) {
                                String from = parts[3].trim();
                                String to = parts[4].trim();
                                tasks.add(new Event(description, from, to)); // Event does not take status as parameter
                            } else {
                                System.out.println("Incomplete input for event task.");
                            }
                            break;
                        default:
                            System.out.println("Unknown task type: " + type);
                            break;
                    }
                } else {
                    System.out.println("Incomplete input: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
        return tasks;
    }

    public void saveTasksToFile(List<Task> taskList) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Task task : taskList) {
                writer.write(task.toFileString() + "\n"); // Write task details to the file
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void createNewFile() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating new file: " + e.getMessage());
        }
    }
}
