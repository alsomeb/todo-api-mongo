package com.alsomeb.todoapimongo;
// https://jschmitz.dev/posts/how_to_test_the_data_layer_of_your_spring_boot_application_with_dataMongotest/
// https://jschmitz.dev/posts/how_to_test_the_data_layer_of_your_spring_boot_application_an_overview/

/*
Your application works with documents and persists them in a MongoDB? Spring Boot has you covered!
Test your MongoDB components by annotating your test with @DataMongoTest.
Those tests will use an embedded in-memory MongoDB process by default if it is available.
Otherwise, the tests will use the configured MongoDB.

DVS DEM LÄGGS EJ IN PÅ RIKTIGT, IN MEMORY MONGODB DÄR DET GÅR
ANNARS ANVÄNDS KONFIGURERAD MONGODB!
 */

import com.alsomeb.todoapimongo.model.Todo;
import com.alsomeb.todoapimongo.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataMongoTest
public class TodoTests {

    private MongoTemplate mongoTemplate;
    private TodoRepository todoRepository;

    @Autowired
    public TodoTests(MongoTemplate mongoTemplate, TodoRepository todoRepository) {
        this.mongoTemplate = mongoTemplate;
        this.todoRepository = todoRepository;
    }
    /*
    Regardless of what kind of automated test we write,
    there is always one task we have to do before executing the thing we want to test  – put the system into a known state.
    Often this phase in the lifecycle of a test is called Setup- or Bootstrapping phase.
    In a unit test, this is done by instantiating some objects.
    In the kind of integration test, we are going to write, it also means putting some test data into our database.
    Luckily for us, Spring Boot supports some different ways to do this.
     */

    /*
    Spring Boot will configure a MongoTemplate for tests with the @DataMongoTest annotation.
    This means we can use it to bootstrap the data required for our test.

    We can also use the MongoRepositories of our application to bootstrap the required test data:
    -- mongoTemplate.save(todo);
    This approach has the disadvantage that we rely on the functionality of the comp
    we want to test.
    Only use it if we already know save method works as expected
     */

    // We have MongoDB Atlas 5.0.14
    // It only worked with 5.0.0 in application.properties!

    @Test
    void bootstrapTestDataWithMongoTemplate() {
        Todo todo = new Todo("test", "test");
        Todo todo2 = new Todo("test2", "test2");
        Todo todo3 = new Todo("test3", "test3");

        mongoTemplate.insert(todo);
        mongoTemplate.insert(todo2);
        mongoTemplate.insert(todo3);

        List<Todo> todosList = todoRepository.findAll();

        assertEquals(3, todosList.size());
        assertEquals("test", todosList.get(0).getDesc());
        assertEquals("test2", todosList.get(1).getDesc());
        assertEquals("test3", todosList.get(2).getDesc());
    }

    @Test
    void testTodoObjectCreationIsCorrect() {
        // Given
        Todo todo = new Todo("testAlex", "testCat");
        LocalDate expectedDate = todo.getDateAdded();
        mongoTemplate.insert(todo);

        // when
        Todo testTodo = todoRepository.findAll().get(0);

        // then
        assertEquals("testAlex", testTodo.getDesc());
        assertEquals("testCat", testTodo.getCategory());
        assertNotNull(testTodo.getId());
        assertEquals(expectedDate, testTodo.getDateAdded());
    }

    @Test
    void testTodoObjectCreationIsIncorrect() {
        // Given
        Todo todo = new Todo("testAlex", "testCat");
        mongoTemplate.insert(todo);

        // when
        Todo testTodo = todoRepository.findAll().get(0);

        // then
        assertNotEquals("testNotCorrect", testTodo.getDesc());
        assertNotEquals("testNotCorrect", testTodo.getCategory());
        assertNotEquals(LocalDate.of(2023, Month.JANUARY, 15), testTodo.getDateAdded());
    }

    @Test // with AssertJ Library https://assertj.github.io/doc/
    void itShouldAddTodoToCollection() {
        // given
        String desc = "clean house";
        String category = "house";
        Todo todo = new Todo(desc, category);
        mongoTemplate.insert(todo);

        // when
        List<Todo> expectedList = todoRepository.findTodoByCategory(category);
        Todo expectedTodo = expectedList.get(0);

        // then
        System.out.println(expectedList);
        assertThat(expectedList.size()).isEqualTo(1);
        assertThat(expectedTodo.getDesc()).isEqualToIgnoringCase("clean house");
        assertThat(expectedTodo.getCategory()).isEqualToIgnoringCase("house");
        assertThat(expectedTodo.getId()).isNotNull();
        assertThat(expectedTodo.getDateAdded()).isNotNull();
        assertThat(expectedTodo.getLatestUpdated()).isNotNull();
        assertThat(expectedList).isNotEmpty();
        assertThat(expectedList).isNotNull();
    }

    @Test // with AssertJ Library
    void itShouldDeleteTodoFromCollection() {
        // GIVEN
        String desc = "123testdesc";
        String category = "123testcategory";
        Todo todo = new Todo(desc, category);
        mongoTemplate.insert(todo);

        // Eftersom mongodb skapar ID åt oss så måste vi hitta by desc
        // Men vi har bara en Todo som heter "123 test" och vi drop DB efter varje test
        // WHEN
        Todo expectedTodo = todoRepository.findTodoByCategory(category).get(0);
        todoRepository.delete(expectedTodo);

        //THEN
        assertThat(todoRepository.findTodoByCategory(category))
                .isEmpty();
        assertThat(todoRepository.findTodoByCategory(category))
                .hasSize(0);
    }


    // !!--- Test TEAR DOWN ---!!
    // When writing any kind of automated test, we need to make sure that all tests are independent of one another.
    // If one test writes data into the database, it could potentially break the next test which does not expect any existing data.
    // Therefore, we need to delete the data that is created by our tests.
    // Unlike the @DataJpaTest, the @DataMongoTest we are using here is not transactional.
    // This means the data persisted within a test will not be automatically deleted.
    // The easiest way to do this that I found is to drop the database after each test.
    // We could do this explicitly in each test or once in a method that is annotated with JUnit’s @AfterEach.
    @AfterEach
    void cleanUpDB() {
        mongoTemplate.getDb().drop();
    }
}
