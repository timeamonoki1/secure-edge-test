import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.fail;

public class BaseApiTest {

    private RestTemplate restTemplate;
    private DbConnectionManager dbConnectionManager = new DbConnectionManager();
    private Connection mySqlconnection;
    private ObjectMapper objectMapper;
    private MongoClient mongoClient;

    @BeforeClass
    public void beforeSuite() {
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        converter.setObjectMapper(objectMapper);
        restTemplate.getMessageConverters().add(0, converter);

        mySqlconnection = dbConnectionManager.getMySqlConnection();
        mongoClient = dbConnectionManager.getMongoDbConnection();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @AfterClass(alwaysRun = true)
    public void afterSuite() throws SQLException {
        mySqlconnection.close();
        mongoClient.close();
    }

    protected MongoDatabase getMongoDatabase() {
        return dbConnectionManager.getMongoDatabase(mongoClient, "sec_test");
    }

    protected List<User> getAllUsersFromApi(HttpMethod method) {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange("http://159.89.13.81:8075/users", method, new HttpEntity(null), new ParameterizedTypeReference<List<User>>() {
        });
        return responseEntity.getBody();
    }

    protected User getUserByIdFromApi(String id) {
        ResponseEntity<User> responseEntity = restTemplate.exchange("http://159.89.13.81:8075/users/" + id, HttpMethod.GET, new HttpEntity(null), User.class);
        return responseEntity.getBody();
    }

    protected User postUserToApi(HttpEntity httpEntity) {
        ResponseEntity<User> responseEntity = restTemplate.exchange("http://159.89.13.81:8075/users", HttpMethod.POST, httpEntity, User.class);
        return responseEntity.getBody();
    }

    protected void putUserToApi(HttpEntity httpEntity) {
        restTemplate.put("http://159.89.13.81:8075/users/1", httpEntity);
    }

    protected void putUserToApiPost(HttpEntity httpEntity) {
        restTemplate.exchange("http://159.89.13.81:8075/users/1", HttpMethod.POST, httpEntity, User.class);
    }

    protected User deleteUserFromApi(String userId) {
        ResponseEntity<User> responseEntity = restTemplate.exchange("http://159.89.13.81:8075/users/" + userId, HttpMethod.DELETE, new HttpEntity(null), User.class);
        return responseEntity.getBody();
    }


    protected List<User> getAllUsersFromDb() {
        List<User> users = new ArrayList<User>();
        try {
            Statement st = mySqlconnection.createStatement();
            String sql = "Select * from user";
            ResultSet resultSet = st.executeQuery(sql);

            while(resultSet.next()) {
                users.add(convertResultSetToUser(resultSet));
            }
            return users;
        } catch (Exception e) {
            fail("Failed to get all users from DB, Excpetion is: ", e);
        }
        return users;
    }

    private User convertResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
}
