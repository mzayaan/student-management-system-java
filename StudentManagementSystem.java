import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentManagementSystem extends JFrame implements ActionListener {

    // Linked List for storing students
    StudentLinkedList studentList = new StudentLinkedList();

    // GUI Components
    JTextField studentNumberField, surnameField, otherNamesField, searchField;
    JTextArea displayArea;
    JButton addButton, updateButton, deleteButton, searchButton, displayButton;
    JButton sortAscButton, sortDescButton, sortAlphaButton;

    // Constructor to set up the GUI
    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(700, 600);
        setLayout(new FlowLayout());

        // Input Fields
        studentNumberField = new JTextField(10);
        surnameField = new JTextField(10);
        otherNamesField = new JTextField(10);
        searchField = new JTextField(10);

        // Buttons
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        searchButton = new JButton("Search Student");
        displayButton = new JButton("Display All");
        
        sortAscButton = new JButton("Sort Ascending");
        sortDescButton = new JButton("Sort Descending");
        sortAlphaButton = new JButton("Sort Alphabetically");

        // Text Area
        displayArea = new JTextArea(20, 55);
        displayArea.setEditable(false);

        // Add components to the frame
        add(new JLabel("Student Number:"));
        add(studentNumberField);
        add(new JLabel("Surname:"));
        add(surnameField);
        add(new JLabel("Other Names:"));
        add(otherNamesField);
        add(new JLabel("Search Query:"));
        add(searchField);

        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(searchButton);
        add(displayButton);

        add(sortAscButton);
        add(sortDescButton);
        add(sortAlphaButton);

        add(new JScrollPane(displayArea));

        // Add action listeners
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        searchButton.addActionListener(this);
        displayButton.addActionListener(this);

        sortAscButton.addActionListener(this);
        sortDescButton.addActionListener(this);
        sortAlphaButton.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String number = studentNumberField.getText();
            String surname = surnameField.getText();
            String otherNames = otherNamesField.getText();
            studentList.addStudent(number, surname, otherNames);
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == updateButton) {
            String number = studentNumberField.getText();
            String surname = surnameField.getText();
            String otherNames = otherNamesField.getText();
            studentList.updateStudent(number, surname, otherNames);
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == deleteButton) {
            String number = studentNumberField.getText();
            studentList.deleteStudent(number);
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == searchButton) {
            String query = searchField.getText();
            displayArea.setText(studentList.getSearchResultsAsString(query));
        }

        if (e.getSource() == displayButton) {
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == sortAscButton) {
            studentList.sortAscending();
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == sortDescButton) {
            studentList.sortDescending();
            displayArea.setText(studentList.getStudentListAsString());
        }

        if (e.getSource() == sortAlphaButton) {
            studentList.sortAlphabetically();
            displayArea.setText(studentList.getStudentListAsString());
        }
    }

    public static void main(String[] args) {
        new StudentManagementSystem();
    }
}


// ========== LINKED LIST ========

class StudentLinkedList {

    class Node {
        String studentNumber;
        String surname;
        String otherNames;
        Node next;

        Node(String studentNumber, String surname, String otherNames) {
            this.studentNumber = studentNumber;
            this.surname = surname;
            this.otherNames = otherNames;
            this.next = null;
        }
    }

    private Node head;

    // Add Student
    public void addStudent(String studentNumber, String surname, String otherNames) {
        Node newNode = new Node(studentNumber, surname, otherNames);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // Update Student
    public void updateStudent(String studentNumber, String newSurname, String newOtherNames) {
        Node current = head;
        while (current != null) {
            if (current.studentNumber.equals(studentNumber)) {
                current.surname = newSurname;
                current.otherNames = newOtherNames;
                return;
            }
            current = current.next;
        }
    }

    // Delete Student
    public void deleteStudent(String studentNumber) {
        if (head == null) return;

        if (head.studentNumber.equals(studentNumber)) {
            head = head.next;
            return;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.studentNumber.equals(studentNumber)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    // Search Results with Partial Matching
    public String getSearchResultsAsString(String query) {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            if (current.studentNumber.startsWith(query) || 
                current.surname.toLowerCase().startsWith(query.toLowerCase()) || 
                current.otherNames.toLowerCase().startsWith(query.toLowerCase())) {
                sb.append(current.studentNumber + " | " + current.surname + " | " + current.otherNames + "\n");
            }
            current = current.next;
        }
        if (sb.length() == 0) {
            return "No results found.\n";
        }
        return sb.toString();
    }

    // Display All Students
    public String getStudentListAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number | Surname | Other Names\n");
        sb.append("------------------------------\n");
        Node current = head;
        while (current != null) {
            sb.append(current.studentNumber + " | " + current.surname + " | " + current.otherNames + "\n");
            current = current.next;
        }
        return sb.toString();
    }

    
    // SORTING METHODS 
    

    //  Sort by Student Number in Ascending Order
    public void sortAscending() {
        for (Node i = head; i != null && i.next != null; i = i.next) {
            for (Node j = i.next; j != null; j = j.next) {
                if (i.studentNumber.compareTo(j.studentNumber) > 0) {
                    swap(i, j);
                }
            }
        }
    }

    //  Sort by Student Number in Descending Order
    public void sortDescending() {
        for (Node i = head; i != null && i.next != null; i = i.next) {
            for (Node j = i.next; j != null; j = j.next) {
                if (i.studentNumber.compareTo(j.studentNumber) < 0) {
                    swap(i, j);
                }
            }
        }
    }

    //  Sort by Surname in Alphabetical Order
    public void sortAlphabetically() {
        for (Node i = head; i != null && i.next != null; i = i.next) {
            for (Node j = i.next; j != null; j = j.next) {
                if (i.surname.compareTo(j.surname) > 0) {
                    swap(i, j);
                }
            }
        }
    }

    // Helper method to swap two nodes
    private void swap(Node i, Node j) {
        String tempNum = i.studentNumber;
        String tempSurname = i.surname;
        String tempOtherNames = i.otherNames;

        i.studentNumber = j.studentNumber;
        i.surname = j.surname;
        i.otherNames = j.otherNames;

        j.studentNumber = tempNum;
        j.surname = tempSurname;
        j.otherNames = tempOtherNames;
    }
}
