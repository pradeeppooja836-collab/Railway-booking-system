 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ================= MAIN CLASS =================
public class RailwayBookingSystem extends JFrame implements ActionListener {

    JTextField nameField, ageField, sourceField, destField;
    JComboBox<String> genderBox, trainBox;
    JButton bookBtn, viewBtn, cancelBtn;

    // Seat Map
    JButton[] seats = new JButton[24];
    Integer selectedSeat = null;

    // ============== CONSTRUCTOR ==============
    public RailwayBookingSystem() {

        setTitle("Railway Booking System");
        setSize(900, 500);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        Font f = new Font("Arial", Font.BOLD, 14);

        addLabel("Name", 30, 30, f);
        nameField = addTextField(150, 30);

        addLabel("Age", 30, 70, f);
        ageField = addTextField(150, 70);

        addLabel("Gender", 30, 110, f);
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderBox.setBounds(150, 110, 150, 25);
        add(genderBox);

        addLabel("Train", 30, 150, f);
        trainBox = new JComboBox<>(new String[]{
                "Rajdhani Express",
                "Shatabdi Express",
                "Duronto Express"
        });
        trainBox.setBounds(150, 150, 150, 25);
        add(trainBox);

        addLabel("Source", 30, 190, f);
        sourceField = addTextField(150, 190);

        addLabel("Destination", 30, 230, f);
        destField = addTextField(150, 230);

        // Buttons
        bookBtn = createButton("Book Ticket", 30, 280);
        viewBtn = createButton("View Bookings", 160, 280);
        cancelBtn = createButton("Cancel Ticket", 310, 280);

        add(bookBtn);
        add(viewBtn);
        add(cancelBtn);

        bookBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        // Seat Map Panel
        JPanel seatPanel = new JPanel(new GridLayout(4, 6, 10, 10));
        seatPanel.setBounds(450, 30, 400, 300);
        seatPanel.setBackground(Color.BLACK);

        for (int i = 0; i < 24; i++) {
            int seatNo = i + 1;
            seats[i] = new JButton(String.valueOf(seatNo));
            seats[i].setBackground(Color.GREEN);
            seats[i].setFont(f);

            seats[i].addActionListener(e -> selectSeat(seatNo));

            seatPanel.add(seats[i]);
        }

        add(seatPanel);

        createTable();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ============== GUI HELPERS ==============
    private void addLabel(String text, int x, int y, Font f) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 25);
        l.setForeground(Color.WHITE);
        l.setFont(f);
        add(l);
    }

    private JTextField addTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 150, 25);
        add(tf);
        return tf;
    }

    private JButton createButton(String text, int x, int y) {
        JButton b = new JButton(text);
        b.setBounds(x, y, 130, 30);
        b.setBackground(Color.RED);
        b.setForeground(Color.WHITE);
        return b;
    }

    // ============== SEAT SELECTION ==============
    private void selectSeat(int seatNo) {

        if (seats[seatNo - 1].getBackground() == Color.RED) {
            JOptionPane.showMessageDialog(this,
                    "Seat already booked");
            return;
        }

        // Reset previous selection
        if (selectedSeat != null) {
            seats[selectedSeat - 1].setBackground(Color.GREEN);
        }

        selectedSeat = seatNo;
        seats[seatNo - 1].setBackground(Color.YELLOW);
    }

    // ============== BUTTON ACTIONS ==============
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == bookBtn) {
            bookTicket();
        }

        if (e.getSource() == viewBtn) {
            new ViewBookings();
        }

        if (e.getSource() == cancelBtn) {
            cancelTicket();
        }
    }

    // ============== BOOK TICKET ==============
    private void bookTicket() {

        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a seat");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String q = "INSERT INTO bookings(name,age,gender,train,source,destination,seat) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(q);

            ps.setString(1, nameField.getText());
            ps.setInt(2, Integer.parseInt(ageField.getText()));
            ps.setString(3, genderBox.getSelectedItem().toString());
            ps.setString(4, trainBox.getSelectedItem().toString());
            ps.setString(5, sourceField.getText());
            ps.setString(6, destField.getText());
            ps.setInt(7, selectedSeat);

            ps.executeUpdate();

            seats[selectedSeat - 1].setBackground(Color.RED);

            JOptionPane.showMessageDialog(this,
                    "Ticket Booked — Seat " + selectedSeat);

            selectedSeat = null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ============== CANCEL TICKET ==============
    private void cancelTicket() {

        String seatStr = JOptionPane.showInputDialog(
                this,
                "Enter Seat No to Cancel"
        );

        if (seatStr == null) return;

        try {
            int seatNo = Integer.parseInt(seatStr);

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM bookings WHERE seat=?"
            );

            ps.setInt(1, seatNo);
            ps.executeUpdate();

            seats[seatNo - 1].setBackground(Color.GREEN);

            JOptionPane.showMessageDialog(this,
                    "Ticket Cancelled for Seat " + seatNo);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ============== CREATE TABLE ==============
    private void createTable() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS bookings (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT, age INTEGER, gender TEXT," +
                            "train TEXT, source TEXT, destination TEXT," +
                            "seat INTEGER)"
            );

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ============== MAIN ==============
    public static void main(String[] args) {
        new RailwayBookingSystem();
    }
}

// ================= DB CONNECTION =================
// ================= DB CONNECTION =================


 class DBConnection {

private static final String DB_URL = "jdbc:sqlite:railway.db"; // file will be created in current folder if not exists
    // Better: make it private constructor so nobody can instantiate this utility class
    private DBConnection() {
        // prevent instantiation
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected successfully → " + DB_URL);
            // Optional: you can check if connection is really alive
            if (conn.isValid(2)) {
                System.out.println("Connection is valid.");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
           e.printStackTrace();   // very important during debugging!
        }
        return conn;  // may return null if failed — caller should check!
    }

    // Optional helper: get connection or throw exception (cleaner for many projects)
    public static Connection getConnectionOrThrow() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("Database connected successfully.");
        return conn;
    }
}
// ================= VIEW BOOKINGS =================
class ViewBookings extends JFrame {

    JTextArea area;

    ViewBookings() {

        setTitle("All Bookings");
        setSize(500, 400);

        area = new JTextArea();
        add(new JScrollPane(area));

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bookings");

            while (rs.next()) {

                area.append(
                        rs.getInt("id") + " | " +
                                rs.getString("name") +
                                " | Seat: " + rs.getInt("seat") + "\n"
                );
            }

        } catch (Exception e) {
            area.setText(e.getMessage());
        }

        setVisible(true);
    }
}
