package itmo.lab.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class SpecialSave {

    private CollectionsKeeper dc;

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public SpecialSave(CollectionsKeeper dc) {
        this.dc=dc;
    }

    public boolean execute() {
        String dir = System.getenv("output");
        try (PrintWriter pw = new PrintWriter(new File(dir))) {
            LinkedList<Person> people = dc.getPeople();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String jsonString;
            for (Person p : people) {
                jsonString = gson.toJson(p);
                pw.write(jsonString + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл для записи не найден, проверьте существование переменной окружения.");
            return false;
        }
        return true;
    }
}