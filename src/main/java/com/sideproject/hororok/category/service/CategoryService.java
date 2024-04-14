package com.sideproject.hororok.category.service;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.category.dto.CategoryKeywords;
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

    @LogTrace
    public CategoryKeywords findAllCategoryAndKeyword() {

        CategoryKeywords categoryKeywords = new CategoryKeywords();
        List<Category> categories = categoryRepository.findAll();


        for (Category category : categories) {
            List<String> keywords = new ArrayList<>();
            for (Keyword keyword : category.getKeywords()) {
                keywords.add(keyword.getName());
            }

            if(category.getCode().equals("PURPOSE")) {
                categoryKeywords.setPurpose(keywords);
                continue;
            }

            if(category.getCode().equals("ATMOSPHERE")) {
                categoryKeywords.setAtmosphere(keywords);
                continue;
            }

            if(category.getCode().equals("FACILITY")) {
                categoryKeywords.setFacility(keywords);
                continue;
            }

            if(category.getCode().equals("MENU")) {
                categoryKeywords.setMenu(keywords);
                continue;
            }

            if(category.getCode().equals("THEME")) {
                categoryKeywords.setTheme(keywords);
                continue;
            }
        }

        return categoryKeywords;
    }
}
