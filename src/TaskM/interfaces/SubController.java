package TaskM.interfaces;

import TaskM.controller.baseController;

/**
 * @author Justin Haupt
 *
 * The interface for all sub-controllers. Only used for a method
 * that provides a reference to the base controller.
 */

public interface SubController {

    // Inject a reference of the base controller into a sub-controller
    void setSceneParent(baseController mainController);
}
