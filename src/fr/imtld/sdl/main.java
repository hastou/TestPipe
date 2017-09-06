package fr.imtld.sdl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        Object test = new ArrayList<String>();
        System.out.println(test instanceof List);
    }
}
