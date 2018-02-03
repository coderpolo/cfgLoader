package com.test.cfgLoader.entity;

import lombok.Data;

/**
 * Created by kaisheng1 on 2018/1/15.
 */

@Data
public class Command
{
    String script;

    public Command(int version, String process, String script) {
        this.version = version;
        this.process = process;
        this.script = script;
    }

    public Command()
    {
        this.version = 0;
        this.process ="";
        this.script ="";
    }
    String process;
    int version;
}
