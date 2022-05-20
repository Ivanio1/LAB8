package executors;


import app.collection.LabWork;
import commands.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.TreeMap;

public class Invoker {

    private Receiver receiver;

    public Invoker(Receiver receiver) {
        this.receiver = receiver;
        Command remove = new RemoveCommand(receiver);
        Command clear = new ClearCommand(receiver);
        Command help = new HelpCommand(receiver);
        Command info = new InfoCommand(receiver);
        Command add = new AddCommand(receiver);
        Command add_if_max = new AddIfMaxCommand(receiver);
        Command remove_at = new RemoveAt(receiver);
        Command remove_first = new RemoveFirst(receiver);
        Command show = new ShowCommand(receiver);
        Command updateId = new UpdateIdCommand(receiver);
        Command exit = new ExitCommand(receiver);
        Command maxbyauth = new MaxByAuthorCommand(receiver);
        Command executeScript = new ExecuteScriptCommand(receiver);
        Command filter=new FilterGreater(receiver);
        Command count=new CountByDIffCommand(receiver);
        this.setCommandMap(remove.getName(), remove);
        this.setCommandMap(filter.getName(),filter);
        this.setCommandMap(count.getName(),count);
        this.setCommandMap(clear.getName(), clear);
        this.setCommandMap(maxbyauth.getName(), maxbyauth);
        this.setCommandMap(help.getName(), help);
        this.setCommandMap(info.getName(), info);
        this.setCommandMap(add.getName(), add);
        this.setCommandMap(add_if_max.getName(), add_if_max);
        this.setCommandMap(remove_at.getName(), remove_at);
        this.setCommandMap(remove_first.getName(), remove_first);
        this.setCommandMap(show.getName(), show);
        this.setCommandMap(updateId.getName(), updateId);
        this.setCommandMap(exit.getName(), exit);
        this.setCommandMap(executeScript.getName(), executeScript);

    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    private final TreeMap<String, Command> commandMap = new TreeMap<>();

    public void setCommandMap(String commandName, Command command) {
        commandMap.put(commandName, command);

    }


    /**
     * Метод выполняет команду, с заданным ему ее названием commandName
     *
     * @param commandName название команды
     * @param commandMap  карта команды
     * @param args        аргументы команды
     * @throws IOException ошибка ввода
     */

    public void execute(String commandName, TreeMap<String, Command> commandMap, String args) throws IOException, ParseException {
        Command command = commandMap.get(commandName);
        if (command == null) throw new IllegalStateException("Команды с названием " + commandName + " не существует");
        command.execute(commandMap, this, args);
    }

    public void execute(String commandName, TreeMap<String, Command> commandMap, LabWork city) throws SQLException {
        Command command = commandMap.get(commandName);
        command.execute(commandMap, this, city);
    }

    public void execute(String commandName, TreeMap<String, Command> commandMap) {
        Command command = commandMap.get(commandName);
        if (command == null) throw new IllegalStateException("Команды с названием " + commandName + " не существует");
        command.execute(commandMap, this);
    }

    public void execute(String commandName, TreeMap<String, Command> commandMap, LabWork city, String... args) throws IOException {
        Command command = commandMap.get(commandName);
        command.execute(commandMap, this, city, args);
    }

    public void execute(String commandName, TreeMap<String, Command> commandMap, File file) throws IOException {
        Command command = commandMap.get(commandName);
        command.execute(commandMap, this, file);
    }


    public TreeMap<String, Command> getCommandMap() {
        return commandMap;
    }


    @Override
    public String toString() {
        return "Switch{" +
                "commandMap=" + commandMap +
                '}';
    }
}

