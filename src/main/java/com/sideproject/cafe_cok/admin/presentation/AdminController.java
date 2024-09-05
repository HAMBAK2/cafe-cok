package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService;
import com.sideproject.cafe_cok.cafe.dto.CafeAdminDto;
import com.sideproject.cafe_cok.admin.dto.AdminSuggestionDto;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final List<String> daysOfWeek = Arrays.asList("월", "화", "수", "목", "금", "토", "일");

    @Value("${oauth.kakao.client-id}")
    private String kakaoApiKey;

    @GetMapping()
    public String home() {

        return "page/home";
    }

    @GetMapping("/suggestions")
    public String suggestions(Model model) {
        List<AdminSuggestionDto> findSuggestions = adminService.findFeedbackByCategory(FeedbackCategory.IMPROVEMENT_SUGGESTION);
        model.addAttribute("suggestions", findSuggestions);
        return "page/feedback/suggestions";
    }

    @GetMapping("/withdrawal-reason")
    public String withdrawalReason(Model model) {

        List<AdminSuggestionDto> findWithdrawalReasons = adminService.findFeedbackByCategory(FeedbackCategory.WITHDRAWAL_REASON);
        model.addAttribute("withdrawalReasons", findWithdrawalReasons);
        return "page/feedback/withdrawal-reason";
    }

    @GetMapping("/cafes")
    public String getCafes(Model model) {
        List<CafeAdminDto> findCafes = adminService.findCafes();
        model.addAttribute("cafes", findCafes);
        return "page/cafe/list";
    }

    @GetMapping("/cafe/{id}")
    public String getCafeById(@PathVariable Long id, Model model) {
        CafeAdminDto findCafe = adminService.findCafeById(id);
        model.addAttribute("cafe", findCafe);
        model.addAttribute("daysOfWeek", daysOfWeek);
        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.asMap().get("message"));
        }
        return "page/cafe/detail";
    }

    @GetMapping("/cafe/add")
    public String cafeRegisterForm(Model model) {
        model.addAttribute("daysOfWeek", daysOfWeek);
        return "page/cafe/add";
    }

    @GetMapping("/app-key")
    @ResponseBody
    @Hidden
    public String getAppKey() {
        return kakaoApiKey;
    }
}
