package me.itzisonn_.cmv.lang.main;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class FinishableArrayList<T> extends ArrayList<T> {
    private boolean isFinished = false;

    public void finish() {
        isFinished = true;
    }
}