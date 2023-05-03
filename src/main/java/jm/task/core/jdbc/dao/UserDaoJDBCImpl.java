package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl extends Util implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        //""" текстовый блок. С 15 Java
        String sqlCreate = """
                CREATE TABLE IF NOT EXISTS pp_base.users(
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                name VARCHAR(25),
                                lastName VARCHAR(45),
                                age TINYINT(3) NOT NULL CHECK (age >= 0 AND age <= 127),
                                PRIMARY KEY (id)
                                )""";
        try (Connection connection = Util.getConnection()) {
            connection.prepareStatement(sqlCreate).execute();
            System.out.println("Создали шапку таблицы или она уже была создана");
        } catch (SQLException e) {
            System.err.println("Что то пошло не так с созданием шапки: " + e.getMessage());
        }
    }

    //Удаление таблицы
    public void dropUsersTable() {

        String sqlDrop = "DROP TABLE IF EXISTS users";
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(sqlDrop);
            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            System.err.println("Что то пошло не так с удалением шапки: " + e.getMessage());
        }
    }

    //Добавление юзера
    public void saveUser(String name, String lastName, byte age) {

        String saveUserSQL = "INSERT INTO users(name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveUserSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Юзер с именем " + name + " добавлен в базу данных");

        } catch (SQLException e) {
            System.err.println("Что то пошло не так с добавлением юзера: " + e.getMessage());
        }
    }

    //Чек юзер
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        String getUsersSQL = "SELECT * FROM users";
        try (Connection connection = Util.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery(getUsersSQL)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                System.out.println(user);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }


    public void removeUserById(long id) {
        String removeUserSQL = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(removeUserSQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanUsersTable() {

        String clearTable = "TRUNCATE users";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(clearTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}