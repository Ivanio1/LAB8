package executors;


import app.collection.*;
import db.DataBase;
import treatment.SendToClient;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * Класс, который хранит в себе коллекцию объектов и хранит реализациюю некоторых команд, связанных с коллекцией напрямую
 */

public class RepositoryOfLabwork {

    protected static HashMap<String, String> manual;
    private final Vector<LabWork> worksCollection = new Vector<LabWork>();

    private LabWork Labwork;
    private LocalDateTime birthdayOfTreemap;
    private DataBase dataBase;


    public RepositoryOfLabwork(DataBase dataBase) {

        try {
            this.birthdayOfTreemap = LocalDateTime.now();
            this.dataBase = dataBase;
            dataBase.loadCollection(worksCollection);
        } catch (SQLException e) {
            System.out.println("Чпуньк");
        }

    }

    public void info() {
        if (!getWorksCollection().isEmpty()) {
            SendToClient.write(getWorksCollection().size() + "," + getWorksCollection().getClass().getTypeName());
        } else {
            SendToClient.write("EMPTY");
        }
    }

    public String script_info() {
        String s = "";
        if (!getWorksCollection().isEmpty()) {
            s = getWorksCollection().size() + "," + getWorksCollection().getClass().getTypeName();
        } else {
            s = "Коллекция пуста";
        }
        return s;
    }

    public void max_by_author() {
        String s = "";
        if (!getWorksCollection().isEmpty()) {
            try {
                ArrayList<String> names = new ArrayList<>();
                for (LabWork work : getWorksCollection()) {
                    String a = work.getAuthor().getName();
                    names.add(a);
                }
                String Max_name = "";
                for (int i = 0; i < names.size(); i++) {
                    if (names.get(i).length() > Max_name.length()) {
                        Max_name = names.get(i);
                    }
                }

                for (LabWork work : getWorksCollection()) {
                    if (Objects.equals(work.getAuthor().getName(), Max_name)) {
                        s = work.toString();
                    }
                }
                SendToClient.write(s);
            } catch (NoSuchElementException e) {
                SendToClient.write("EMPTY");
            }
        } else {
            SendToClient.write("EMPTY");
        }
    }

    public String script_max_by_author() {
        String s = "";
        if (!getWorksCollection().isEmpty()) {
            try {
                ArrayList<String> names = new ArrayList<>();
                for (LabWork work : getWorksCollection()) {
                    String a = work.getAuthor().getName();
                    names.add(a);
                }
                String Max_name = "";
                for (int i = 0; i < names.size(); i++) {
                    if (names.get(i).length() > Max_name.length()) {
                        Max_name = names.get(i);
                    }
                }

                for (LabWork work : getWorksCollection()) {
                    if (Objects.equals(work.getAuthor().getName(), Max_name)) {
                        s = work.toString();
                    }
                }

            } catch (NoSuchElementException e) {
                s = "Коллекция пуста";
            }
        } else {
            s = "Коллекция пуста";
        }
        return s;
    }

