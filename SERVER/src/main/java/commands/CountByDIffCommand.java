package commands;

import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class CountByDIffCommand extends Command{
    private Receiver receiver;


    public CountByDIffCommand(Receiver receiver) {
        setName("count_by_difficulty");
        setDescription("посчитать по сложности");
        this.receiver = receiver;
    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, String args) {
        receiver.countbydiff(args);
    }
}
