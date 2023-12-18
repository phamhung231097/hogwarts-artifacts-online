package edu.vgu.vn.hogwartsartifactsonline.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectname,String id)
    {
        super("Could not find " +objectname + " with Id " +id);
    }
    public ObjectNotFoundException(String objectname,int id)
    {
        super("Could not find " +objectname + " with Id " +id);
    }
}
