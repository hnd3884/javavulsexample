package jmxcodewhitesec;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Server {
    public static void main(String[] args) throws Exception {
        // Create a new MBean instance from Hello (HelloMBean interface)
        Hello mbean = new Hello();

        // Create an object name,
        ObjectName mbeanName = new ObjectName("de.mogwailabs.MBeans:type=HelloMBean");

        // Connect to the MBean server of the current Java process
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(mbean, mbeanName);

        // Keep the application running until user enters something
        System.out.println("Press any key to exit");
        System.in.read();
    }
}
