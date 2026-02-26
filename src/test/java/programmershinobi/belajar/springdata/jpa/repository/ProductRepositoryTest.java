package programmershinobi.belajar.springdata.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import programmershinobi.belajar.springdata.jpa.entity.Category;
import programmershinobi.belajar.springdata.jpa.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProducts() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("DELL");
            product.setPrice(25_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("ADVAN");
            product.setPrice(10_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Test
    void findByCategoryName() {
        List<Product> products = productRepository.findAllByCategory_Name("LAPTOP MAHAL");
        assertEquals(2, products.size());
        assertEquals("DELL", products.get(0).getName());
        assertEquals("ADVAN", products.get(1).getName());
    }

}