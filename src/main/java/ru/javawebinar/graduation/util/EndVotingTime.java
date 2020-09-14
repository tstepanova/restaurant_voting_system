package ru.javawebinar.graduation.util;

import java.time.LocalTime;

public class EndVotingTime {

    private static LocalTime time;

    public static LocalTime getTime() {
        return time;
    }

    private EndVotingTime() {
        time = LocalTime.of(11, 00);
    }

    public static void setTime(LocalTime time) {
        EndVotingTime.time = time;
    }

    @Override
    public String toString() {
        return getTime().toString();
    }
}
