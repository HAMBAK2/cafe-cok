package com.sideproject.hororok.category.service;

import com.sideproject.hororok.category.dto.response.CategoryKeywordResponse;
import com.sideproject.hororok.category.entity.Category;
import com.sideproject.hororok.category.repository.CategoryRepository;
import com.sideproject.hororok.keword.entity.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryKeywordResponse findAllCategoryAndKeyword() {

        Map<String, List<String>> keywordsByCategory = new HashMap<>();

        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            List<String> keywords = new ArrayList<>();
            for (Keyword keyword : category.getKeywords()) {
                keywords.add(keyword.getName());
            }
            keywordsByCategory.put(category.getName(), keywords);
        }

        return new CategoryKeywordResponse(keywordsByCategory);
    }
}
