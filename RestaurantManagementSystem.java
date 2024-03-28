import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
public class RestaurantManagementSystem extends JFrame implements ActionListener {
    private JTextField itemNameField, descriptionField, priceField, categoryField, availabilityField;
    private JButton addButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public RestaurantManagementSystem() {
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 400)); // Larger window size

        // Create a panel with padding for input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBackground(new Color(173, 255, 147)); // Light Green Background
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adding padding
        add(inputPanel, BorderLayout.WEST);

        inputPanel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField();
        inputPanel.add(itemNameField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Availability (true or false):"));
        availabilityField = new JTextField();
        inputPanel.add(availabilityField);

        // Button to add menu item
        addButton = new JButton("Add Item");
        addButton.addActionListener(this);
        add(addButton, BorderLayout.SOUTH);

        // Table to display menu items on the right side
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Table Model for the JTable
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("Price");
        tableModel.addColumn("Category");
        tableModel.addColumn("Availability");

        table.setModel(tableModel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        fetchMenuItems();
    }

    public void actionPerformed(ActionEvent e) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("driver recieved");
        } catch (Exception a) {
            a.printStackTrace();
        }
        if (e.getSource() == addButton) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBMS_Project", "root", "Mayur@03");

                String itemName = itemNameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                boolean availability = Boolean.parseBoolean(availabilityField.getText());

                String insertItemSQL = "INSERT INTO menu_items (item_name, description, price, category, availability) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertItemStatement = connection.prepareStatement(insertItemSQL);
                insertItemStatement.setString(1, itemName);
                insertItemStatement.setString(2, description);
                insertItemStatement.setDouble(3, price);
                insertItemStatement.setString(4, category);
                insertItemStatement.setBoolean(5, availability);
                insertItemStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Menu Item added successfully.");

                insertItemStatement.close();
                connection.close();

                fetchMenuItems(); // Refresh the displayed items
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding item. Please check the inputs.");
            }
        }
    }

    private void fetchMenuItems() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBMS_Project", "root", "Mayur@03");
            String selectItemsSQL = "SELECT item_name, description, price, category, availability FROM menu_items";
            PreparedStatement selectItemsStatement = connection.prepareStatement(selectItemsSQL);
            ResultSet resultSet = selectItemsStatement.executeQuery();

            // Clear existing data in the table
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getString("item_name"),
                    resultSet.getString("description"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category"),
                    resultSet.getBoolean("availability")
                };
                tableModel.addRow(rowData);
            }

            resultSet.close();
            selectItemsStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching menu items.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantManagementSystem());
    }
}