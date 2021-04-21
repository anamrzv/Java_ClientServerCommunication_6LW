package itmo.lab.other;

import itmo.lab.other.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private List<String> commandArgs;
    private Person person;

    public Message(String commandName, List<String> commandArgs){
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        person = null;
    }

    public Message(String commandName, Person person){
        this.commandName = commandName;
        this.person = person;
        commandArgs = null;
    }

}

