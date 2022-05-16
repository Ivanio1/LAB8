package app.collection;


import lombok.AllArgsConstructor;


import java.io.Serializable;
import java.util.Date;


public class LabWork implements Comparable<LabWork>, Serializable {
    private Integer id;
    private String unique_id;
    private String name;
    private Coordinates coordinates;
    private String creationDate;
    private String date;
    private Double minimalPoint;
    private Difficulty difficulty;
    private Person author;
    private String login;

    private String owner;

    public LabWork(int id, String name, Coordinates coordinates, String creationDate, Double minimalPoint, Difficulty difficulty, Person author, String login) {
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.name = name;
        this.difficulty = difficulty;
        this.minimalPoint = minimalPoint;
        this.login = login;
    }

    public LabWork(String name, Coordinates coordinates, Double minimalPoint, Difficulty difficulty, Person author) {
        this.author = author;
        this.coordinates = coordinates;
        this.name = name;
        this.difficulty = difficulty;
        this.minimalPoint = minimalPoint;

    }

    public LabWork(String name, Coordinates coordinates, Double minimalPoint, String creationDate, Difficulty difficulty, Person author) {
        this.author = author;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.name = name;
        this.difficulty = difficulty;
        this.minimalPoint = minimalPoint;

    }

    public LabWork(int id, String owner, String name, Coordinates coordinates, Double minimalPoint, String creationDate, Difficulty difficulty, Person author) {
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.owner=owner;

    }
    public LabWork(int id, String name, Coordinates coordinates, Double minimalPoint, String creationDate, Difficulty difficulty, Person author) {
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;

    }

    public LabWork(String text, Coordinates coordinates, Difficulty stringNameToObj, Person person) {
        this.name = text;
        this.coordinates = coordinates;
        this.difficulty = stringNameToObj;
        this.author = person;
    }

    public LabWork(int id, String login, String name, Coordinates coordinates, String toString, Double minimalPoint, Difficulty diff, Person p) {
        this.author = p;
        this.id = id;
        this.owner=login;
        this.creationDate = toString;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;
        this.difficulty = diff;
    }
    public LabWork(int id, String login, String name, Coordinates coordinates, String toString, Double minimalPoint, Person p) {
        this.author = p;
        this.id = id;
        this.owner=login;
        this.creationDate = toString;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Double minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        String S = null;
        if (difficulty != null) {
            S = "LabWork {" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", coordinates=" + coordinates.toString() +
                    ", creationDate=" + creationDate +
                    ", minimalPoint=" + minimalPoint +
                    ", difficulty='" + difficulty + '\'' +
                    ", owner=" + author.toString() +
                    '}';
        }
        if (difficulty == null) {
            S = "LabWork {" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", coordinates=" + coordinates.toString() +
                    ", creationDate=" + creationDate +
                    ", minimalPoint=" + minimalPoint +
                    ", owner=" + author.toString() +
                    '}';
        }
        return S;
    }

    @Override
    public int compareTo(LabWork o) {

        return 0;
    }
}
