package org.ukwikora.libraries.selenium.browserManagement;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class MaximizeBrowserWindow extends LibraryKeyword {
    public MaximizeBrowserWindow(){
        this.type = Type.Action;
    }

    @Override
    public void run(Runtime runtime) {
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)Math.round(screenSize.getWidth());
        int height = (int)Math.round(screenSize.getHeight());

        //getDriver().manage().window().setSize(new Dimension(width, height));
    }
}
