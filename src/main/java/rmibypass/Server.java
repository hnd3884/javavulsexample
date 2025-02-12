package rmibypass;

import java.lang.reflect.InvocationTargetException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(2222);
        User user = new UserImpl();
        registry.rebind("HelloRegistry", user);
        System.out.println("rmi start at 2222");
    }
}