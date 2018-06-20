package lu.uni.serval.robotframework.libraries.selenium.browserManagement;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.report.ExecutionResult;
import org.openqa.selenium.Dimension;

public class MaximizeBrowserWindow extends LibraryKeyword {
    @Override
    public void execute(Runtime runtime) {
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)Math.round(screenSize.getWidth());
        int height = (int)Math.round(screenSize.getHeight());

        //getDriver().manage().window().setSize(new Dimension(width, height));
    }
}
