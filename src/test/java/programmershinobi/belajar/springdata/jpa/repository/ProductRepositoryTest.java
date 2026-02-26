package programmershinobi.belajar.springdata.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Test
    void sort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("LAPTOP MAHAL", sort);
        assertEquals(2, products.size());
        assertEquals("DELL", products.get(1).getName());
        assertEquals("ADVAN", products.get(0).getName());
    }

    @Test
    void pageable() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findAllByCategory_Name("LAPTOP MAHAL", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("ADVAN", products.getContent().get(0).getName());

        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("LAPTOP MAHAL", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("DELL", products.getContent().get(0).getName());

    }

    @Test
    void count() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("LAPTOP MAHAL");
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("NOTHING");
        assertEquals(0L, count);
    }

    @Test
    void existsByName() {
        boolean exists = productRepository.existsByName("ADVAN");
        assertTrue(exists);

        exists = productRepository.existsByName("NOTHING");
        assertFalse(exists);
    }
}