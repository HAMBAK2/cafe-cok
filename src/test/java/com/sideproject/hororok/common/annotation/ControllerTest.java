package com.sideproject.hororok.common.annotation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.hororok.auth.application.AuthService;
import com.sideproject.hororok.auth.application.OAuthClient;
import com.sideproject.hororok.auth.presentation.AuthController;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.application.BookmarkService;
import com.sideproject.hororok.bookmark.presentation.BookmarkController;
import com.sideproject.hororok.bookmark.presentation.BookmarkFolderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        AuthController.class,
        BookmarkFolderController.class,
        BookmarkController.class
})
@ActiveProfiles("test")
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

}
