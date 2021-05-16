package itmo.lab.other;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Персона, обладающая некоторыми характеристиками.
 */
@Data
public class Person implements Comparable<Person>, Serializable {

    private static final long serialVersionUID = 6L;

    /**
     * Поле - id
     * Поле не может быть null, Значение поля должно быть больше 0,
     * Значение этого поля должно быть уникальным,
     * Значение этого поля должно генерироваться автоматически
     */
    private Long id;
    /**
     * Поле - имя
     * Поле не может быть null, Строка не может быть пустой
     */
    private String name;
    /**
     * Поле - время и дата создания
     * Поле не может быть null, Значение этого поля должно генерироваться автоматически
     */
    private java.time.LocalDateTime creationDate;
    /**
     * Поле - рост; Значение поля должно быть больше 0
     */
    private long height;
    /**
     * Поле - вес; Значение поля должно быть больше 0
     */
    private long weight;
    /**
     * Поле - id паспорта; Строка не может быть пустой, Длина строки должна быть не меньше 10,
     * Длина строки не должна быть больше 27, Поле не может быть null
     */
    private String passportID;
    /**
     * Поле - цыет волос; Поле не может быть null
     */
    private Color hairColor;
    /**
     * Поле - местоположение; Поле может быть null
     */
    private Location location;
    /**
     * Поле - координаты; Поле не может быть null
     */
    private Coordinates coordinates;

    /**
     * Метод - сеттер имени
     *
     * @param name - имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод - геттер имени
     *
     * @return String имя
     */
    public String getName() {
        return name;
    }

    /**
     * Метод - сеттер id
     */
    public void setID() {
        if (hashCode() > 0) this.id = (long) hashCode();
        else this.id = (long) hashCode() * (-17);
    }

    /**
     * Метод - геттер id
     *
     * @return Long id
     */
    public Long getID() {
        return id;
    }

    /**
     * Метод - сеттер момента создания
     */
    public void setTime() {
        creationDate = LocalDateTime.now();
    }

    /**
     * Метод - геттер момента создания
     *
     * @return LocalDateTime время
     */
    public LocalDateTime getTime() {
        return creationDate;
    }

    /**
     * Метод - геттер отфармотированного момента создания
     *
     * @return String
     */
    public String getParsedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return creationDate.format(formatter);
    }

    /**
     * Метод - сеттер роста
     *
     * @param height - рост
     */
    public void setHeight(Long height) {
        this.height = height;
    }

    /**
     * Метод - сеттер веса
     *
     * @param weight - вес
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * Метод - геттер веса
     *
     * @return long вес
     */
    public long getWeight() {
        return weight;
    }

    /**
     * Метод - сеттер паспорта
     *
     * @param passport - id паспорта
     */
    public void setPassport(String passport) {
        this.passportID = passport;
    }

    /**
     * Метод - геттер номера паспорта
     *
     * @return Long номер
     */
    public Long getPassportAsLong() {
        return Long.parseLong(passportID);
    }

    /**
     * Метод - геттер номера паспорта как строки
     *
     * @return String номер
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * Метод - сеттер цвета волос
     *
     * @param color - цвет волос
     */
    public void setHair(Color color) {
        this.hairColor = color;
    }

    /**
     * Метод - геттер цвета волос
     *
     * @return Color цвет
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     * Метод - сеттер местоположения
     *
     * @param location - цвето волос
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Метод - геттер местоположения
     *
     * @return Location место
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Метод - сеттер координат
     *
     * @param coordinates - цвето волос
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Метод - геттер координат
     *
     * @return Coordinates координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Метод - сравнение по умоланию по длине id
     *
     * @param p - объект
     * @return -1/1/0
     */
    @Override
    public int compareTo(Person p) {
        return this.name.compareTo(p.name);
    }

    /**
     * Метод - сравнение двух объектов
     *
     * @param o - объект сравнения
     * @return true если объекты равны, false иначе
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return height == person.height &&
                weight == person.weight &&
                id.equals(person.id) &&
                name.equals(person.name) &&
                creationDate.equals(person.creationDate) &&
                passportID.equals(person.passportID) &&
                hairColor == person.hairColor &&
                location.equals(person.location) &&
                coordinates.equals(person.coordinates);
    }

    /**
     * Метод - создает хэшкод объекта
     *
     * @return int хэшкож
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, creationDate, height, weight, passportID, hairColor, location, coordinates);
    }

    /**
     * Метод - представляет объект в строковом виде
     *
     * @return String строка с информацией об объекте
     */
    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + getParsedTime() +
                ", height=" + height +
                ", weight=" + weight +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                ", location=" + location +
                ", coordinates=" + coordinates +
                '}';
    }
}
