package programmershinobi.belajar.springdata.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.support.TransactionOperations;
import programmershinobi.belajar.springdata.jpa.entity.Category;
import programmershinobi.belajar.springdata.jpa.entity.Product;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionOperations transactionOperations;

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

    @Test
    void deleteModeRolBack() {
        transactionOperations.executeWithoutResult(transactionStatus -> { // transaction 1
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("LENOVO");
            product.setPrice(5_000_000L);
            product.setCategory(category);
            productRepository.save(product); // transaction 1

            int delete = productRepository.deleteByName("LENOVO"); // transaction 1
            assertEquals(1, delete);

            delete = productRepository.deleteByName("NOTHING"); // transaction 1
            assertEquals(0, delete);

        });
    }

    @Test
    void deleteModeUnrolBack() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("LENOVO");
        product.setPrice(5_000_000L);
        product.setCategory(category);
        productRepository.save(product); // transaction 1

        int delete = productRepository.deleteByName("LENOVO"); // transaction 2
        assertEquals(1, delete);

        delete = productRepository.deleteByName("NOTHING"); // transaction 3
        assertEquals(0, delete);

    }

    @Test
    void namedQuery() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Product> products = productRepository.searchProductUsingName("DELL", pageable);

        assertEquals(1, products.size());
        assertEquals("DELL", products.get(0).getName());
    }

    @Test
    void searchProducts() {
        Pageable pageable = PageRequest.of(0, 2,  Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.searchProduct("%D%", pageable);
        assertEquals(2, products.getContent().size());
        assertEquals(2, products.getTotalElements());
        assertEquals(1, products.getTotalPages());

        products = productRepository.searchProduct("DELL", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(1, products.getTotalElements());
        assertEquals(1, products.getTotalPages());

        products = productRepository.searchProduct("ADVAN", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(1, products.getTotalElements());
        assertEquals(1, products.getTotalPages());

        products = productRepository.searchProduct("NOTHING", pageable);
        assertEquals(0, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(0, products.getTotalElements());
        assertEquals(0, products.getTotalPages());
    }

    @Test
    void modifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            int total = productRepository.deleteProductUsingName("Wrong");
            assertEquals(0, total);

            total = productRepository.updateProductPriceToZero(1L);
            assertEquals(1, total);

            Product product = productRepository.findById(1L).orElse(null);
            assertNotNull(product);
            assertEquals(0L, product.getPrice());
        });
    }

    @Test
    void stream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Stream<Product> stream = productRepository.streamAllByCategory(category);
            stream.forEach(product -> System.out.println(product.getId() + " : " + product.getName()));
        });
    }

    @Test
    void slice() {
        Pageable firstPage = PageRequest.of(0, 1);

        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, firstPage);
        // do with content
        while (slice.hasNext()) {
            slice = productRepository.findAllByCategory(category, slice.nextPageable());
            // do with content
        }
    }
}