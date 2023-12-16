package edu.vgu.vn.hogwartsartifactsonline.system.exception;

import edu.vgu.vn.hogwartsartifactsonline.artifact.ArtifactNotFoundException;
import edu.vgu.vn.hogwartsartifactsonline.system.Result;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceHandler {
    @ExceptionHandler(ArtifactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(ArtifactNotFoundException exception)
    {
        return new Result(false, StatusCode.NOT_FOUND,exception.getMessage());
    }
}
