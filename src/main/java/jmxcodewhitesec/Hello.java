package jmxcodewhitesec;

public class Hello implements HelloMBean {

    private String name = "MOGWAI LABS";

    // getter/setter for the "name" attribute
    public String getName() { return this.name; }
    public void setName(String newName) { this.name = newName; }

    // Methods
    public String sayHello() { return "hello: " + name; }
}
