package commands;


import executors.Invoker;
import executors.Receiver;

import java.util.TreeMap;

public class ExitCommand extends Command {

    private Receiver receiver;

    public ExitCommand(Receiver receiver) {
        this.receiver = receiver;
        setName("exit");
        setDescription("завершить программу (с сохранением)");

    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager) {
           // receiver.exit();
    }
}
