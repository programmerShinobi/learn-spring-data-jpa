package programmershinobi.belajar.springdata.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import programmershinobi.belajar.springdata.jpa.entity.Category;
import programmershinobi.belajar.springdata.jpa.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void create() {
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categoryRepository.save(category);
        }

        throw new RuntimeException("Ups rollback please");
    }

    public void test() {
        create();
    }
}
