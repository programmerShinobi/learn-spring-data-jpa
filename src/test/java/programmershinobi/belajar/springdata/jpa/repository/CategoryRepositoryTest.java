package programmershinobi.belajar.springdata.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}