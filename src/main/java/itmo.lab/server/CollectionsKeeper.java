package itmo.lab.server;

import itmo.lab.other.Location;
import itmo.lab.other.Person;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectionsKeeper {

    /**
     * Поле - связный список объектов Person
     */
    private final LinkedList<Person> people = new LinkedList<>();


    /**
     * Поле - отображение объектов Location
     */
    private final Map<Integer, Location> readyLocations = new HashMap<>();

    /**
     * Поле - последний добавленный в коллекцию объект
     */
    private int lastPersonNum;

    /**
     * Поле - TreeSet объектов String для обработки рекурсии в execute_script
     */
    private final Set<String> scriptNames = new TreeSet<>();

    /**
     * Метод - обновление поля последнего добавленного объекта коллекции
     */
    public void setLastPersonNum(int number) {
        lastPersonNum = number;
    }

    /**
     * Метод - геттер индекса последнего добавленного объекта коллекции
     *
     * @return int индекс
     */
    public int getLastPersonNum() {
        return lastPersonNum;
    }

    /**
     * Метод - геттер коллекции людей
     *
     * @return LinkedList<Person> коллекция
     */
    public LinkedList<Person> getPeople() {
        return people;
    }

    /**
     * Метод- добавляет локацию в коллекцию
     */
    public void addLocation(Location l) {
        readyLocations.put(readyLocations.size() + 1, l);
    }

    /**
     * Метод - геттер коллекции локаций
     *
     * @return Map<Integer, Location> коллекция
     */
    public Map<Integer, Location> getLocations() {
        return readyLocations;
    }

    /**
     * Метод - валидация на отсутствие цифр и спец символов в строке
     *
     * @return boolean true/false
     */
    public boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[\\p{L} ]+$");
        Matcher m = pattern.matcher(name);
        return m.matches();
    }

    /**
     * Метод - валидация на отсутствие букв в строке
     *
     * @return boolean true/false
     */
    public boolean validatePassport(String pass) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher m = pattern.matcher(pass);
        return m.matches();
    }

    public Set<String> getScriptNames(){
        return scriptNames;
    }

    public boolean addScriptName(String name){
        return scriptNames.add(name);
    }

    public void clearScriptNames(){
        scriptNames.clear();
    }
}
