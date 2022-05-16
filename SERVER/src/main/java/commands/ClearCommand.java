package commands;



import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class ClearCommand extends Command {
    private Receiver receiver;

    public ClearCommand(Receiver receiver) {
        setName("clear");
        setDescription("очистить коллекцию");
        this.receiver = receiver;
    }


    @Override
    public void execute(TreeMap<String, Command> treemap, Invoker commandManager) {
            //receiver.clear();
    }
}
