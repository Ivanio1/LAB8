package treatment;




import db.DataBase;
import executors.Invoker;
import io.Message;

import java.io.*;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class GetFromClient implements Runnable {

    private SocketChannel channel;
    private Invoker invoker;
    private DataBase dataBase;
    private Selector readerSelector;


    public GetFromClient(SocketChannel channel, Invoker invoker, DataBase dataBase) {
        this.channel = channel;
        this.invoker = invoker;
        this.dataBase = dataBase;
        try {
            readerSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        int i = 0;
        try {
            // хватаем канал коннекта
            ByteBuffer bb = ByteBuffer.allocate(5000); //буффер для channel.read()
            // селектором произвожу очередь на выоленение в этом потоке
            //регистрирую канал на чтение
            channel.register(readerSelector, SelectionKey.OP_READ);
            //немного поспим, на самом, больше подстраховка, нежели необходимость.
             System.out.println(channel.socket().getChannel() + " - Адресат");
            int N = readerSelector.select();
            // если количество оижадающих больше 0..
            if (N > 0) {
                int count = 0;
                i++;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // сложные манипуляции по праавильному прочтению команды
               // System.out.println("Считываем сообщение от клиента..");
                try {
                    while ((count = channel.read(bb)) > 0) {
                        bb.flip();
                        baos.write(bb.array(), 0, count);
                        bb.clear();
                       // System.out.println("Размер полученного сообщения: " + baos.size());
                        ByteArrayInputStream bios = new ByteArrayInputStream(baos.toByteArray());

                        ObjectInputStream ois = new ObjectInputStream(bios);
                        Message message = (Message) ois.readObject();
                        System.out.println("Поступил запрос на выполнение команды: " + message.getCommandName());

                        // создается поток, который занимается инициализацией команды и выполнением.
                        Transaction transaction = new Transaction(message, invoker, channel);
                        Thread commandTransaction = new Thread(transaction, "Transaction");
                        commandTransaction.start();
                    }
                }catch (IOException e){
                    System.out.println("Client disabled");
                    i--;
                    if (i == 0) {
                        System.out.println("На сервере нет активного клиента, введите exit и нажмите ENTER - если хотите выключить сервер.");
                        Runnable save = () -> {
                            try {
                                serverMod();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        };
                        new Thread(save).start();

                    }
                }

            }


        } catch (IOException | ClassNotFoundException e) { // беда, коннект отпал внезапно...
            e.printStackTrace();
            System.out.println("-------------------");
            try {
                channel.close();
            } catch (IOException ex) {
                System.out.println("Произошла ошибка закрытия канала чтения. ");
            }
            System.out.println("Подключение закрылось на этапе чтения сообщения.");
        }
    }
    /**
     * Метод для обработки команд save и exit на сервере
     */
    public void serverMod() throws IOException {
        String s = "";
        Scanner scanner=new Scanner(System.in);
        switch (scanner.nextLine()) {
            case "exit":
               System.out.println("Программа сервера успешно завершена.");
                System.exit(0);
                break;
            default:
                System.out.println("На сервере поддерживаются только команда exit.");

        }
    }
}
