package commands;


import app.collection.LabWork;
import executors.Invoker;
import executors.Receiver;


import java.util.TreeMap;

public class UpdateIdCommand extends Command {


    private Receiver receiver;


    public UpdateIdCommand(Receiver receiver) {
        setName("update_id");
        setDescription("обновить элемент по заданному id - [key]");
        this.receiver = receiver;
    }



    public void execute(TreeMap<String, Command> commandTreeMap,
                        Invoker commandManager, LabWork work){
        try {

            receiver.updateId(work);
        }catch (NumberFormatException e){
            System.out.println("Аргумент имеет недопустимое значение.");
        }

    }
}
