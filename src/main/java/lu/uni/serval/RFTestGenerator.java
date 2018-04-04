package lu.uni.serval;

import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.ConsoleColors;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.apache.commons.cli.*;

import java.io.IOException;

public class RFTestGenerator {
    public static void main(String[] args) {
        try {
            Options options = new Options();

            lu.uni.serval.analytics.Cli analyticsCli = new lu.uni.serval.analytics.Cli();
            lu.uni.serval.robotframework.Cli rfCli = new lu.uni.serval.robotframework.Cli();

            rfCli.setCmdOptions(options);
            analyticsCli.setCmdOptions(options);

            options.addOption("config", true, "path to the json configuration file");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if(!cmd.hasOption("config")){
                throw new MissingArgumentException("config");
            }

            Configuration.initialize(cmd.getOptionValue("config"));
            Configuration config = Configuration.getInstance();

            rfCli.run();

            if(config.hasPlugin("analytics")){
                analyticsCli.setForest(rfCli.getForest());
                analyticsCli.run();
            }

        } catch (ParseException e) {
            String message = "argument '" + e.getMessage() + "' is missing";
            System.out.println(ConsoleColors.RED + "\t" + message + ConsoleColors.RESET);
        } catch (DuplicateNodeException e) {
            System.out.println(ConsoleColors.RED + "\t" + e.getMessage() + ConsoleColors.RESET);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
