package itmo.lab.other;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private List<String> commandArgs;
    private Person person;

}

