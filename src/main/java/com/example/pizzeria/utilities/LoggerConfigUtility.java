package com.example.pizzeria.utilities;

public class LoggerConfigUtility {
    public static void setNewLogFilename() {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"));
        System.setProperty("logFilename", "logs/simulation_" + timestamp + ".log");
        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
        ctx.reconfigure();
    }
}
