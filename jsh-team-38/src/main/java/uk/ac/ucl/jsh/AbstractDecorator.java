package uk.ac.ucl.jsh;

/**
 * AbstractDecorator: contains the instance of application, the function of application can be extended by its subclass
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

abstract class AbstractDecorator implements Application {

    // the application will be decorated
    Application decoratedApplication;

    // an application input as an argument will be the application ready to be decorated
    AbstractDecorator(Application decoratedApplication) {
        this.decoratedApplication = decoratedApplication;
    }
}

