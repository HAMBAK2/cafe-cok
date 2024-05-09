package com.sideproject.cafe_cok.keword.domain.repository;

import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.common.annotation.RepositoryTest;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.exception.NoSuchKeywordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class KeywordRepositoryTest extends RepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("키워드 이름으로 키워드를 찾는다.")
    public void test_get_keyword_by_name() {

        Keyword keyword = new Keyword("아메리카노", Category.MENU);
        Keyword savedKeyword = keywordRepository.save(keyword);

        Keyword findKeyword = keywordRepository.getByName(keyword.getName());

        assertThat(findKeyword.getName()).isEqualTo(savedKeyword.getName());
        assertThat(findKeyword.getCategory()).isEqualTo(savedKeyword.getCategory());
    }

    @Test
    @DisplayName("존재하지 않는 키워드 이름으로 조회하면 에러를 리턴한다.")
    public void test_return_error_not_exist_name() {

        String notExistName = "존재하지 않는 이름";
        assertThatThrownBy(() -> keywordRepository.getByName(notExistName))
                .isInstanceOf(NoSuchKeywordException.class);
    }
}