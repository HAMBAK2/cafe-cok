package com.sideproject.hororok.common.annotation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.hororok.admin.application.AdminService;
import com.sideproject.hororok.admin.presentation.AdminController;
import com.sideproject.hororok.auth.application.AuthService;
import com.sideproject.hororok.auth.application.OAuthClient;
import com.sideproject.hororok.auth.presentation.AuthController;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.application.BookmarkService;
import com.sideproject.hororok.bookmark.presentation.BookmarkController;
import com.sideproject.hororok.bookmark.presentation.BookmarkFolderController;
import com.sideproject.hororok.cafe.application.CafeService;
import com.sideproject.hororok.cafe.presentation.CafeController;
import com.sideproject.hororok.combination.application.CombinationService;
import com.sideproject.hororok.combination.presentation.CombinationController;
import com.sideproject.hororok.member.application.MyPageService;
import com.sideproject.hororok.member.presentation.MyPageController;
import com.sideproject.hororok.plan.application.PlanService;
import com.sideproject.hororok.plan.presentation.PlanController;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.review.presentation.ReviewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest({
        AuthController.class,
        BookmarkFolderController.class,
        BookmarkController.class,
        PlanController.class,
        MyPageController.class,
        ReviewController.class,
        CafeController.class,
        CombinationController.class,
        AdminController.class
})
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {


    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public BookmarkService bookmarkService;

    @MockBean
    public BookmarkFolderService bookmarkFolderService;

    @MockBean
    public AuthService authService;

    @MockBean
    public OAuthClient oAuthClient;

    @MockBean
    public PlanService planService;

    @MockBean
    public MyPageService myPageService;

    @MockBean
    public ReviewService reviewService;

    @MockBean
    public CafeService cafeService;

    @MockBean
    public CombinationService combinationService;

    @MockBean
    public AdminService adminService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

}
