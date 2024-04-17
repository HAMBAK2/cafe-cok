package com.sideproject.hororok.common.annotation;


import com.sideproject.hororok.auth.application.AuthService;
import com.sideproject.hororok.auth.application.OAuthClient;
import com.sideproject.hororok.auth.presentation.AuthController;
import com.sideproject.hororok.favorite.application.FavoriteFolderService;
import com.sideproject.hororok.favorite.presentation.FavoriteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        AuthController.class,
        FavoriteController.class
})
@ActiveProfiles("test")
public abstract class ControllerTest {


    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public FavoriteFolderService favoriteFolderService;

    @MockBean
    public AuthService authService;

    @MockBean
    public OAuthClient oAuthClient;

}
