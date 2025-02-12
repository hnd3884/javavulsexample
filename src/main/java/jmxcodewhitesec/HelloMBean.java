package jmxcodewhitesec;

public interface HelloMBean {
    // getter and setter for the attribute "name"
    public String getName();
    public void setName(String newName);

    // Bean method "sayHello"
    public String sayHello();
}
