package programmershinobi.belajar.springdata.jpa.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import programmershinobi.belajar.springdata.jpa.entity.Category;
import programmershinobi.belajar.springdata.jpa.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdEquals(Long id);

    Slice<Product> findAllByCategory(Category category, Pageable pageable);

    Stream<Product> streamAllByCategory(Category category);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.name = :name")
    int deleteProductUsingName(@Param("name") String name);

    @Modifying
    @Query("UPDATE Product p SET p.price = 0 WHERE p.id = :id")
    int updateProductPriceToZero(@Param("id") Long id);

    @Query(
            value = "SELECT p FROM Product p WHERE p.name LIKE :name or p.category.name LIKE :name",
            countQuery = "SELECT COUNT(p) FROM Product p WHERE p.name LIKE :name or p.category.name LIKE :name"
    )
    Page<Product> searchProduct(@Param("name") String name, Pageable pageable);

    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);

    @Transactional
    int deleteByName(String name);

    boolean existsByName(String name);

    Long countByCategory_Name(String name);

    List<Product> findAllByCategory_Name(String name);

    List<Product> findAllByCategory_Name(String name, Sort sort);

    Page<Product> findAllByCategory_Name(String name, Pageable pageable);
}
