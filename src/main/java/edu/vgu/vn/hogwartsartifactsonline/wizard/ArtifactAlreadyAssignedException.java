package edu.vgu.vn.hogwartsartifactsonline.wizard;

public class ArtifactAlreadyAssignedException extends RuntimeException{
    public ArtifactAlreadyAssignedException(String name)
    {
        super("This artifact is already assigned by "+ name);
    }
}
