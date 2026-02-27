package programmershinobi.belajar.springdata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programmershinobi.belajar.springdata.jpa.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // WHERE name = ?
    Optional<Category> findFirstByNameEquals(String name);

    // WHERE name like ?
    List<Category> findAllByNameLike(String name);

    // WHERE name = ?
    Category findByNameContains(String name);
}
