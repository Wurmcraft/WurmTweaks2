package com.wurmcraft.script.exception;

import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.script.utils.SupportBase;

import java.security.InvalidParameterException;

/**
 Thrown when a invalid Script Object is found

 @see SupportBase#checkNotNull(EnumInputType,Object,String) */
public class InvalidStackException extends InvalidParameterException {

    public InvalidStackException (String msg) {
        super (msg);
    }
}
