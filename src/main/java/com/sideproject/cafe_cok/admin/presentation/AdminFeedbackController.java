package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminFeedbackService;
import com.sideproject.cafe_cok.admin.dto.SuggestionDto;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final AdminFeedbackService adminFeedbackService;

    @GetMapping("/suggestions")
    public String suggestions(Model model) {

        List<SuggestionDto> findSuggestionDtos = adminFeedbackService.findFeedbackByCategory(FeedbackCategory.IMPROVEMENT_SUGGESTION);
        model.addAttribute("suggestions", findSuggestionDtos);
        return "page/main/suggestions";
    }

    @GetMapping("/withdrawal-reason")
    public String withdrawalReason(Model model) {

        List<SuggestionDto> findWithdrawalReasons = adminFeedbackService.findFeedbackByCategory(FeedbackCategory.WITHDRAWAL_REASON);
        model.addAttribute("withdrawalReasons", findWithdrawalReasons);
        return "page/main/withdrawal-reason";
    }
}
