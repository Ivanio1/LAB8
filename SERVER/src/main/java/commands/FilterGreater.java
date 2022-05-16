package commands;

import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class FilterGreater extends Command{
    private Receiver receiver;


    public FilterGreater(Receiver receiver) {
        setName("filter_greater");
        setDescription("filter_greater");
        this.receiver = receiver;
    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, String args) {
        receiver.filterGreater(args);
    }
}
