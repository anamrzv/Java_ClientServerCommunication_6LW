package itmo.lab.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import itmo.lab.other.Location;
import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;

/**
 * Команда обновляет значения полей объекта коллекции с заданным id
 */
public class Update extends Command {

    /**
     * Поле - логическая переменная, найден ли такой объект
     */
    private boolean isFound = false;
    /**
     * Поле - объект Person, поля которого будут обновлены
     */
    private Person updatePers;

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Update(CollectionsKeeper dc) {
        super(dc);
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    @Override
    public boolean execute(String... args) {
        Map<Integer, Location> readyLocations = dc.getLocations();
        LinkedList<Person> people = dc.getPeople();
        if (args == null || args.length != 2)
            System.out.println("У команды update должно быть два аргумента - необходимое id и слово Person/строка формата json.");
        else {
            long id = Long.parseLong(args[0]);

            for (Person p : people) {
                if (p.getID() == id) {
                    updatePers = p;
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                System.out.println("Человека с таким id не существует");
                return false;
            } else if (!args[1].equalsIgnoreCase("person")) {
                try {
                    String jsonLine = args[1];
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    Person pers = gson.fromJson(jsonLine, Person.class);
                    if (pers.getName() == null || pers.getPassportID() == null || pers.getHairColor() == null || pers.getLocation() == null || pers.getCoordinates() == null) {
                        System.out.println("Проверьте, что заполнены все обязательны поля: name, passport id, hair color, location, coordinates, а в поле hair color правильно указан цвет.");
                        return false;
                    } else if (!dc.validateName(pers.getName())) {
                        System.out.println("В имени не могут содержаться цифры и спец. знаки");
                        return false;
                    } else if (!dc.validatePassport(pers.getPassportID())) {
                        System.out.println("В passport id должны содержаться только цифры.");
                        return false;
                    } else if (pers.getPassportID().length() < 10 || pers.getPassportID().length() > 27) {
                        System.out.println("Passport id должен содержать от 10 до 27 цифр, проверьте длину.");
                        return false;
                    } else {
                        if (!updatePers.getName().equals(pers.getName())) updatePers.setName(pers.getName());
                        if (updatePers.getHeight() != pers.getHeight()) updatePers.setHeight(pers.getHeight());
                        if (updatePers.getWeight() != pers.getWeight()) updatePers.setWeight(pers.getWeight());
                        if (!updatePers.getPassportID().equals(pers.getPassportID()))
                            updatePers.setPassport(pers.getPassportID());
                        if (!updatePers.getHairColor().equals(pers.getHairColor()))
                            updatePers.setHair(pers.getHairColor());
                        if (!updatePers.getLocation().equals(pers.getLocation()))
                            updatePers.setLocation(pers.getLocation());
                        if (!updatePers.getCoordinates().equals(pers.getCoordinates()))
                            updatePers.setCoordinates(pers.getCoordinates());
                        System.out.println("Данные о персоне с id " + id + " успешно обновлены");
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("Проверьте формат ввода строки json");
                    return false;
                }
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                do {
                    try {
                        System.out.println("Выберите, какие поля вы хотите обновить(Введите число):\n1. Nмя\n2.Рост\n3.Вес\n4.ID паспорта\n5.Цвет волос\n6.Локация\n7.Координаты\nNли нажмите enter чтобы закончить изменения");
                        System.out.print(">");
                        CreatePerson sa = new CreatePerson(dc);
                        String i = br.readLine().trim();
                        if (!i.isEmpty()) {
                            int choice = Integer.parseInt(i);
                            if (choice > 0 && choice < 8) {
                                switch (choice) {
                                    case (1):
                                        System.out.println("Введите новое имя персоны (не пустая строка).");
                                        sa.setInputName(br, updatePers);
                                        //updatePers.setName(br);
                                        break;
                                    case (2):
                                        System.out.println("Введите новый рост. Он должен быть больше 0. Если хотите сбросить рост, нажмите enter.");
                                        sa.setInputHeight(br, updatePers);
                                        break;
                                    case (3):
                                        System.out.println("Введите новый вес. Он должен быть больше 0. Если хотите сбросить вес, нажмите enter.");
                                        sa.setInputWeight(br, updatePers);
                                        break;
                                    case (4):
                                        System.out.println("Введите новый ID паспорта. Длина ID должна лежать в диапазоне [10;27].");
                                        sa.setInputPassport(br, updatePers);
                                        break;
                                    case (5):
                                        System.out.println("Выберите новый цвет волос.");
                                        sa.setInputHairColor(br, updatePers);
                                        break;
                                    case (6):
                                        System.out.println("Выберите новое местоположение персоны.");
                                        sa.setInputLocation(br, updatePers, readyLocations);
                                        break;
                                    case (7):
                                        System.out.println("Введите новые координаты персоны.");
                                        sa.setInputCoordinates(br, updatePers);
                                        break;
                                }
                            } else System.out.println("Введите число от 1 до 7 или enter, чтобы окончить изменения.");
                        } else break;
                    } catch (Exception e) {
                        System.out.println("Неверный формат ввода. Введите число или enter, чтобы окончить изменения.");
                    }
                } while (true);
            }
        }

        return false;
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
