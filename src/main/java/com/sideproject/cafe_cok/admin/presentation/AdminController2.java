package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService2;
import com.sideproject.cafe_cok.admin.dto.AdminCafeDto;
import com.sideproject.cafe_cok.admin.dto.AdminSuggestionDto;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController2 {

    private final AdminService2 adminService2;

    @GetMapping("/suggestions")
    public String suggestions(Model model) {

        List<AdminSuggestionDto> findSuggestions = adminService2.findFeedbackByCategory(FeedbackCategory.IMPROVEMENT_SUGGESTION);
        model.addAttribute("suggestions", findSuggestions);
        return "page/feedback/suggestions";
    }

    @GetMapping("/withdrawal-reason")
    public String withdrawalReason(Model model) {

        List<AdminSuggestionDto> findWithdrawalReasons = adminService2.findFeedbackByCategory(FeedbackCategory.WITHDRAWAL_REASON);
        model.addAttribute("withdrawalReasons", findWithdrawalReasons);
        return "page/feedback/withdrawal-reason";
    }

    @GetMapping("/cafes")
    public String getCafes(Model model) {
        List<AdminCafeDto> findCafes = adminService2.findCafes();
        model.addAttribute("cafes", findCafes);



        return "page/cafe/list";
    }

    @GetMapping("/cafes/{id}")
    public String getCafeById(@PathVariable Long id, Model model) {
        AdminCafeDto findCafe = adminService2.findCafeById(id);
        model.addAttribute("cafe", findCafe);

        List<String> daysOfWeek = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        model.addAttribute("daysOfWeek", daysOfWeek);

        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.asMap().get("message"));
        }
        return "page/cafe/detail";
    }
}
