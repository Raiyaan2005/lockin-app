package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUserStoresNameAndPassword() {
        User user = new User("alice", "secret");
        assertEquals("alice", user.getName());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void testEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "pw"));
    }

    @Test
    void testEmptyPasswordThrows() {
        assertThrows(IllegalArgumentException.class, () -> new User("alice", ""));
    }

    @Test
    void testUserFactoryCreatesUser() {
        User user = new UserFactory().create("bob", "hash");
        assertEquals("bob", user.getName());
        assertEquals("hash", user.getPassword());
    }
}
