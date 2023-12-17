package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private JComboBox<String> searchComboBox;
    private JList<String> list1;
    private JList<String> list2;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private DefaultListModel<String> list1Model;
    private DefaultListModel<String> list2Model;
    private JTextField searchField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Text Search App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main app = new Main();
        JPanel mainPanel = app.createMainPanel();
        frame.getContentPane().add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel listsPanel = new JPanel(new GridLayout(1, 2));
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel searchLabel = new JLabel("Поиск:");
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        searchComboBox = new JComboBox<>();
        searchComboBox.addItem("Список 1");
        searchComboBox.addItem("Список 2");
        searchComboBox.setSelectedIndex(0);

        checkBox1 = new JCheckBox("Искать в Списке 1");
        checkBox2 = new JCheckBox("Искать в Списке 2");

        JLabel list1Label = new JLabel("Список 1:");
        list1Model = new DefaultListModel<>();
        list1 = new JList<>(list1Model);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.setVisibleRowCount(5);
        JScrollPane list1ScrollPane = new JScrollPane(list1);

        JLabel list2Label = new JLabel("Список 2:");
        list2Model = new DefaultListModel<>();
        list2 = new JList<>(list2Model);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setVisibleRowCount(5);
        JScrollPane list2ScrollPane = new JScrollPane(list2);



        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewItem();
            }
        });

        JButton editButton = new JButton("Редактировать");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedItem();
            }
        });

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchComboBox);

        listsPanel.add(list1ScrollPane);
        listsPanel.add(list2ScrollPane);



        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkBox1);
        buttonPanel.add(checkBox2);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(listsPanel, BorderLayout.CENTER);
        mainPanel.add(checkBoxPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addNewItem() {
        String newItem = JOptionPane.showInputDialog(null, "Введите новый элемент:", "Добавление элемента", JOptionPane.PLAIN_MESSAGE);
        if (newItem != null && !newItem.isEmpty()) {
            if (searchComboBox.getSelectedItem().equals("Список 1")) {
                list1Model.addElement(newItem);
            } else {
                list2Model.addElement(newItem);
            }
        }
    }

    private void editSelectedItem() {
        if (searchComboBox.getSelectedItem().equals("Список 1")) {
            int selectedIndex = list1.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedItem = list1Model.getElementAt(selectedIndex);
                String editedItem = JOptionPane.showInputDialog(null, "Редактировать элемент:", selectedItem);
                if (editedItem != null && !editedItem.isEmpty()) {
                    list1Model.setElementAt(editedItem, selectedIndex);
                }
            }
        } else {
            int selectedIndex = list2.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedItem = list2Model.getElementAt(selectedIndex);
                String editedItem = JOptionPane.showInputDialog(null, "Редактировать элемент:", selectedItem);
                if (editedItem != null && !editedItem.isEmpty()) {
                    list2Model.setElementAt(editedItem, selectedIndex);
                }
            }
        }
    }

    private void deleteSelectedItem() {
        if (searchComboBox.getSelectedItem().equals("Список 1")) {
            int selectedIndex = list1.getSelectedIndex();
            if (selectedIndex >= 0) {
                list1Model.remove(selectedIndex);
            }
        } else {
            int selectedIndex = list2.getSelectedIndex();
            if (selectedIndex >= 0) {
                list2Model.remove(selectedIndex);
            }
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        boolean searchInList1 = checkBox1.isSelected();
        boolean searchInList2 = checkBox2.isSelected();

        if (searchInList1) {
            list1.clearSelection();
            List<Integer> matchingIndices = getMatchingIndices(list1Model, searchText);
            for (int index : matchingIndices) {
                list1.addSelectionInterval(index, index);
            }
        } else {
            list1.clearSelection();
        }

        if (searchInList2) {
            list2.clearSelection();
            List<Integer> matchingIndices = getMatchingIndices(list2Model, searchText);
            for (int index : matchingIndices) {
                list2.addSelectionInterval(index, index);
            }
        } else {
            list2.clearSelection();
        }
    }

    private List<Integer> getMatchingIndices(DefaultListModel<String> model, String searchText) {
        List<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            String item = model.getElementAt(i).toLowerCase();
            if (item.contains(searchText)) {
                matchingIndices.add(i);
            }
        }
        return matchingIndices;
    }

    private int[] convertIndices(List<Integer> indices) {
        int[] result = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            result[i] = indices.get(i);
        }
        return result;
    }
}