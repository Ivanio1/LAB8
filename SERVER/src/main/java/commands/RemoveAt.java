package commands;


import app.collection.LabWork;

import executors.Invoker;
import executors.Receiver;

import java.sql.SQLException;
import java.util.TreeMap;

public class RemoveAt extends Command {


    private Receiver receiver;


    public RemoveAt(Receiver receiver) {
        setName("remove_at");
        setDescription("удалить элемент по индексу");
        this.receiver = receiver;
    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, String args) {
        receiver.remove_at(Integer.parseInt(args));
    }
}

