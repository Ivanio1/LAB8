package request;

public enum Commands {

    REMOVE("remove"),
    CLEAR("clear"),
    EXECUTESCRIPT("execute_script"),
    EXIT("exit"),
    HELP("help"),
    INFO("info"),
    ADD("add"),
    ADD_IF_MAX("add_if_max"),
    REMOVEAT("remove_at"),
    REMOVEFIRST("remove_first"),
    MAXBYAUTHOR("max_by_author"),
    FILTERGREATER("filter_greater_than_minimal_point"),
    COUNTBYDIFF("count_by_difficulty"),
    SHOW("show"),
    UPDATEID("update_id");


    /**
     * commandName - название команды в системе
     */

    final private String commandName;

    /**
     * Перечислены команды, которые имеют реализацию на сервере.
     *
     * @param commandName - название команды в системе
     */

    Commands(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
