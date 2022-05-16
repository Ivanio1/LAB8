package db;


import app.collection.*;
import org.postgresql.util.PSQLException;
import treatment.SendToClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Vector;

public class DataBase {
    private MessageDigest hash;
    private static final String URL = "jdbc:postgresql://localhost:5432/studs"; // тут будет наш локальный постгресс
    private static final String LOGIN = "s336760";
    private static final String PASS = "******";
    private Connection connection;
    private Statement statement;

    private String owner = "";


    private final String deleteCity = "DELETE FROM labworks WHERE owner = ?;";

    private final String deleteCityById = "DELETE FROM labworks WHERE owner = ? AND id = ?;";

    private final String addwork =
            "INSERT INTO labworks (name, owner, " +
                    "x, y, creationDate,minimalPoint,difficulty,pername,birthDate, eyeColor,nationality) " +
                    "VALUES (?, ?, ?, ?, ?, ? ,?,?,?, ?, ?) RETURNING id;";

    private final String addCityWithId =
            "INSERT INTO labworks (id, name, owner," +
                    "x,y,creationDate,minimalPoint,difficulty,pername,birthDate,eyeColor,nationality)" +
                    "VALUES (?,?, ?, ?, ?, ?, ? ,?,?,?, ?, ?);";

    private final String load = "SELECT * FROM labworks";

