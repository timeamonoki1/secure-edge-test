import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoDatabase;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.testng.Assert.fail;

public class DbConnectionManager {
    private static final String SSH_USER = "root";
    private static final String SSH_PASSWORD = "x#0#M6Wa0#lJ";
    private static final String SSH_HOST = "159.89.13.81";
    private static final Integer SSH_PORT = 22;

    public Connection getMySqlConnection() {
        try {
            String rhost = "localhost";
            int lport = 4321;
            int rport = 3306;

            JSch jsch = new JSch();
            Session session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setPassword(SSH_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            int assinged_port=session.setPortForwardingL(lport, rhost, rport);

            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://" + rhost +":" + lport + "/";
            String db = "sec_test";
            String dbUser = "root";
            String dbPasswd = "app1235";

            Class.forName(driver);
            return DriverManager.getConnection(url+db, dbUser, dbPasswd);
        } catch (Exception e) {
            fail("MySql DB connection failed. Error is: ", e);
        }
        return null;
    }

    public MongoClient getMongoDbConnection() {
        String LOCAL_HOST = "localhost";
        String REMOTE_HOST = "127.0.0.1";
        Integer LOCAL_PORT = 8988;
        Integer REMOTE_PORT = 27017;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setPassword(SSH_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(LOCAL_PORT, REMOTE_HOST, REMOTE_PORT);

            MongoClient mongoClient = new MongoClient(LOCAL_HOST, LOCAL_PORT);
            mongoClient.setReadPreference(ReadPreference.nearest());
            return mongoClient;
        } catch (Exception e) {
            fail("Mongo DB connection failed. Error is: ", e);
        }
        return null;
    }

    public MongoDatabase getMongoDatabase(MongoClient mongoClient, String name) {
        return mongoClient.getDatabase(name);
    }

}
