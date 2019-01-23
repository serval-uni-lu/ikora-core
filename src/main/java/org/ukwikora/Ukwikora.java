package org.ukwikora;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.ukwikora.analytics.EvolutionAnalyticsCli;
import org.ukwikora.analytics.ProjectAnalyticsCli;
import org.ukwikora.utils.Configuration;
import org.ukwikora.utils.ConsoleColors;
import org.ukwikora.exception.DuplicateNodeException;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Ukwikora {
    private final static Logger logger = LogManager.getLogger(Ukwikora.class);

    public static void main(String[] args) throws Exception {
        try {
            Options options = new Options();

            options.addOption("config", true, "path to the json configuration file");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if(!cmd.hasOption("config")){
                throw new MissingArgumentException("config");
            }

            Configuration.initialize(cmd.getOptionValue("config"));
            Configuration config = Configuration.getInstance();

            initializeLogger(config);

            if(config.hasPlugin("evolution analytics")) {
                EvolutionAnalyticsCli evolutionAnalyticsCli = new EvolutionAnalyticsCli();
                evolutionAnalyticsCli.run();
            }

            if(config.hasPlugin("project analytics")){
                ProjectAnalyticsCli projectAnalyticsCli = new ProjectAnalyticsCli();
                projectAnalyticsCli.run();
            }

        } catch (ParseException e) {
            String message = "argument '" + e.getMessage() + "' is missing";
            System.out.println(ConsoleColors.RED + "\t" + message + ConsoleColors.RESET);
        } catch (DuplicateNodeException e) {
            System.out.println(ConsoleColors.RED + "\t" + e.getMessage() + ConsoleColors.RESET);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //logger.info("program finished");
    }

    private static void initializeLogger(Configuration config){
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        // configure appender to send the output
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        builder.add(console);

        // set logger level
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration log4jConfig = context.getConfiguration();
        LoggerConfig loggerConfig = log4jConfig.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(getLoggerLevel(config));

        context.updateLoggers();
    }

    private static Level getLoggerLevel(Configuration config){
        String level = config.getLoggerLevel().toUpperCase();

        Level loggerLevel;

        switch (level){
            case "ALL": loggerLevel = Level.ALL; break;
            case "TRACE": loggerLevel = Level.ALL; break;
            case "DEBUG": loggerLevel = Level.DEBUG; break;
            case "INFO": loggerLevel = Level.INFO; break;
            case "WARN": loggerLevel = Level.WARN; break;
            case "ERROR": loggerLevel = Level.ERROR; break;
            case "FATAL": loggerLevel = Level.FATAL; break;
            case "OFF": loggerLevel = Level.OFF; break;
            default: loggerLevel = Level.INFO; break;
        }

        return loggerLevel;
    }
}
