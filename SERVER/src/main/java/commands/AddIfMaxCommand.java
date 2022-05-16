package commands;

import app.collection.LabWork;
import executors.Invoker;
import executors.Receiver;

import java.sql.SQLException;
import java.util.TreeMap;

public class AddIfMaxCommand extends Command{
    private Receiver receiver;


    public AddIfMaxCommand(Receiver receiver) {
        setName("add_if_max");
        setDescription("добавить если больше");
        this.receiver = receiver;
    }

    @Override
    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, LabWork work) throws SQLException {
        receiver.add_if_max(work);
    }

}
