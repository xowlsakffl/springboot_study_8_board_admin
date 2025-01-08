package com.fastcampus.boardadmin.controller;

import com.fastcampus.boardadmin.dto.response.ArticleResponse;
import com.fastcampus.boardadmin.service.ArticleManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/management/articles")
@Controller
public class ArticleManagementController {

    private final ArticleManagementService articleManagementService;

    @GetMapping
    public String articles(Model model, HttpServletRequest request) {
        model.addAttribute(
                "articles",
                articleManagementService.getArticles().stream().map(ArticleResponse::withoutContent).toList()
        );

        String requestURI = request.getRequestURI();
        model.addAttribute("isManagement", requestURI.startsWith("/management"));
        model.addAttribute("isArticles", requestURI.equals("/management/articles"));
        model.addAttribute("isArticleComments", requestURI.equals("/management/article-comments"));
        model.addAttribute("isUserAccounts", requestURI.equals("/management/user-accounts"));
        model.addAttribute("isAdmin", requestURI.startsWith("/admin"));
        model.addAttribute("isMembers", requestURI.equals("/admin/members"));

        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{articleId}")
    public ArticleResponse article(@PathVariable Long articleId) {
        return ArticleResponse.withContent(articleManagementService.getArticle(articleId));
    }

    @PostMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleManagementService.deleteArticle(articleId);

        return "redirect:/management/articles";
    }

}
