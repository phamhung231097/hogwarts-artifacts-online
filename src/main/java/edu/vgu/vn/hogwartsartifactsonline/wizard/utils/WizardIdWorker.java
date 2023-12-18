package edu.vgu.vn.hogwartsartifactsonline.wizard.utils;

public class WizardIdWorker {
    public WizardIdWorker() {
    }

    private static Integer currentId = 3;
    public Integer nextId()
    {
        currentId++;
        Integer newId = currentId;
        return newId;
    }
}
