package commands;


import app.collection.LabWork;

import executors.Invoker;
import executors.Receiver;

import java.sql.SQLException;
import java.util.TreeMap;

public class AddCommand extends Command {


    private Receiver receiver;


    public AddCommand(Receiver receiver) {
        setName("add");
        setDescription("добавить новый элемент в коллекцию");
        this.receiver = receiver;
    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, LabWork work) throws SQLException {
        receiver.add(work);
    }

    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager) {
        //receiver.add();
    }
}

