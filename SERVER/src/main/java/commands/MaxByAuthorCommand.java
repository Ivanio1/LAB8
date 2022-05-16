package commands;

import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class MaxByAuthorCommand extends Command{
    private Receiver receiver;

    public MaxByAuthorCommand(Receiver receiver) {
        setName("max_by_author");
        setDescription("Максимум по имени автора.");
        this.receiver = receiver;
    }


    @Override
    public void execute(TreeMap<String, Command> treemap, Invoker commandManager) {
        receiver.max_by_author();
    }
}
