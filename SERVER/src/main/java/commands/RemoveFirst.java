package commands;

import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class RemoveFirst extends Command{
    private Receiver receiver;


    public RemoveFirst(Receiver receiver) {
        setName("remove_first");
        setDescription("удаляет первый элемент");
        this.receiver = receiver;
    }


    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, String args) {
       receiver.remove_first();
    }
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager) {
        receiver.remove_first();
    }
}
