package programmershinobi.belajar.springdata.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import programmershinobi.belajar.springdata.jpa.entity.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insert() {
        Category category = new Category();
        category.setName("LAPTOP");

        categoryRepository.save(category);

        assertNotNull(category.getId());
    }

    @Test
    void testUpdate() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        category.setName("LAPTOP MAHAL");
        categoryRepository.save(category);

        category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);
        assertEquals("LAPTOP MAHAL", category.getName());
    }

    @Test
    void queryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("LAPTOP MAHAL").orElse(null);
        assertNotNull(category);
        assertEquals("LAPTOP MAHAL", category.getName());

        List<Category> categories = categoryRepository.findAllByNameLike("%LAPTOP%");
        assertNotNull(categories);
        assertEquals("LAPTOP MAHAL", categories.get(0).getName());
    }

    @Test
    void audit() {
        Category category = new Category();
        category.setName("Sample Audit");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }

    @Test
    void auditUpdate() {
        Category category = categoryRepository.findByNameContains("Sample Audit");
        category.setName("Sample Audit (Updated)");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }

    @Test
    void example() {
        Category category = new Category();
        category.setName("LAPTOP MAHAL");
        category.setId(1L);

        Example<Category> example = Example.of(category);

        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(1, categories.size());
        assertEquals("LAPTOP MAHAL", categories.get(0).getName());
    }

    @Test
    void exampleMatcher() {
        Category category = new Category();
        category.setName("lApTop MahAl");

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase();

        Example<Category> example = Example.of(category, matcher);

        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(1, categories.size());
        assertEquals("LAPTOP MAHAL", categories.get(0).getName());
    }

}