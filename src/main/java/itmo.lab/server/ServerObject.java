package itmo.lab.server;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandName;
    private String[] commandArgs;

}

