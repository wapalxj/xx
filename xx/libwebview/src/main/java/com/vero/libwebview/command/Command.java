package com.vero.libwebview.command;

import java.util.Map;

public interface Command {
    String name();

    void execute(Map params);
}
