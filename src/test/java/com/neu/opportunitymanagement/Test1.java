package com.neu.opportunitymanagement;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        list.add("ok");
        System.out.println(list);
        System.out.println(list.size());

    }
}