    private final String createlabworksTable =
            "CREATE TABLE IF NOT EXISTS labworks " +
                    "(id serial primary key not null," +
                    " owner TEXT NOT NULL , " +
                    " name TEXT NOT NULL , " +
                    " x INTEGER NOT NULL, " +
                    " y INTEGER NOT NULL, " +
                    " creationDate TEXT NOT NULL , " +
                    " minimalPoint INTEGER NOT NULL ," +
                    " difficulty TEXT , " +
                    " pername TEXT NOT NULL ," +
                    " birthDate TEXT  , " +
                    " eyeColor TEXT NOT NULL , " +
                    " nationality TEXT) ";


    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public DataBase() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASS);
            statement = connection.createStatement();
        } catch (PSQLException e) {
            System.out.println("Ошибка соединения с базой данных. Поправьте настройки или попробуйте позднее..");
            System.exit(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createSecretBase();
        createCitiesDB();

    }
    /**
     * Удаляет все элементы из БД по индексу
     *
     * @throws SQLException
     */
    public void removeAt(String name, String pername, String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM labworks WHERE (name=?) AND (pername=?) AND (owner = ?)");
        ps.setString(1, name);
        ps.setString(2, pername);
        ps.setString(3, login);
        ps.execute();

    }


    /**
     * Удаляет элемент из БД по его id
     *
     * @param id
     * @param login
     * @throws SQLException
     */
    public void removeById(int id, String login) throws SQLException {
       PreparedStatement ps = connection.prepareStatement("DELETE FROM labworks WHERE(id = ?) AND (owner = ?)");
        ps.setInt(1, id);
        ps.setString(2, login);
        ps.execute();
    }

    public void createCitiesDB() throws SQLException {
        this.statement = connection.createStatement();
        statement.execute(createlabworksTable);
    }

    public void addWorkInDB(LabWork work) throws SQLException {
//            Statement st = connection.createStatement();
//            ResultSet rs = st.executeQuery("INSERT INTO labworks VALUES (1111,'s336760', 'LAB1', 5, 4, 'Thu Mar 07 19:05:18 MSK 2022', 10, 'HARD', 'IVAN', null, 'GREEN', 'INDIA'),");
        PreparedStatement ps = connection.prepareStatement("INSERT INTO labworks (id,owner, name, x, y, " +
                "creationdate, minimalPoint, difficulty, pername, birthDate, eyeColor, nationality) " +
                "VALUES (? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setInt(1, work.getId());
        ps.setString(2, work.getOwner());
        ps.setString(3, work.getName());
        ps.setLong(4, work.getCoordinates().getX());
        ps.setLong(5, work.getCoordinates().getY());
        ps.setString(6, String.valueOf(work.getCreationDate()));
        ps.setDouble(7, work.getMinimalPoint());
        try {
            ps.setString(8, String.valueOf(work.getDifficulty()));
        } catch (NullPointerException e) {
            ps.setObject(8, null);
        }
        ps.setString(9, work.getAuthor().getName());
        try {
            ps.setString(10, String.valueOf(work.getAuthor().getBirthday()));
        } catch (NullPointerException e) {
            ps.setObject(10, null);
        }
        ps.setString(11, String.valueOf(work.getAuthor().getEyeColor()));
        try {
            ps.setString(12, String.valueOf(work.getAuthor().getNationality()));
        } catch (NullPointerException | SQLException e) {
            ps.setObject(12, null);
        }
        ps.execute();

    }

    /**
     * Метод получает сгенерированное id
     *
     * @return
     * @throws SQLException
     */
    public int getSQLId() throws SQLException {
        ResultSet res = statement.executeQuery("SELECT nextval('idsequence');");
        res.next();
        return res.getInt(1);
    }


    public DataBase getDataBase() {
        return this;
    }


    public boolean isValue(String word, String value) throws SQLException {
        try {
            ResultSet rs = statement.executeQuery("SELECT " + word + " FROM users");
            while (rs.next())
                if (value.equals(rs.getString(1)))
                    return true;
            return false;
        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
        }
        return false;
    }

    public void addUserToTheBase(String login, String password) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        boolean access = true;
        hash = MessageDigest.getInstance("SHA-224");
        ResultSet rs = statement.executeQuery("SELECT login FROM users");
        while (rs.next()) {
            if (login.equals(rs.getString(1))) {
                access = false;
            }
        }
        if (access) {
            String sql = "INSERT INTO users (login, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, Base64.getEncoder().encodeToString(hash.digest(password.getBytes("UTF-8"))));
            preparedStatement.execute();
            SendToClient.write("110100011000000011010000101101011101000010110011");
        } else {
            SendToClient.write("Пользователь с логином " + login + " уже зарегистрирован." + System.lineSeparator());
        }

    }

    public boolean authForExecuteCommand(String login, String password) throws NoSuchAlgorithmException {
        boolean access = false;
        hash = MessageDigest.getInstance("SHA-224");
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            String pass = Base64.getEncoder().encodeToString(hash.digest(password.getBytes("UTF-8")));
            // System.out.println(pass+"\n\n\n\n\n\n\n\n\n");
            while (rs.next()) {
                if (login.equals(rs.getString("login")) && pass.equals(rs.getString("password"))) {
                    access = true;
                }
            }
        } catch (PSQLException e) {
            System.out.println("Произошла ошибка при подключении к БД. Проверьте подключение и отправьте запрос еще раз!");
            System.exit(0);
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return access;
    }

    public void authorizationUser(String login, String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        boolean access = false;
        hash = MessageDigest.getInstance("SHA-224");
        ResultSet rs = statement.executeQuery("SELECT * FROM users");
        String pass = Base64.getEncoder().encodeToString(hash.digest(password.getBytes("UTF-8")));
        while (rs.next()) {
            if (login.equals(rs.getString("login")) && pass.equals(rs.getString("password"))) {
                access = true;
            }
        }
        if (access) {
            SendToClient.write("110100001011000011010000101100101101000110000010");
        } else {
            SendToClient.write("Вы ввели неверный логин или пароль. Попробуйте снова!" + System.lineSeparator());
        }
    }


    public void createSecretBase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS  users " +
                "(login TEXT, " +
                " password TEXT)";
        statement.execute(createTableSQL);
    }

    public boolean clearCities(String username) {
        try {
            int count = 0;
            Long localId = 1L;
            boolean isAccess = false;

            PreparedStatement ps = connection.prepareStatement(deleteCity);
            ps.setString(1, username);
            if (ps.executeUpdate() == 0) System.out.println("UPADETR == 0");
            else {
                System.out.println("UPADETR != 0");
                ResultSet rs = statement.executeQuery("Select * from cities;");
                while (rs.next()) {
                    count += 1;
                }
                if (count == 0) {
                    PreparedStatement statement = connection.prepareStatement("alter sequence cities_id_seq restart with " + localId.toString() + ";");
                    statement.executeUpdate();
                }
                isAccess = true;
            }
            return isAccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeById(long id, String username) {
        try {
            boolean isAccess;
            PreparedStatement ps = connection.prepareStatement(deleteCityById);
            ps.setString(1, username);
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) isAccess = false;
            else {
                isAccess = true;
            }
            return isAccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean replace_if_greater(long id, City city, String username) {
//        boolean access = false;
//        try {
//            String request1 = "SELECT * FROM cities WHERE id = ?";
//            PreparedStatement ps = connection.prepareStatement(request1);
//            ps.setLong(1, id);
//            if (ps.execute()) {
//                System.out.println(access + " execute..");
//                ResultSet resultSet = ps.executeQuery();
//                resultSet.next();
//                if (resultSet.getInt("population") < city.getPopulation()) {
//                    System.out.println(access + " compare populations!");
//                    if (removeById(resultSet.getLong(1), owner)) {
//                        System.out.println(access + "remove by id");
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//                        String cityCreationDate = city.getCreationDate().format(formatter);
//                        String governorBirthday = city.getGovernor().getDateOfBirthday().format(formatter);
//
//                        PreparedStatement _ps = connection.prepareStatement(addCityWithId);
//                        _ps.setLong(1, id);
//                        _ps.setString(2, city.getName());
//                        _ps.setString(3, owner);
//                        _ps.setInt(4, city.getCoordinates().getX());
//                        _ps.setFloat(5, city.getCoordinates().getY());
//                        _ps.setString(6, cityCreationDate);
//                        _ps.setLong(7, city.getArea());
//                        _ps.setInt(8, city.getPopulation());
//                        _ps.setLong(9, city.getMetersAboveSeaLevel());
//                        if (city.getClimate() != null) {
//                            _ps.setString(10, city.getClimate().getRussianName());
//                        } else {
//                            _ps.setNull(10, Types.VARCHAR);
//                        }
//                        if (city.getClimate() != null) {
//                            _ps.setString(11, city.getGovernment().getRussianName());
//                        } else {
//                            _ps.setNull(11, Types.VARCHAR);
//                        }
//                        _ps.setString(12, city.getStandardOfLiving().getRussianName());
//                        _ps.setString(13, governorBirthday);
//                        _ps.setFloat(14, city.getGovernor().getAge());
//                        if (_ps.executeUpdate() == 0) {
//                            System.out.println(access + "update = 0");
//                            return false;
//                        }
//                        System.out.println(access + "update = true");
//                        return true;
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//

    /**
     * Метод обновляет в БД элемент по его id
     *
     * @param id
     * @param login
     * @return
     * @throws SQLException
     */
    public boolean update(int id, String login, LabWork work) throws SQLException, NullPointerException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET name = ? , x = ? , y = ?" +
                    ", creationdate = ?, minimalPoint = ? , difficulty = ?, pername = ?, birthDate = ?, eyeColor = ? " +
                    ", nationality = ? WHERE id = ? AND owner = ?;");
            ps.setString(1, work.getName());
            ps.setLong(2, work.getCoordinates().getX());
            ps.setLong(3, work.getCoordinates().getY());
            // System.out.println(work.getCreationDate()+" "+work.getAuthor().getBirthday());
            ps.setString(4, String.valueOf(work.getCreationDate()));
            ps.setDouble(5, work.getMinimalPoint());
            try {
                ps.setString(6, String.valueOf(work.getDifficulty()));
            } catch (NullPointerException e) {
                ps.setObject(6, null);
            }
            try {
                ps.setString(6, String.valueOf(work.getDifficulty()));
            } catch (NullPointerException e) {
                ps.setObject(6, null);
            }
            ps.setString(7, work.getAuthor().getName());
            try {
                ps.setString(8, String.valueOf(work.getAuthor().getBirthday()));
            } catch (NullPointerException e) {
                ps.setObject(8, null);
            }
            ps.setString(9, String.valueOf(work.getAuthor().getEyeColor()));
            try {
                ps.setString(10, String.valueOf(work.getAuthor().getNationality()));
            } catch (NullPointerException e) {
                ps.setObject(10, null);
            }
            ps.setInt(11, id);
            ps.setString(12, login);
            ps.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void loadCollection(Vector<LabWork> repositoryOfCities) throws SQLException {

        ResultSet resultSet = statement.executeQuery(load);
        repositoryOfCities.clear();
        while (resultSet.next()) {
            repositoryOfCities.add(
                    new LabWork(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            new Coordinates(resultSet.getInt(4),
                                    resultSet.getInt(5)),
                            resultSet.getDouble(7),
                            resultSet.getString(6), Difficulty.StringNameToObj(resultSet.getString(8))
                            , new Person(resultSet.getString(9), resultSet.getString(10),
                            Color.StringNameToObj(resultSet.getString(11)), Country.StringNameToObj(resultSet.getString(12)))
                    ));
        }

    }
//
//    public boolean removeLower(City city) {
//        try {
//            int index = city.getPopulation();
//            String request1 = "SELECT * FROM cities WHERE population < ?";
//            PreparedStatement ps = connection.prepareStatement(request1);
//            ps.setInt(1, index);
//            if (ps.execute()) {
//                ResultSet resultSet = ps.executeQuery();
//                while (resultSet.next()) {
//                    removeById(resultSet.getLong(1), owner);
//                }
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean removeGreater(City city) {
//        try {
//            int index = city.getPopulation();
//            String request1 = "SELECT * FROM cities WHERE population < ?";
//            PreparedStatement ps = connection.prepareStatement(request1);
//            ps.setInt(1, index);
//            if (ps.execute()) {
//                ResultSet resultSet = ps.executeQuery();
//                while (resultSet.next()) {
//                    removeById(resultSet.getLong(1), owner);
//                }
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
