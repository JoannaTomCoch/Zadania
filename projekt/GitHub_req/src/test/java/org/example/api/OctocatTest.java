package org.example.api;

import org.example.model.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OctocatTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.github.com";
    }

    @Test
    public void testGetUserOctocat() {
        Response response =
                given()
                        .when()
                        .get("/users/octocat")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        User user = response.getBody().as(User.class);

        // Assertions for static fields
        assertThat(user.getLogin(), equalTo("octocat"));
        assertThat(user.getId(), equalTo(583231));
        assertThat(user.getNode_id(), equalTo("MDQ6VXNlcjU4MzIzMQ=="));
        assertThat(user.getAvatar_url(), equalTo("https://avatars.githubusercontent.com/u/583231?v=4"));
        assertThat(user.getHtml_url(), equalTo("https://github.com/octocat"));
        assertThat(user.getType(), equalTo("User"));
        assertThat(user.isSite_admin(), equalTo(false));

        // Assertions for selected dynamic fields
        assertThat(user.getFollowers(), greaterThanOrEqualTo(0));
        assertThat(user.getPublic_repos(), greaterThanOrEqualTo(0));
        assertThat(user.getFollowing(), greaterThanOrEqualTo(0));

        // Validation of URLs
        String[] urls = {
                user.getUrl(),
                user.getHtml_url(),
                user.getFollowers_url(),
                user.getFollowing_url(),
                user.getGists_url(),
                user.getStarred_url(),
                user.getSubscriptions_url(),
                user.getOrganizations_url(),
                user.getRepos_url(),
                user.getEvents_url(),
                user.getReceived_events_url(),
                user.getAvatar_url(),
        };

        for (String url : urls) {
            if (url != null) {
                assertThat(url, matchesPattern("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"));
            }
        }

        // Validation of email address
        if (user.getEmail() != null) {
            assertThat(user.getEmail(), matchesPattern("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"));
        }

        // Checking if the created_at date is earlier than updated_at
        if (user.getCreated_at() != null && user.getUpdated_at() != null) {
            assertThat(user.getCreated_at(), lessThan(user.getUpdated_at()));
        }
    }
}
