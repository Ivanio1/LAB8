package executors;


import app.collection.LabWork;
import app.collection.ScriptRecursionException;
import app.collection.WrongAmountOfElementsException;
import commands.Command;
import db.DataBase;
import exceptions.MessageErrors;
import treatment.SendToClient;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.stream.Stream;

;


/**
 * Класс, который обеспечивает выполнение команд
 */

public class Receiver implements MessageErrors {


    Set<String> fileNames = new HashSet<>();
    RepositoryOfLabwork repositoryOfwork;
    ReadWriteLock lock;
    DataBase dataBase;


    public Receiver(RepositoryOfLabwork repositoryOfCity, DataBase dataBase) {
        this.repositoryOfwork = repositoryOfCity;
        lock = new ReentrantReadWriteLock();
        this.dataBase = dataBase;
    }

    public DataBase getDataBase() {
        return dataBase;
    }


    /**
     * Реализация команды help, выводит все справку по командам консоли
     *
     * @param commandTreeMap карта, хранящая все команды
     */

    public void help(TreeMap<String, Command> commandTreeMap) {
        lock.readLock().lock();
        try {
            SendToClient.write(System.lineSeparator() + "СПРАВКА ПО КОМАНДАМ:" + System.lineSeparator());
            for (Map.Entry<String, Command> entry : commandTreeMap.entrySet()) {
                SendToClient.write(entry.getKey() + " -" + entry.getValue().getArgs() + " " + entry.getValue().getDescription() + System.lineSeparator());
            }
            SendToClient.write(System.lineSeparator() + "*: если в описании команды имеется '[key]', значит у неё есть аругмент. " + System.lineSeparator() +
                    " : команда 'execute_script' имеет аргумент [file name] - название файла.");
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * Обеспечивает вывод информации о коллекции
     */
    public void info() {
        lock.readLock().lock();
        try {
            repositoryOfwork.info();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void filterGreater(String args) {
        lock.readLock().lock();
        try {
            repositoryOfwork.filtergreater(args);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Обеспечивает вывод всех объектов коллекции
     */
    public void show() {
        lock.readLock().lock();
        try {
            repositoryOfwork.show();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void max_by_author() {
        lock.readLock().lock();
        try {
            repositoryOfwork.max_by_author();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Обеспечивает обновление элемента по заданному id
     */

    public void updateId(LabWork work) {
        lock.writeLock().lock();
        try {
            if (dataBase.update(work.getId(), dataBase.getOwner(), work)) {
                repositoryOfwork.update_id(work, work.getId());
            } else {
                SendToClient.write("NOT");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void countbydiff(String args) {
        lock.readLock().lock();
        try {
            repositoryOfwork.countbydiff(args);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Обеспечивает добавление элемента с заданным id
     */
    public void add(LabWork city) throws SQLException {
        lock.writeLock().lock();
        int id = dataBase.getSQLId();
        try {
            if (id > 0) {
                city.setId(id);
                city.setOwner(dataBase.getOwner());
                dataBase.addWorkInDB(city);
                repositoryOfwork.add(city, id);
                SendToClient.write("Удачное добавление");
            } else {
                SendToClient.write("Возникли проблемы с добавлением элемента БД.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void add_if_max(LabWork work) throws SQLException {
        lock.writeLock().lock();
        int id = dataBase.getSQLId();
        Stream<LabWork> stream = repositoryOfwork.getWorksCollection().stream();
        Integer nameMAX = stream.filter(col -> col.getName() != null)
                .max(Comparator.comparingInt(p -> p.getName().length())).get().getName().length();
        try {
            if (work.getName() != null && work.getName().length() > nameMAX) {
                if (id > 0) {
                    work.setId(id);
                    work.setOwner(dataBase.getOwner());
                    dataBase.addWorkInDB(work);
                    repositoryOfwork.add(work, id);
                    SendToClient.write("Удачное добавление");
                } else {
                    SendToClient.write("Возникли проблемы с добавлением элемента БД.");
                }
            } else {
                SendToClient.write("Объект меньше заданного");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove_first() {
        lock.readLock().lock();
        try {
            repositoryOfwork.remove_at(dataBase.getOwner(), 0);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove_at(int index) {
        lock.readLock().lock();
        try {
            repositoryOfwork.remove_at(dataBase.getOwner(), index);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(int index) {
        lock.readLock().lock();
        try {
            repositoryOfwork.remove(dataBase.getOwner(), index);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void exit() {
        Runnable save = () -> {
            System.out.println("Введите exit для завершения работы сервера.");
            try {
                Scanner scanner=new Scanner(System.in);
               String userCommand = scanner.nextLine();
                String[] finalUserCommand = userCommand.trim().split(" ", 2);
                if(finalUserCommand[0].equals("exit")){
                    System.out.println("Завершение программы");
                    System.exit(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        new Thread(save).start();

    }

    /**
     * Метод обработки команды скрипт
     */
    public void executeScript(String arg) throws ParseException {
        lock.readLock().lock();
        try {
            String arr = readScript(arg);
            // System.out.println(arr+"\n\n\n");
            if (!Objects.equals(arr, "")) {
                String s = "";
                String login = dataBase.getOwner();
                String[] Arr = arr.split(";");
                for (String command : Arr) {
                    String[] finalUserCommand = command.split(" ", 2);
                    try {
                        switch (finalUserCommand[0]) {
                            case "":
                                break;
                            case "remove_first":
                                s += repositoryOfwork.script_remove_at(login, 0) + ";\n ";
                                break;
                            case "add":
                                if (finalUserCommand[1] != null) {
                                    LabWork city = repositoryOfwork.script_add(finalUserCommand[1].trim());
                                    //System.out.println(city.toString());

                                    try {

                                        city.setOwner(dataBase.getOwner());
                                        dataBase.addWorkInDB(city);
                                        repositoryOfwork.add(city, city.getId());
                                        s += "Удачное добавление\n";

                                    } catch (SQLException e) {
                                        e.printStackTrace();

                                    }
                                } else {
                                    s += "Неверный ввод данных в скрипте. \n";
                                }
                                break;
                            case "update":
                                if (finalUserCommand[1] != null) {
                                    LabWork work = repositoryOfwork.script_update(finalUserCommand[1]);
                                    if (dataBase.update(work.getId(), dataBase.getOwner(), work)) {
                                        s += repositoryOfwork.UPDATE(work, work.getId()) + ";\n ";
                                    } else s += "Объект вам не принадлежит\n";
                                } else {
                                    s += "Неверный ввод данных в скрипте. ";
                                }
                                break;
                            case "remove_at":
                                s += repositoryOfwork.script_remove_at(login, Integer.parseInt((finalUserCommand[1].trim()))) + ";\n ";
                                break;
                            case "show":
                                s += repositoryOfwork.script_show() + ";\n ";
                                ;
                                break;

                            case "info":
                                s += repositoryOfwork.script_info() + ";\n ";
                                break;
                            case "add_if_max":

                                LabWork work = repositoryOfwork.script_add(finalUserCommand[1].trim());
                                int id = dataBase.getSQLId();
                                Stream<LabWork> stream = repositoryOfwork.getWorksCollection().stream();
                                Integer nameMAX = stream.filter(col -> col.getName() != null)
                                        .max(Comparator.comparingInt(p -> p.getName().length())).get().getName().length();

                                if (work.getName() != null && work.getName().length() > nameMAX) {
                                    if (id > 0) {
                                        work.setId(id);
                                        work.setOwner(dataBase.getOwner());
                                        dataBase.addWorkInDB(work);
                                        repositoryOfwork.add(work, id);
                                        s += "Удачное добавление";
                                    } else {
                                        s += "Возникли проблемы с добавлением элемента БД.";
                                    }
                                } else {
                                    s += "Объект меньше заданного";
                                }


                                break;
                            case "help":
                                s += repositoryOfwork.script_help() + ";\n ";
                                break;
                            case "exit":
                                s += "\nПроцесс завершён." + ";\n ";
                                //System.exit(0);
                                break;
                            case "max_by_author":
                                s += repositoryOfwork.script_max_by_author() + ";\n ";
                                break;
                            case "execute_script":
                                s += "Рекурсия скрипта." + ";\n ";
                                break;
                            case "count_by_difficulty":
                                s += repositoryOfwork.script_countbydiff(finalUserCommand[1].trim()) + ";\n ";
                                break;
                            case "filter_greater_than_minimal_point":
                                s += repositoryOfwork.script_filtergreater(finalUserCommand[1].trim()) + ";\n ";
                                break;
                            default:
                                s += "Неопознанная команда. Наберите 'help' для справки." + ";\n ";
                        }

                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Отсутствует аргумент.");
                    } catch (ParseException | SQLException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println(s);
                SendToClient.write(s);
            } else {
                SendToClient.write("Файл со скриптом пуст");
            }

        } finally {
            lock.readLock().unlock();
        }
    }


    /**
     * Чтение данных из скрипта
     *
     * @param argument
     * @return
     */
    public String readScript(String argument) {
        StringBuilder builder = new StringBuilder();
        //System.out.println(argument);
        try {
            if (argument == null) {
                builder = null;
                throw new WrongAmountOfElementsException();
            } else {
                System.out.println("Выполняю скрипт '" + argument + "'...");
                String[] userCommand = {"", ""};
                try (Scanner scriptScanner = new Scanner(new File(argument))) {
                    if (!scriptScanner.hasNext()) throw new NoSuchElementException();
                    do {
                        userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                        if (userCommand[0].equals("update_id")) {
                            String[] comands = new String[]{"execute_script", "save", "remove_first", "add", "remove_greater", "show", "clear", "update_id", "info", "help", "man", "remove_at_index", "remove_by_id", "add_if_max", "exit", "max_by_author", "count_by_difficulty", "filter_greater_than_minimal_point"};
                            String line = (scriptScanner.nextLine().trim());
                            if (!(Arrays.asList(comands)).contains(line)) {
                                userCommand[1] = line;
                            } else {
                                userCommand[1] = "0";
                                System.out.println("Отсутствует аргумент.");
                            }

                        }
                        if (userCommand[0].equals("add")) {
                            StringBuilder command = new StringBuilder();
                            String[] comands = new String[]{"execute_script", "save", "remove_first", "add", "remove_greater", "show", "clear", "update_id", "info", "help", "man", "remove_at_index", "remove_by_id", "add_if_max", "exit", "max_by_author", "count_by_difficulty", "filter_greater_than_minimal_point"};
                            String line = (scriptScanner.nextLine().trim() + " ").split(" ")[0];
                            while (!(Arrays.asList(comands)).contains(line)) {
                                command.append(line + ",");
                                line = scriptScanner.nextLine();
                            }
                            //System.out.println(command);

                            userCommand[1] = command.toString() + ";" + line;


                        }
                        if (userCommand[0].equals("update")) {
                            StringBuilder command = new StringBuilder();
                            String[] comands = new String[]{"execute_script", "save", "remove_first", "add", "remove_greater", "show", "clear", "update_id", "info", "help", "man", "remove_at_index", "remove_by_id", "add_if_max", "exit", "max_by_author", "count_by_difficulty", "filter_greater_than_minimal_point"};
                            String line = (scriptScanner.nextLine().trim() + " ").split(" ")[0];
                            while (!(Arrays.asList(comands)).contains(line)) {
                                command.append(line + ",");
                                line = scriptScanner.nextLine();
                            }
                            //System.out.println(command);

                            userCommand[1] = command.toString() + ";" + line;


                        }
                        if (userCommand[0].equals("execute_script")) {
                            //System.out.println("Рекурсия скрипта. В скрипте не может быть команды execute_script");
                            throw new ScriptRecursionException();
                        }
                        String out = userCommand[0] + " " + userCommand[1];
                        builder.append(out + ';');
                    } while (scriptScanner.hasNextLine());
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                    System.out.println("Файл со скриптом не найден!");
                } catch (NoSuchElementException exception) {
                    System.out.println("Файл со скриптом пуст!");

                } catch (ScriptRecursionException exception) {
                    System.out.println("Скрипты не могут вызываться рекурсивно!");
                } catch (IllegalStateException exception) {
                    System.out.println("Непредвиденная ошибка!");
                    System.exit(0);
                }
            }
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Некорректные команды в скрипте!");
        }
        // System.out.println(commands);
        return String.valueOf(builder).trim();
    }


}
