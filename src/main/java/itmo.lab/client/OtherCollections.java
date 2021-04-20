package itmo.lab.client;

import itmo.lab.other.Location;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherCollections {

    /**
     * Поле - отображение объектов Location
     */
    private final Map<Integer, Location> readyLocations = new HashMap<>();

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
}
