package dataaccess;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUserDataAccessObjectTest {

    @TempDir
    Path tempDir;

    private FileUserDataAccessObject makeDao() {
        File csv = tempDir.resolve("users.csv").toFile();
        return new FileUserDataAccessObject(csv.getAbsolutePath(), new UserFactory());
    }

    @Test
    void testSaveAndGetUser() {
        FileUserDataAccessObject dao = makeDao();
        User user = new UserFactory().create("alice", "hashedpw");
        dao.save(user);

        User retrieved = dao.get("alice");
        assertNotNull(retrieved);
        assertEquals("alice", retrieved.getName());
        assertEquals("hashedpw", retrieved.getPassword());
    }

    @Test
    void testExistsByName() {
        FileUserDataAccessObject dao = makeDao();
        assertFalse(dao.existsByName("alice"));
        dao.save(new UserFactory().create("alice", "pw"));
        assertTrue(dao.existsByName("alice"));
    }

    @Test
    void testGetReturnsNullForMissingUser() {
        assertNull(makeDao().get("nobody"));
    }

    @Test
    void testCurrentUsername() {
        FileUserDataAccessObject dao = makeDao();
        assertNull(dao.getCurrentUsername());
        dao.setCurrentUsername("bob");
        assertEquals("bob", dao.getCurrentUsername());
    }

    @Test
    void testDeleteUser() {
        FileUserDataAccessObject dao = makeDao();
        dao.save(new UserFactory().create("alice", "pw"));
        dao.deleteUser("alice");
        assertFalse(dao.existsByName("alice"));
        assertNull(dao.get("alice"));
    }

    @Test
    void testChangePassword() {
        FileUserDataAccessObject dao = makeDao();
        dao.save(new UserFactory().create("alice", "oldpw"));

        User updated = new UserFactory().create("alice", "newpw");
        dao.changePassword(updated);

        assertEquals("newpw", dao.get("alice").getPassword());
    }

    @Test
    void testPersistenceAcrossInstances() {
        File csv = tempDir.resolve("users_persist.csv").toFile();
        UserFactory factory = new UserFactory();

        FileUserDataAccessObject dao1 = new FileUserDataAccessObject(csv.getAbsolutePath(), factory);
        dao1.save(factory.create("alice", "pw"));

        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(csv.getAbsolutePath(), factory);
        assertTrue(dao2.existsByName("alice"), "Data should persist across DAO instances.");
        assertEquals("pw", dao2.get("alice").getPassword());
    }
}
