package com.hpspells.core.extension;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ExtensionLogger extends Logger {
    private String prefix;
    
    public ExtensionLogger(Extension extension) {
        super(extension.getClass().getCanonicalName(), null);
        prefix = "[HPS:" + extension.getName() + "] ";
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(prefix + logRecord.getMessage());
        super.log(logRecord);
    }
}
