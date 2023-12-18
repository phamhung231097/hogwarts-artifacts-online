package edu.vgu.vn.hogwartsartifactsonline.exception;

public class WizardNameCannotBeNullException extends RuntimeException {
    public WizardNameCannotBeNullException(){
        super("Provided arguments are invalid, see data for details.");
    }
}
