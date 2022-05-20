package io;

import app.collection.LabWork;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс, объект которого клиент отправляет серверу.
 * Объект хранит в себе наименование команды и аргументы. А также логин и пароль user'а
 */

public class Message implements Serializable {
    private User user;
    private String commandName;
    private String args;
    private LabWork Labwork;
    private File executionFile;
    private static final long serialVersionUID = 5435345435342444242L;
    private boolean isRegistration;

    public Message(String commandName, User user, boolean isRegistration) {
        this.commandName = commandName;
        this.user = user;
        this.isRegistration = isRegistration;
    }

    public Message(String commandName, User user) {
        this.commandName = commandName;
        this.user = user;
    }

    public Message(String commandName, String args, User user) {
        this.commandName = commandName;
        this.args = args;
        this.user = user;
    }

    public Message(String commandName, LabWork objectLabWork, User user) {
        this.commandName = commandName;
        this.Labwork=objectLabWork;
        this.user = user;
    }
    public Message(String commandName) {
        this.commandName = commandName;

    }
    public Message(String commandName, String args, LabWork objectLabWork, User user) {
        this.commandName = commandName;
        this.args = args;
        this.Labwork=objectLabWork;
        this.user = user;
    }

    public Message(String commandName, File file, User user) {
        this.commandName = commandName;
        executionFile = file;
        this.user = user;
    }


    public String getCommandName() {
        return commandName;
    }

    public String getArgs() {
        return args;
    }

    public LabWork getLabWork() {
        return Labwork;
    }

    public File getExecutionFile() {
        return executionFile;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRegistration() {
        return isRegistration;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setLabWork(LabWork LabWork) {
        this.Labwork = LabWork;
    }

    public void setExecutionFile(File executionFile) {
        this.executionFile = executionFile;
    }

    public void setRegistration(boolean registration) {
        isRegistration = registration;
    }

    public User getUser() {
        return user;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user=" + user +
                ", commandName='" + commandName + '\'' +
                ", args='" + args + '\'' +
                ", LabWork=" + Labwork +
                ", executionFile=" + executionFile +
                ", isRegistration=" + isRegistration +
                '}';
    }
}
