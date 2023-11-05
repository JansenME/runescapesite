package com.runescape.info.model.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(String m) {
        super(m);
    }
}
