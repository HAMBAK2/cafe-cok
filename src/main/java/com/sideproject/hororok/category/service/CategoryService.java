package com.sideproject.hororok.category.service;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.category.dto.CategoryAndKeyword;
import com.sideproject.hororok.category.entity.Category;
import com.sideproject.hororok.category.repository.CategoryRepository;
import com.sideproject.hororok.keword.entity.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @LogTrace
    public List<CategoryAndKeyword> findAllCategoryAndKeyword() {

        List<CategoryAndKeyword> categoryAndKeywords = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            List<String> keywords = new ArrayList<>();
            for (Keyword keyword : category.getKeywords()) {
                keywords.add(keyword.getName());
            }
            categoryAndKeywords.add(new CategoryAndKeyword(category.getName(), keywords));
        }

        return categoryAndKeywords;
    }
}
