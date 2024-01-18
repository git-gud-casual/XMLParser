package com.sps.speedtest;

import java.nio.file.Path;

public abstract class SpeedTest {
    private String testName;
    public SpeedTest(String testName) {
        this.testName = testName;
    }

    private void testDeserialization() {
        long startTime1, endTime1, startTime2, endTime2;
        startTime1 = System.nanoTime();
        myLibDeserializationTest();
        endTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        anotherLibDeserializationTest();
        endTime2 = System.nanoTime();

        System.out.printf("    My Lib Deserialization Time: %.2fms | " +
                        "Jaxb Deserialization Time: %.2fms. " +
                        "Result: My Lib %.2f times faster in deserialization;\n",
                ((float) endTime1 - startTime1) / 1_000_000,
                ((float) endTime2 - startTime2) / 1_000_000,
                ((float) endTime2 - startTime2) / (endTime1 - startTime1));
    }

    private void testSerialization() {
        long startTime1, endTime1, startTime2, endTime2;
        startTime1 = System.nanoTime();
        myLibSerializationTest();
        endTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        anotherLibSerializationTest();
        endTime2 = System.nanoTime();

        System.out.printf("    My Lib Serialization Time: %.2fms | " +
                        "Jaxb Serialization Time: %.2fms. Result: " +
                        "My Lib %.2f times faster in serialization;\n",
                ((float) endTime1 - startTime1) / 1_000_000,
                ((float) endTime2 - startTime2) / 1_000_000,
                ((float) endTime2 - startTime2) / (endTime1 - startTime1));
    }

    public void test() {
        System.out.println("Case " + testName + " [");
        testDeserialization();
        testSerialization();
        System.out.println("]");
    }

    abstract void myLibDeserializationTest();
    abstract void anotherLibDeserializationTest();
    abstract void myLibSerializationTest();
    abstract void anotherLibSerializationTest();

}
