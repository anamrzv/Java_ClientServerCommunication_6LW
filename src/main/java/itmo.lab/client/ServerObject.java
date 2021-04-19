package itmo.lab.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private String[] commandArgs;

}
