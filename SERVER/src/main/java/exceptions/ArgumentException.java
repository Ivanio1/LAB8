package exceptions;

/**
 * Ошибка некорректного значения аргумента команды
 */

public class ArgumentException extends RuntimeException {
    public ArgumentException(){
        super("Вы ввели некорректные аргументы для команды! " + System.lineSeparator() + "Попробуйте заново!");

    }
}

