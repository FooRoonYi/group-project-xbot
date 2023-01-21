package my.uum;

import java.sql.*;

/**
 * This class is for connecting to SQLite database.
 * hence the data can be inserted, deleted and selected.
 *
 * @author Group X_bot
 */

public class DatabaseConnect {

    /**
     * This method is used to connect with database
     *
     * @return connect
     */
    public static Connection connect() {
        String url = "jdbc:sqlite:Xbot.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    /**
     * This method is used to insert users' data into tbl_user.
     *
     * @param user_name User should key in his or her name
     * @param ic_no User should key in i/c number
     * @param staff_id User should key in staff id
     * @param phone_no User should in phone number
     * @param email User should in email
     */
    public void insertUser(String user_name, String ic_no, String staff_id, String phone_no, String email) {
        String sqlInsertUser = "INSERT INTO tbl_user (ic_no, staff_id, user_name, phone_no, email) " +
                "VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertUser)) {
            preparedStatement.setString(1, ic_no);
            preparedStatement.setString(2, staff_id);
            preparedStatement.setString(3, user_name);
            preparedStatement.setString(4, phone_no);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update users' data into tbl_user
     *
     * @param email User can update email
     * @param phone_no User can update phone number
     * @param staff_id to control update user's details
     */
    public void updateUser(String email, String phone_no,String staff_id) {
        String sql = "UPDATE tbl_user SET email = ? , phone_no= ? WHERE staff_id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            System.out.println("Update success");

            // set the corresponding param
            stmt.setString(1, email);
            stmt.setString(2, phone_no);
            stmt.setString(3, staff_id);
            // update
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is used to insert booking data into tbl_booking.
     *
     * @param room_id User key in room id which is wanted to be booked
     * @param booking_purpose User key in booking purpose
     * @param booking_date User key in booking date for the room
     * @param booking_time User key in booking time for the room
     * @param staff_id The booking details will be recorded based on the staff id
     */
    public void insertBooking(String room_id,String booking_purpose, String booking_date, String booking_time, String staff_id) {
        String sqlInsert = "INSERT INTO tbl_booking (room_id, booking_purpose, booking_date, booking_time, staff_id) " +
                "VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, room_id);
            preparedStatement.setString(2, booking_purpose);
            preparedStatement.setString(3, booking_date);
            preparedStatement.setString(4, booking_time);
            preparedStatement.setString(5, staff_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to delete the users' data from tbl_booking.
     *
     * @param staff_id to delete the room booking by using staff id
     */
    public void deleteBooking(String staff_id) {
        String sqlDelete = "DELETE FROM tbl_booking WHERE staff_id = ?";
        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlDelete)) {
            preparedStatement.setString(1, staff_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select all details from tbl_user, tbl_room and tbl_booking for display.
     * The users can see their booking records in the list.
     *
     * @return display booking data (records)
     */
    public String displayList () {
        String bookingData = "";
        String sqlList = "SELECT tbl_user.user_name, tbl_user.ic_no, tbl_user.phone_no, tbl_user.email," +
                "tbl_booking.room_id, tbl_booking.booking_purpose, tbl_booking.booking_date," +
                "tbl_booking.booking_time, tbl_room.room_description, tbl_room.maximum_capacity FROM tbl_booking " +
                "INNER JOIN tbl_user ON tbl_user.staff_id = tbl_booking.staff_id " +
                "INNER JOIN tbl_room ON tbl_room.room_id = tbl_booking.room_id " +
                "WHERE tbl_booking.staff_id = tbl_user.staff_id";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlList)) {

            while (rs.next()) {
                bookingData +=  "User Details:" +
                        "\n1. Name: " + rs.getString("user_name") +
                        "\n2. I/C Number: " + rs.getString("ic_no") +
                        "\n3. Phone Number: " + rs.getString("phone_no") +
                        "\n4. Email: " + rs.getString("email") +
                        "\n\nRoom Details:" +
                        "\n1. Room ID: " + rs.getString("room_id") +
                        "\n2. Room Description: " + rs.getString("room_description") +
                        "\n3. Maximum Capacity: " + rs.getString("maximum_capacity") +
                        "\n\nBooking Details:" +
                        "\n1. Booking Purpose: " + rs.getString("booking_purpose") +
                        "\n2. Booking Date: " + rs.getString("booking_date") +
                        "\n3. Booking Time: " + rs.getString("booking_time") +
                        "\n---------------------------------------------------------------------\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingData;
    }

    /**
     * This method is used for the user to register as the school admin.
     *
     * @param staff_id register admin by using staff id
     * @param school_id register admin for which school by entering the school id
     */
    public void insertAdmin(String staff_id, String school_id) {
        String sqlInsertAdmin = "INSERT INTO tbl_admin (staff_id, school_id) VALUES (?,?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertAdmin)) {
            preparedStatement.setString(1, staff_id);
            preparedStatement.setString(2, school_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used for the admin to add the rooms.
     *
     * @param room_id the room that will be added according to the school
     * @param room_description the room types: Lounge/Student Hall/Lecturer Hall
     * @param maximum_capacity the room maximum capacity
     * @param school_id represent different schools
     */
    public void insertRoom(String room_id, String room_description, String maximum_capacity, String school_id) {
        String sqlInsertAdmin = "INSERT INTO tbl_room (room_id, room_description, maximum_capacity, school_id) VALUES (?,?,?,?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertAdmin)) {
            preparedStatement.setString(1, room_id);
            preparedStatement.setString(2, room_description);
            preparedStatement.setString(3, maximum_capacity);
            preparedStatement.setString(4, school_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select all details from tbl_room and tbl_school for display.
     *
     * @return room data (room records)
     */
    public String displayRoom () {
        String roomData = "";
        String sqlRoom = "SELECT tbl_room.room_id, tbl_room.room_description, tbl_room. maximum_capacity, " +
                "tbl_school.school_name FROM tbl_room INNER JOIN tbl_school ON tbl_school.school_id = tbl_room.school_id";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlRoom)) {

            while (rs.next()) {
                roomData +=  "Reply '" + rs.getString("room_id") +
                        "' for " + rs.getString("room_description") +
                        " (" + rs.getString("school_name") + ")" +
                        " - " + rs.getString("maximum_capacity") + " capacity\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomData;
    }

    /**
     * This method is used for admin to update the room details.
     *
     * @param room_id the room in different schools
     * @param room_desc the description of the room (especially the function: Lounge/Student Hall/Lecturer Hall)
     * @param room_max_cap the maximum capacity of the room
     */
    public void updateroom(String room_id, String room_desc, String room_max_cap){
        String sqlUpdateRdesc = "UPDATE tbl_room SET room_description = '" + room_desc + "', maximum_capacity = '" + room_max_cap + "' WHERE room_id = '" + room_id + "';";
        try{
            Connection conn = DatabaseConnect.connect();
            PreparedStatement stmt = conn.prepareStatement(sqlUpdateRdesc);
            stmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used to check room id whether it exists or not
     *
     * @param room_id
     * @return
     */
    public String checkRoomID(String room_id) {
        String message = "false";
        String sql = "SELECT * FROM tbl_room WHERE room_id= '" + room_id + "'";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                message = "true";
            }
            System.out.println(message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;

    }

    /**
     * This method is used to check whether the user is admin or not.
     *
     * @param school_id check by using school id to detect there is admin or not
     * @return whether the user is admin or not
     */
    public String checkAdmin(String school_id){
        String message = "false";
        String sqlcheckA = "SELECT * FROM tbl_admin WHERE school_id= '" + school_id + "'";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlcheckA)) {

            while (rs.next()) {
                message = "true";
            }
            System.out.println(message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * This method is used to check whether he is a user or not before register as admin
     *
     * @param staff_id used to check whether is user or not before register as admin
     * @return whether he or she is the user before as an admin or not
     */
    public String checkUser(String staff_id){
        String message = "false";
        String sqlcheckA = "SELECT * FROM tbl_user WHERE staff_id= '" + staff_id + "'";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlcheckA)) {

            while (rs.next()) {
                message = "true";
            }
            System.out.println(message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * This method is used to display the available room list.
     *
     * @return available room list
     */
    public String displayAvailableRoomList () {
        String roomListData = "";
        String sqlAvailableRoom = "SELECT tbl_room.room_id, tbl_room.room_description, tbl_room.maximum_capacity, " +
                "tbl_school.school_id, tbl_school.school_name FROM tbl_room INNER JOIN tbl_school ON tbl_school.school_id = tbl_room.school_id";

        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlAvailableRoom)) {

            while (rs.next()) {
                roomListData +=  "Room Details:" +
                        "\n1. Room ID: " + rs.getString("room_id") +
                        "\n2. Room Description: " + rs.getString("room_description") +
                        "\n3. Maximum Capacity: " + rs.getString("maximum_capacity") +
                        "\n\nSchool Details:" +
                        "\n1. School ID: " + rs.getString("school_id") +
                        "\n2. School Name: " + rs.getString("school_name") +
                        "\n---------------------------------------------------------------------\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomListData;
    }
}