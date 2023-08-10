package com.arkwith.starter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok {

    private final String hello;
    private final int lombok;

    public static void main(String[] args) {

        final HelloLombok helloLombok = new HelloLombok("Hello", 1);

        System.out.println(helloLombok.getHello());
        System.out.println(helloLombok.getLombok());
    }

}
