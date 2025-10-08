import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class CrudForm extends JFrame implements ActionListener {
    JTextField txtId, txtName, txtEmail, txtMobile;
    JButton btnAdd, btnUpdate, btnDelete, btnView;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    CrudForm() {
        setTitle("CRUD Operation Form");
        setSize(400, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(50, 30, 100, 25);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(150, 30, 150, 25);
        add(txtId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 70, 100, 25);
        add(lblName);
        txtName = new JTextField();
        txtName.setBounds(150, 70, 150, 25);
        add(txtName);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 110, 100, 25);
        add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(150, 110, 150, 25);
        add(txtEmail);

        JLabel lblMobile = new JLabel("Mobile:");
        lblMobile.setBounds(50, 150, 100, 25);
        add(lblMobile);
        txtMobile = new JTextField();
        txtMobile.setBounds(150, 150, 150, 25);
        add(txtMobile);

        btnAdd = new JButton("Add");
        btnAdd.setBounds(50, 200, 80, 30);
        btnAdd.addActionListener(this);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(140, 200, 80, 30);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(230, 200, 80, 30);
        btnDelete.addActionListener(this);
        add(btnDelete);

        btnView = new JButton("View");
        btnView.setBounds(140, 240, 80, 30);
        btnView.addActionListener(this);
        add(btnView);

        connect();
        setVisible(true);
    }

    // MySQL Database Connection
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/crud_db", "root", "Admin123");
            System.out.println("✅ Database Connected Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button Actions
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addRecord();
        } else if (e.getSource() == btnView) {
            viewRecord();
        } else if (e.getSource() == btnUpdate) {
            updateRecord();
        } else if (e.getSource() == btnDelete) {
            deleteRecord();
        }
    }

    // Add Record
    void addRecord() {
        try {
            pst = con.prepareStatement(
                "INSERT INTO users(name, email, mobile) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );
            pst.setString(1, txtName.getText());
            pst.setString(2, txtEmail.getText());
            pst.setString(3, txtMobile.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    txtId.setText(String.valueOf(id)); // Show the new ID
                }
                JOptionPane.showMessageDialog(this,
                    "Record Added Successfully!\nGenerated ID: " + txtId.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Insert Failed!");
            }
            clearFields();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // View Record
    void viewRecord() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to view!");
                return;
            }

            pst = con.prepareStatement("SELECT * FROM users WHERE id=?");
            pst.setString(1, txtId.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtEmail.setText(rs.getString("email"));
                txtMobile.setText(rs.getString("mobile"));
                JOptionPane.showMessageDialog(this, "Record Found!");
            } else {
                JOptionPane.showMessageDialog(this, "No Record Found for ID " + txtId.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Update Record
    void updateRecord() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to update!");
                return;
            }

            pst = con.prepareStatement("UPDATE users SET name=?, email=?, mobile=? WHERE id=?");
            pst.setString(1, txtName.getText());
            pst.setString(2, txtEmail.getText());
            pst.setString(3, txtMobile.getText());
            pst.setString(4, txtId.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed! Check ID.");
            }
            clearFields();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Delete Record
    void deleteRecord() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to delete!");
                return;
            }

            pst = con.prepareStatement("DELETE FROM users WHERE id=?");
            pst.setString(1, txtId.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Delete Failed! Check ID.");
            }
            clearFields();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtMobile.setText("");
    }

    public static void main(String[] args) {
        new CrudForm();
    }
}
