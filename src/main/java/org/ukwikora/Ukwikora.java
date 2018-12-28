package org.ukwikora;

import org.ukwikora.analytics.GitAnalyticsCli;
import org.ukwikora.analytics.ProjectAnalyticsCli;
import org.ukwikora.utils.Configuration;
import org.ukwikora.utils.ConsoleColors;
import org.ukwikora.exception.DuplicateNodeException;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Ukwikora {
    final static Logger logger = Logger.getLogger(Ukwikora.class);

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

            if(config.hasPlugin("git analytics")) {
                GitAnalyticsCli gitAnalyticsCli = new GitAnalyticsCli();
                gitAnalyticsCli.run();
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

        logger.info("program finished");
    }
}
