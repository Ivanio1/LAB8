package commands;


import app.collection.LabWork;
import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class ShowCommand extends Command {


    private Receiver receiver;


    public ShowCommand(Receiver receiver) {
        setName("show");
        setDescription("вывести коллекцию в строковом представлении");
        this.receiver = receiver;
    }


    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager) {
            receiver.show();

    }
}
