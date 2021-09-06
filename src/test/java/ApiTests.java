import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testng.Assert.*;


public class ApiTests extends BaseApiTest {


    @Test(description = "Verify that the same users are returned by the api as there are set in DB - positive test")
    public void testGetUsers() {
        List<User> usersFromApi = getAllUsersFromApi(HttpMethod.GET);
        List<User> usersFromDb = getAllUsersFromDb();

        assertEquals(usersFromApi.size(), usersFromDb.size(), "There are different number of users returned by the API as there are in the DB");
        for (int i = 0; i < usersFromDb.size(); i++) {
            assertEquals(usersFromApi.get(i), usersFromDb.get(i), "User does not match");
        }
    }

    @Test
    public void testGetUsers2() {
        try {
            List<User> usersFromApi = getAllUsersFromApi(HttpMethod.GET);
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 400, "Wrong status code");
            assertTrue(e.getMessage().contains("Required request body is missing"));
        }
    }

    @Test(description = "Verify that the same user by id is returned by the api as there is set in DB - positive test")
    public void testGetUserById() {
        List<User> usersFromDb = getAllUsersFromDb();
        for (int i = 0; i < usersFromDb.size(); i++) {
            User userFromDb = usersFromDb.get(i);
            User userFromApi = getUserByIdFromApi(userFromDb.getId());
            assertEquals(userFromApi, userFromDb, "User does not match");
        }
    }

    @Test()
    public void testGetUserByApi2() {
        List<User> usersFromDb = getAllUsersFromDb();
        String lastId = usersFromDb.get(usersFromDb.size() - 1).getId();
        try {
            User userFromApi = getUserByIdFromApi(lastId + 1);
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 404, "Wrong status code");
            assertEquals(e.getMessage(), "404 null");
        }
    }

    @Test
    public void testPostUsers() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("id", "1");
        requestBody.add("username", "vladsdasd");
        requestBody.add("email", "vladsdasd@asdsad.com");
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody);
        User userAfterPost = postUserToApi(httpEntity);
        assertNotNull(userAfterPost);
        List<User> allUsersFromDb = getAllUsersFromDb();
        assertTrue(allUsersFromDb.contains(userAfterPost), "Post was unsuccessful");
    }

    @Test
    public void testPostUsers2() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("email", "test@test.com");
        try {
            User userAfterPost = postUserToApi(new HttpEntity(requestBody));
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 400, "Wrong status code");
        }
    }

    @Test
    public void testPutUserToApi() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("email", "test@test.com");
        try {
            putUserToApiPost(new HttpEntity(requestBody));
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 405, "Wrong status code");
        }
    }

    @Test
    public void testUserDelete() {
        List<User> allUsersFromDb = getAllUsersFromDb();
        assertFalse(allUsersFromDb.isEmpty(), "No users in DB");
        User firstUserFromDb = allUsersFromDb.get(0);
        User deletedUser = deleteUserFromApi(firstUserFromDb.getId());
        assertFalse(getAllUsersFromDb().stream()
                .map(User::getId)
                .collect(Collectors.toList()).contains(firstUserFromDb.getId()), "Delete was unsuccesful");

    }

    private MultiValueMap toMultiValueMap(Object object) {
        return toMultiValueMap(getObjectMapper().convertValue(object, Map.class));
    }
}
