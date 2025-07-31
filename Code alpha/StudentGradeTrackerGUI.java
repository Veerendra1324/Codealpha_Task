import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Student {
    String name;
    ArrayList<Integer> grades;

    Student(String name) {
        this.name = name;
        grades = new ArrayList<>();
    }

    void addGrade(int grade) {
        grades.add(grade);
    }

    double getAverage() {
        return grades.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    int getHighest() {
        return grades.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    int getLowest() {
        return grades.stream().mapToInt(Integer::intValue).min().orElse(0);
    }
}

public class StudentGradeTrackerGUI extends JFrame {
    private JTextField nameField, gradeField;
    private JTextArea gradeListArea;
    private DefaultTableModel tableModel;
    private ArrayList<Student> students;
    private Student currentStudent;

    public StudentGradeTrackerGUI() {
        students = new ArrayList<>();

        setTitle("Student Grade Tracker");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel - Inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        nameField = new JTextField();
        gradeField = new JTextField();
        gradeListArea = new JTextArea(3, 20);
        gradeListArea.setEditable(false);

        JButton addStudentButton = new JButton("Start New Student");
        JButton addGradeButton = new JButton("Add Grade");
        JButton saveStudentButton = new JButton("Save Student");

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(addGradeButton);
        inputPanel.add(addStudentButton);
        inputPanel.add(new JLabel("Grades:"));
        inputPanel.add(new JScrollPane(gradeListArea));
        inputPanel.add(saveStudentButton);

        // Table to display summary
        String[] columns = {"Name", "Grades", "Average", "Highest", "Lowest"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Event Handlers
        addStudentButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                currentStudent = new Student(name);
                gradeListArea.setText("");
                gradeField.setText("");
                JOptionPane.showMessageDialog(this, "Student created. Now add grades.");
            }
        });

        addGradeButton.addActionListener(e -> {
            if (currentStudent != null) {
                try {
                    int grade = Integer.parseInt(gradeField.getText().trim());
                    currentStudent.addGrade(grade);
                    gradeListArea.append(grade + " ");
                    gradeField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid grade.");
                }
            }
        });

        saveStudentButton.addActionListener(e -> {
            if (currentStudent != null) {
                students.add(currentStudent);
                tableModel.addRow(new Object[]{
                    currentStudent.name,
                    currentStudent.grades.toString(),
                    String.format("%.2f", currentStudent.getAverage()),
                    currentStudent.getHighest(),
                    currentStudent.getLowest()
                });
                currentStudent = null;
                nameField.setText("");
                gradeListArea.setText("");
                JOptionPane.showMessageDialog(this, "Student saved.");
            }
        });

        // Add to frame
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeTrackerGUI::new);
    }
}
