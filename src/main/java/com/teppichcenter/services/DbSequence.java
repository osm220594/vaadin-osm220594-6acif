package com.teppichcenter.services;

public class DbSequence {
    private static Long idCounter = 1L;

    private DbSequence() {}

    public static Long nextVal() {
        return DbSequence.idCounter++;
    }
}
