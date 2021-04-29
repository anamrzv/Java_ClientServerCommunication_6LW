package itmo.lab.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itmo.lab.other.Person;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.ServerResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class SpecialSave {

    private CollectionsKeeper collectionsKeeper;

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public SpecialSave(CollectionsKeeper dc) {
        this.collectionsKeeper =dc;
    }

    public ServerResponse execute() {
        String dir = System.getenv("output");
        if (dir==null) {
            dir = "C:/Users/Ana/Programming/lab-work-6-gradle/src/main/resources";
        }
        try (PrintWriter pw = new PrintWriter(new File(dir))) {
            LinkedList<Person> people = collectionsKeeper.getPeople();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String jsonString;
            for (Person p : people) {
                jsonString = gson.toJson(p);
                pw.write(jsonString + "\n");
            }
        } catch (FileNotFoundException e) {
            return ServerResponse.builder().error("Файл для записи не найден, проверьте существование переменной окружения. Создан файл по умолчанию.").build();
        }
        return ServerResponse.builder().message("Коллекция сохранена в файл "+dir).build();
    }
}