    public void remove_at(String owner, int in) {
        if (!(getWorksCollection().size() == 0)) {
            try {
                LabWork type = getWorksCollection().get(in);
                String name = type.getName();
                String pername = type.getAuthor().getName();
                String log = type.getOwner();
                try {
                    if (Objects.equals(log, owner)) {
                        dataBase.removeAt(name, pername, owner);
                        getWorksCollection().remove(in);
                        SendToClient.write("YES");
                    } else {
                        SendToClient.write("NOT");
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    SendToClient.write("INDEX");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                SendToClient.write("INDEX");
            }
        } else {
            SendToClient.write("EMPTY");
        }
    }

    public String script_remove_at(String owner, int in) {
        String s = "";
        if (!(getWorksCollection().size() == 0)) {
            try {
                LabWork type = getWorksCollection().get(in);
                String name = type.getName();
                String pername = type.getAuthor().getName();
                String log = type.getOwner();
                try {
                    if (Objects.equals(log, owner)) {
                        dataBase.removeAt(name, pername, owner);
                        getWorksCollection().remove(in);
                        s = "Успешное удаление";
                    } else {
                        s = "Объект Вам не принадлежит";
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    s = "По данному индексу нет объекта";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                s = "По данному индексу нет объекта";
            }
        } else {
            s = "Коллекция пуста";
        }
        return s;
    }

    public void remove(String owner, int in) {
        try {
            if (!(getWorksCollection().size() == 0)) {
                if (getWorksCollection().removeIf(col -> col.getId() == in && col.getOwner().equals(owner))) {
                    dataBase.removeById(in, owner);
                }
            } else {
                SendToClient.write("EMPTY");
            }
        } catch (SQLException e) {
            SendToClient.write("NOT");
        }
    }

    public void countbydiff(String arg) {
        String s = "";
        Vector<LabWork> works = getWorksCollection();
        if (works.size() != 0) {
            int c = 0;
            int n = 0;
            for (LabWork work : works) {
                if (work.getDifficulty() != null) {

                    if (Objects.equals(work.getDifficulty().toString(), arg) && work.getDifficulty() != null) {

                        c += 1;
                    }
                } else {
                    n += 1;
                    s += "У " + n + " элементов коллекции сложности нет.";
                }
            }

            SendToClient.write(arg + "," + c);
        } else SendToClient.write("EMPTY");
    }

    public String script_countbydiff(String arg) {
        String s = "";
        Vector<LabWork> works = getWorksCollection();
        if (works.size() != 0) {
            int c = 0;
            int n = 0;
            for (LabWork work : works) {
                if (work.getDifficulty() != null) {

                    if (Objects.equals(work.getDifficulty().toString(), arg) && work.getDifficulty() != null) {

                        c += 1;
                    }
                } else {
                    n += 1;
                    s += "У " + n + " элементов коллекции сложности нет.";
                }
            }

        } else s = "Коллекция пуста";
        return s;
    }

    public void filtergreater(String arg) {
        StringBuilder s = new StringBuilder();
        Vector<LabWork> works = getWorksCollection();
       // Stream< LabWork> stream =getWorksCollection().stream().filter(x->x.getMinimalPoint()==Double.parseDouble(arg)).forEach(x->s.append(x));
        //s.append(stream);
        try {
            if (works.size() != 0) {
                for (LabWork work : works) {
                    if (work.getMinimalPoint() == Double.parseDouble(arg.trim())) {
                        s.append(work).append("\n");
                    }
                }
                SendToClient.write(s.toString());
            } else SendToClient.write("EMPTY");
        } catch (NumberFormatException e) {
            SendToClient.write("WRONG");
        }
    }

    public String script_filtergreater(String arg) {
        String s = "";
        Vector<LabWork> works = getWorksCollection();
        try {
            if (works.size() != 0) {
                for (LabWork work : works) {
                    if (work.getMinimalPoint() >= Double.parseDouble(arg.trim())) {
                        s += work + "\n";
                    }
                }

            } else s = "Коллекция пуста";
        } catch (NumberFormatException e) {
            s = "Неверный формат введенных данных";
        }
        return s;
    }

    /**
     * Выводит на экран список доступных пользователю команд.
     */
    public String script_help() {
        String s = "Данные коллекции сохраняются автоматически после каждой успешной модификации.\n" + "Команды: " + manual.keySet();
        return s;

    }

    {
        manual = new HashMap<>();
        manual.put("remove_first", "удалить первый элемент из коллекции.");
        manual.put("add", "Добавить новый элемент в коллекцию.");
        manual.put("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении.");
        manual.put("update", "обновить значение элемента коллекции, id которого равен заданному.");
        manual.put("info", "Вывести в стандартный поток вывода информацию о коллекции.");
        manual.put("remove_at", "удалить элемент, находящийся в заданной позиции коллекции.");
        manual.put("add_if_max", " добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.");
        manual.put("exit", "Сохранить коллекцию в файл и завершить работу программы.");
        manual.put("max_by_author", "вывести любой объект из коллекции, значение поля author которого является максимальным.");
        manual.put("count_by_difficulty", "вывести количество элементов, значение поля difficulty которых равно заданному.");
        manual.put("filter_greater_than_minimal_point", "вывести элементы, значение поля minimalPoint которых больше заданного.");
    }

    /**
     * Метод реализует вывод количества элементов, находящихся в коллекции в данный момент
     */

    public void size() {
        SendToClient.write("В коллекции сейчас " + getWorksCollection().size() + " объекта/ов");
    }


    /**
     * Реализация вывода всех объектов коллекции
     */

    public void show() {
        for (LabWork e : getWorksCollection()) {
            //System.out.println(e);
            SendToClient.write(e);
        }
    }

    public String script_show() {
        String s = "";
        if (!getWorksCollection().isEmpty()) {
            for (LabWork e : getWorksCollection()) {
                s += e + "\n";
            }
        }else s="Коллекция пуста";
        return s;
    }

    public void update_id(LabWork work, int id) {
        if (!getWorksCollection().isEmpty()) {
            System.out.println(work.getOwner());
            if (getWorksCollection().removeIf(col -> col.getId() == id && col.getOwner().trim().equals(work.getOwner().trim()))) {
                work.setId(id);
                getWorksCollection().add(work);
                SendToClient.write("YES");
            } else {
                SendToClient.write("NOT");
            }
        } else {
            SendToClient.write("EMPTY");
        }
    }

    public String UPDATE(LabWork work, int id) {
        String s = "";
        if (!getWorksCollection().isEmpty()) {
            //System.out.println(work.getOwner());
            if (getWorksCollection().removeIf(col -> col.getId() == id && col.getOwner().trim().equals(work.getOwner().trim()))) {
                work.setId(id);
                getWorksCollection().add(work);
                s = "Успешное обновление!";
            } else {
                s = "Объект Вам не принадлежит";
            }
        } else {
            s = "Коллекция пуста";
        }
        return s;
    }

    public LabWork script_update(String line) throws ParseException {
        String[] args = line.split(",");
        String login = dataBase.getOwner();
        if (isNumeric(args[0]) && isNumeric(args[2]) && isNumeric(args[3]) && isDouble(args[4])) {
            LabWork W = null;
            try {
                int id = Integer.parseInt(args[0].trim());
                String name = args[1];
                String pname = null;
                Color color = null;
                Country country = null;
                Date birth = null;
                Coordinates coordinates = new Coordinates(Long.parseLong(args[2]), Long.parseLong(args[3]));
                java.util.Date creationDate = java.util.Date.from(Instant.now());
                Double minimalPoint = Double.parseDouble(args[4]);
                // System.out.println(args[0]+args[1]+args[2]+args[3]);
                if (Objects.equals(args[5], "EASY") || Objects.equals(args[5], "HARD") || Objects.equals(args[5], "VERY_HARD") || Objects.equals(args[5], "HOPELESS")) {
                    Difficulty diff = Difficulty.valueOf(args[5]);
                    pname = args[6];
                    color = Color.valueOf(args[7]);
                    if (args.length > 8) {
                        country = Country.valueOf(args[8]);
                        if (args.length > 9) {
                            birth = (Date) new SimpleDateFormat("dd.MM.yyyy").parse(args[9]);
                            Person p = new Person(pname, birth.toString(), color, country);
                            W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, diff, p);

                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, diff, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, diff, p);
                    }
                } else {
                    pname = args[5];
                    color = Color.valueOf(args[6]);
                    if (args.length > 7) {
                        country = Country.valueOf(args[7]);
                        if (args.length > 8) {
                            birth = (Date) new SimpleDateFormat("dd.MM.yyyy").parse(args[8]);
                            Person p = new Person(pname, birth.toString(), color, country);
                            W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, p);
                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, login, name, coordinates, creationDate.toString(), minimalPoint, p);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля неверно");
            } catch (NullPointerException e) {
                System.out.println("ERROR! Значение полей неверно");
            }
            return W;
        }
        return null;
    }

    public static boolean isNumeric(String string) {
        int intValue;


        if (string == null || string.equals("")) {

            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    public static boolean isDouble(String string) {
        double intValue;


        if (string == null || string.equals("")) {
            ;
            return false;
        }

        try {
            intValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    public Vector<LabWork> getWorksCollection() {
        return worksCollection;
    }


    public void add(LabWork Labwork, int id) {
        try {
            getWorksCollection().add(Labwork);
            //SendToClient.write(Labwork.getName() + "'  добавлен в коллекцию");
        } catch (StackOverflowError e) {
            //SendToClient.write("Коллекция переполнена!");
        }
    }
    public LabWork script_add(String line) throws ParseException {
        String login=dataBase.getOwner();
        String[] args = line.split(",");
        if (isNumeric(args[1])&&Integer.parseInt(args[1])>0 &&Integer.parseInt(args[1])<626 &&Integer.parseInt(args[2])<172 &&Integer.parseInt(args[1])>0 && isNumeric(args[2]) && isDouble(args[3])) {
            LabWork W = null;
            try {
                int id = dataBase.getSQLId();
                String name = args[0];
                String pname = null;
                Color color = null;
                Country country = null;
                Date birth = null;
                Coordinates coordinates = new Coordinates(Long.parseLong(args[1]), Long.parseLong(args[2]));
                java.util.Date creationDate = java.util.Date.from(Instant.now());
                Double minimalPoint = Double.parseDouble(args[3]);
                // System.out.println(args[0]+args[1]+args[2]+args[3]);
                if (Objects.equals(args[4], "EASY") || Objects.equals(args[4], "HARD") || Objects.equals(args[4], "VERY_HARD") || Objects.equals(args[4], "HOPELESS")) {
                    Difficulty diff = Difficulty.valueOf(args[4]);
                    pname = args[5];
                    color = Color.valueOf(args[6]);
                    if (args.length > 7) {
                        country = Country.valueOf(args[7]);
                        if (args.length > 8) {
                            birth = (Date) new SimpleDateFormat("dd.MM.yyyy").parse(args[8]);
                            Person p = new Person(pname, birth.toString(), color, country);
                            W = new LabWork(id, login,name, coordinates, creationDate.toString(), minimalPoint, diff, p);

                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id,login, name, coordinates, creationDate.toString(), minimalPoint, diff, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, login,name, coordinates, creationDate.toString(), minimalPoint, diff, p);
                    }
                } else {
                    pname = args[4];
                    color = Color.valueOf(args[5]);
                    if (args.length > 6) {
                        country = Country.valueOf(args[6]);
                        if (args.length > 7) {
                            birth = (Date) new SimpleDateFormat("dd.MM.yyyy").parse(args[7]);
                            Person p = new Person(pname, birth.toString(), color, country);
                            W = new LabWork(id,login, name, coordinates, creationDate.toString(), minimalPoint, p);
                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id,login, name, coordinates, creationDate.toString(), minimalPoint, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id,login, name, coordinates, creationDate.toString(), minimalPoint, p);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля неверно");
            } catch (NullPointerException e) {
                System.out.println("ERROR! Значение полей неверно");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return W;
        }
        return null;
    }
    public DataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }

}

