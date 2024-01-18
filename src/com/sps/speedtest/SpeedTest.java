package com.sps.speedtest;

public abstract class SpeedTest {
    private String testName;
    public SpeedTest(String testName) {
        this.testName = testName;
    }

    private void testDeserialization() {
        long startTime, myTime, jaxbTime;
        startTime = System.nanoTime();
        myLibDeserializationTest();
        myTime = System.nanoTime() - startTime;
        startTime = System.nanoTime();
        anotherLibDeserializationTest();
        jaxbTime = System.nanoTime() - startTime;

        System.out.printf("    My Lib Deserialization Time: %.2fms | " +
                        "Jaxb Deserialization Time: %.2fms. Result: ",
                ((float) myTime) / 1_000_000,
                ((float) jaxbTime) / 1_000_000);
        if (jaxbTime >= myTime) {
            System.out.printf("My Lib %.2f times faster in deserialization;\n",
                    ((float) jaxbTime) / myTime);
        } else {
            System.out.printf("My Lib %.2f times slower in deserialization;\n",
                    ((float) myTime) / (jaxbTime));
        }
    }

    private void testSerialization() {
        long startTime, myTime, jaxbTime;
        startTime = System.nanoTime();
        myLibSerializationTest();
        myTime = System.nanoTime() - startTime;
        startTime = System.nanoTime();
        anotherLibSerializationTest();
        jaxbTime = System.nanoTime() - startTime;

        System.out.printf("    My Lib Serialization Time: %.2fms | " +
                        "Jaxb Serialization Time: %.2fms. Result: ",
                ((float) myTime) / 1_000_000,
                ((float) jaxbTime) / 1_000_000);
        if (jaxbTime >= myTime) {
            System.out.printf("My Lib %.2f times faster in serialization;\n",
                    ((float) jaxbTime) / myTime);
        } else {
            System.out.printf("My Lib %.2f times slower in serialization;\n",
                    ((float) myTime) / (jaxbTime));
        }
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
