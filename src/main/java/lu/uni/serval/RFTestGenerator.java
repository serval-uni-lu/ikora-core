package lu.uni.serval;

import lu.uni.serval.utils.ConsoleColors;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class RFTestGenerator {
    public static void main(String[] args) {
        try {
            Options options = new Options();

            lu.uni.serval.analytics.Cli analyticsCli = new lu.uni.serval.analytics.Cli();
            lu.uni.serval.robotframework.Cli rfCli = new lu.uni.serval.robotframework.Cli();

            rfCli.setCmdOptions(options);
            analyticsCli.setCmdOptions(options);

            options.addOption("verbose", "be extra verbose");
            options.addOption("analytics", false, "run analytics");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            rfCli.run(cmd);

            if(options.hasOption("analytics")){
                analyticsCli.setForest(rfCli.getForest());
                analyticsCli.run(cmd);
            }

        } catch (ParseException e) {
            String message = "argument '" + e.getMessage() + "' is missing";
            System.out.println(ConsoleColors.RED + "\t" + message + ConsoleColors.RESET);
        }
    }
}
