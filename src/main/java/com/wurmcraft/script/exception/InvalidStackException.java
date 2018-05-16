package com.wurmcraft.script.exception;

import com.wurmcraft.api.EnumInputType;

import java.security.InvalidParameterException;

/**
 Thrown when a invalid Script Object is found

 @see com.wurmcraft.script.utils.SupportHelper#checkNotNull(EnumInputType,Object,String) */
public class InvalidStackException extends InvalidParameterException {

    public InvalidStackException (String msg) {
        super (msg);
    }
}
