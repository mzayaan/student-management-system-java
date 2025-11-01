/*
    Author: Mohammad Zayaan Husain Dulmeer
    Date: 21/07/2025
    Purpose: Manage Students
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentManagementSystem1 extends JFrame implements ActionListener {

    StudentLinkedList studentList = new StudentLinkedList();

    JTextField studentNumberField, surnameField, otherNamesField, searchField;
    JTextArea displayArea;
    JButton addButton, updateButton, deleteButton, searchButton, displayButton;
    JButton sortAscButton, sortDescButton, sortAlphaButton, sortAlphaDescButton;

    public StudentManagementSystem1() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout(10, 10));

        // ===== TOP INPUT PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        JPanel inputRow = new JPanel(new GridLayout(1, 6, 5, 5));
        inputRow.add(new JLabel("Student Number:"));
        inputRow.add(studentNumberField = new JTextField());
        inputRow.add(new JLabel("Surname:"));
        inputRow.add(surnameField = new JTextField());
        inputRow.add(new JLabel("Other Names:"));
        inputRow.add(otherNamesField = new JTextField());

        JPanel searchRow = new JPanel(new GridLayout(1, 2, 5, 5));
        searchRow.add(new JLabel("Search Query:"));
        searchRow.add(searchField = new JTextField());

        JPanel stackedTop = new JPanel(new GridLayout(2, 1, 5, 5));
        stackedTop.add(inputRow);
        stackedTop.add(searchRow);

        topPanel.add(stackedTop, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== LEFT PANEL FOR BUTTONS (VERTICAL) =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        searchButton = new JButton("Search Student");
        displayButton = new JButton("Display All");
        sortAscButton = new JButton("Sort Ascending");
        sortDescButton = new JButton("Sort Descending");
        sortAlphaButton = new JButton("Sort A→Z");
        sortAlphaDescButton = new JButton("Sort Z→A");

        JButton[] buttons = {
            addButton, updateButton, deleteButton, searchButton,
            displayButton, sortAscButton, sortDescButton, sortAlphaButton, sortAlphaDescButton
        };

        Dimension buttonSize = new Dimension(160, 30);
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(buttonSize);
            btn.setPreferredSize(buttonSize);
            btn.setMinimumSize(buttonSize);
            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(8));
        }

        add(buttonPanel, BorderLayout.WEST);

        // ===== CENTER PANEL =====
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        for (JButton btn : buttons) {
            btn.addActionListener(this);
        }

        studentList.loadFromFile();
        displayArea.setText(studentList.getStudentListAsString());

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String number = studentNumberField.getText().trim();
        String surname = surnameField.getText().trim();
        String otherNames = otherNamesField.getText().trim();
        String query = searchField.getText().trim();

        if (e.getSource() == addButton) {
            studentList.addStudent(number, surname, otherNames);
        } else if (e.getSource() == updateButton) {
            studentNumberField.setEditable(false);
            studentList.updateStudent(number, surname, otherNames);
        } else if (e.getSource() == deleteButton) {
            studentList.deleteStudent(number);
        } else if (e.getSource() == searchButton) {
            displayArea.setText(studentList.getSearchResultsAsString(query));
            JOptionPane.showMessageDialog(null, "Search completed.");
            return;
        } else if (e.getSource() == displayButton) {
            displayArea.setText(studentList.getStudentListAsString());
            JOptionPane.showMessageDialog(null, "Displaying all students.");
            return;
        } else if (e.getSource() == sortAscButton) {
            studentList.sortAscending();
            JOptionPane.showMessageDialog(null, "Sorted by Student Number (Ascending).");
        } else if (e.getSource() == sortDescButton) {
            studentList.sortDescending();
            JOptionPane.showMessageDialog(null, "Sorted by Student Number (Descending).");
        } else if (e.getSource() == sortAlphaButton) {
            studentList.sortAlphabetically();
            JOptionPane.showMessageDialog(null, "Sorted by Surname (A → Z).");
        } else if (e.getSource() == sortAlphaDescButton) {
            studentList.sortAlphabeticallyDescending();
            JOptionPane.showMessageDialog(null, "Sorted by Surname (Z → A).");
        }

        displayArea.setText(studentList.getStudentListAsString());
        clearFields();
    }

    private void clearFields() {
        studentNumberField.setText("");
        surnameField.setText("");
        otherNamesField.setText("");
        searchField.setText("");
        studentNumberField.setEditable(true);
    }

    public static void main(String[] args) {
        new StudentManagementSystem1();
    }
}

// ========== LINKED LIST CLASS ==========

class StudentLinkedList {

    private static final String FILE_NAME = "students.txt";

    class Node {
        String studentNumber;
        String surname;
        String otherNames;
        Node next;

        Node(String studentNumber, String surname, String otherNames) {
            this.studentNumber = studentNumber;
            this.surname = surname;
            this.otherNames = otherNames;
        }
    }

    private Node head;

    public void addStudent(String studentNumber, String surname, String otherNames) {
        if (!isUnique(studentNumber, surname, otherNames)) return;

        Node newNode = new Node(studentNumber, surname, otherNames);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null)
                current = current.next;
            current.next = newNode;
        }

        JOptionPane.showMessageDialog(null, "Student added successfully.");
        saveToFile();
    }

    private boolean isUnique(String studentNumber, String surname, String otherNames) {
        Node current = head;
        while (current != null) {
            if (current.studentNumber.equalsIgnoreCase(studentNumber)) {
                JOptionPane.showMessageDialog(null, "Error: Student Number \"" + studentNumber + "\" already exists. Please enter a different ID.");
                return false;
            }
            if (current.surname.equalsIgnoreCase(surname) && current.otherNames.equalsIgnoreCase(otherNames)) {
                JOptionPane.showMessageDialog(null, "Error: A student with the same full name (Surname + Other Names) already exists.");
                return false;
            }
            current = current.next;
        }
        return true;
    }

    public void updateStudent(String studentNumber, String newSurname, String newOtherNames) {
        Node current = head;
        while (current != null) {
            if (current.studentNumber.equalsIgnoreCase(studentNumber)) {
                current.surname = newSurname;
                current.otherNames = newOtherNames;
                saveToFile();
                JOptionPane.showMessageDialog(null, "Student updated successfully.");
                return;
            }
            current = current.next;
        }
        JOptionPane.showMessageDialog(null, "Error: Student Number \"" + studentNumber + "\" not found. Update failed.");
    }

    public void deleteStudent(String studentNumber) {
        if (head == null) {
            JOptionPane.showMessageDialog(null, "Error: No student records found.");
            return;
        }

        if (head.studentNumber.equalsIgnoreCase(studentNumber)) {
            head = head.next;
            saveToFile();
            JOptionPane.showMessageDialog(null, "Student deleted successfully.");
            return;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.studentNumber.equalsIgnoreCase(studentNumber)) {
                current.next = current.next.next;
                saveToFile();
                JOptionPane.showMessageDialog(null, "Student deleted successfully.");
                return;
            }
            current = current.next;
        }

        JOptionPane.showMessageDialog(null, "Error: Student Number \"" + studentNumber + "\" not found. Deletion failed.");
    }

    public String getSearchResultsAsString(String query) {
    StringBuilder sb = new StringBuilder();
    Node current = head;
    while (current != null) {
        if (current.studentNumber.toLowerCase().contains(query.toLowerCase())
                || current.surname.toLowerCase().contains(query.toLowerCase())
                || current.otherNames.toLowerCase().contains(query.toLowerCase())) {
            sb.append(current.studentNumber).append(" | ")
              .append(current.surname).append(" | ")
              .append(current.otherNames).append("\n");
        }
        current = current.next;
    }
    return sb.length() == 0 ? "No results found.\n" : sb.toString();
}


    public String getStudentListAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number | Surname | Other Names\n");
        sb.append("------------------------------\n");
        Node current = head;
        while (current != null) {
            sb.append(current.studentNumber).append(" | ").append(current.surname)
                    .append(" | ").append(current.otherNames).append("\n");
            current = current.next;
        }
        return sb.toString();
    }

    public void sortAscending() {
    for (Node i = head; i != null && i.next != null; i = i.next) {
        for (Node j = i.next; j != null; j = j.next) {
            try {
                int numI = Integer.parseInt(i.studentNumber);
                int numJ = Integer.parseInt(j.studentNumber);
                if (numI > numJ) swap(i, j);
            } catch (NumberFormatException e) {
                // Fallback to string comparison if not numeric
                if (i.studentNumber.compareToIgnoreCase(j.studentNumber) > 0) {
                    swap(i, j);
                }
            }
        }
    }
    saveToFile();
}


    public void sortDescending() {
    for (Node i = head; i != null && i.next != null; i = i.next) {
        for (Node j = i.next; j != null; j = j.next) {
            try {
                int numI = Integer.parseInt(i.studentNumber);
                int numJ = Integer.parseInt(j.studentNumber);
                if (numI < numJ) swap(i, j);
            } catch (NumberFormatException e) {
                if (i.studentNumber.compareToIgnoreCase(j.studentNumber) < 0) {
                    swap(i, j);
                }
            }
        }
    }
    saveToFile();
}


    public void sortAlphabetically() {
        for (Node i = head; i != null && i.next != null; i = i.next)
            for (Node j = i.next; j != null; j = j.next)
                if (i.surname.compareToIgnoreCase(j.surname) > 0)
                    swap(i, j);
        saveToFile();
    }

    public void sortAlphabeticallyDescending() {
        for (Node i = head; i != null && i.next != null; i = i.next)
            for (Node j = i.next; j != null; j = j.next)
                if (i.surname.compareToIgnoreCase(j.surname) < 0)
                    swap(i, j);
        saveToFile();
    }

    private void swap(Node i, Node j) {
        String tempNum = i.studentNumber, tempSurname = i.surname, tempOther = i.otherNames;
        i.studentNumber = j.studentNumber;
        i.surname = j.surname;
        i.otherNames = j.otherNames;
        j.studentNumber = tempNum;
        j.surname = tempSurname;
        j.otherNames = tempOther;
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            Node current = head;
            while (current != null) {
                writer.println(current.studentNumber + "," + current.surname + "," + current.otherNames);
                current = current.next;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3 && isUnique(parts[0], parts[1], parts[2])) {
                    Node newNode = new Node(parts[0], parts[1], parts[2]);
                    if (head == null) {
                        head = newNode;
                    } else {
                        Node current = head;
                        while (current.next != null) current = current.next;
                        current.next = newNode;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }
}
