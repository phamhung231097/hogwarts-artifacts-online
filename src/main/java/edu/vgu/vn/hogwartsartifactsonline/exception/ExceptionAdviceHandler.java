package edu.vgu.vn.hogwartsartifactsonline.exception;

import edu.vgu.vn.hogwartsartifactsonline.system.Result;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import edu.vgu.vn.hogwartsartifactsonline.wizard.ArtifactAlreadyAssignedException;
import edu.vgu.vn.hogwartsartifactsonline.wizard.Wizard;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(ObjectNotFoundException exception)
    {
        return new Result(false, StatusCode.NOT_FOUND,exception.getMessage());
    }
    @ExceptionHandler(WizardNameCannotBeNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleWizardNameCannotBeNullException(WizardNameCannotBeNullException exception)
    {
        Wizard wizardNameException = new Wizard();
        wizardNameException.setName("name is required.");
        return new Result(false,StatusCode.INVALID_ARGUMENT, exception.getMessage(), wizardNameException);
    }
    @ExceptionHandler(ArtifactAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleArtifactAlreadyAssignedException(ArtifactAlreadyAssignedException exception)
    {
        return new Result(false,StatusCode.INVALID_ARGUMENT,exception.getMessage());
    }
}
