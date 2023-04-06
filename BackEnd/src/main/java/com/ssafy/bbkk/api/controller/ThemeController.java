package com.ssafy.bbkk.api.controller;

import com.ssafy.bbkk.api.dto.AwardThemeBundleResponse;
import com.ssafy.bbkk.api.dto.PreviewThemeResponse;
import com.ssafy.bbkk.api.dto.ReviewOfThemeResponse;
import com.ssafy.bbkk.api.dto.SearchThemeRequest;
import com.ssafy.bbkk.api.dto.ThemeBundleResponse;
import com.ssafy.bbkk.api.dto.ThemeResponse;
import com.ssafy.bbkk.api.service.InterestThemeService;
import com.ssafy.bbkk.api.service.ReviewService;
import com.ssafy.bbkk.api.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("theme")
@RequiredArgsConstructor
public class ThemeController {

    private static final Logger logger = LoggerFactory.getLogger(ThemeController.class);

    private final ThemeService themeService;
    private final InterestThemeService interestThemeService;
    private final ReviewService reviewService;

    @Operation(summary = "로그인 테마 목록 조회", description = "로그인시 메인 화면의 테마를 불러온다")
    @GetMapping("user")
    private ResponseEntity<Map<String, Object>> getThemeOfLoginUser(
            @AuthenticationPrincipal User user) throws Exception {
        logger.info("<<---------------(start)----------------||getThemeOfLoginUser||------------------------------------>>\n");
        logger.info(">> request : myEmail={}", user.getUsername());

        Map<String, Object> resultMap = new HashMap<>();

        List<ThemeBundleResponse> recommendThemes = themeService.getRecommendedThemes(user.getUsername());
        resultMap.put("recommendThemes", recommendThemes);
        logger.info("<< response : recommendThemes={}", recommendThemes);

        List<PreviewThemeResponse> hotThemes = themeService.getHotThemes();
        resultMap.put("hotThemes", hotThemes);
        logger.info("<< response : hotThemes={}", hotThemes);

        List<ThemeBundleResponse> topThemes = themeService.getTopThemesOfUser(user.getUsername());
        resultMap.put("topThemes", topThemes);
        logger.info("<< response : topThemes={}", topThemes);

        AwardThemeBundleResponse awardThemes = themeService.getAwardThemes();
        resultMap.put("awardThemes", awardThemes);
        logger.info("<< response : awardThemes={}", topThemes);
        logger.info("<<---------------------------------------||getThemeOfLoginUser||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("user/recommend")
    private ResponseEntity<Map<String, Object>> getRecommendThemeOfLoginUser(
            @AuthenticationPrincipal User user) throws Exception {
        logger.info("<<---------------(start)----------------||getRecommendThemeOfLoginUser||------------------------------------>>\n");
        logger.info(">> request : myEmail={}", user.getUsername());

        Map<String, Object> resultMap = new HashMap<>();

        List<ThemeBundleResponse> recommendThemes = themeService.getRecommendedThemes(user.getUsername());
        resultMap.put("recommendThemes", recommendThemes);
        logger.info("<< response : recommendThemes={}", recommendThemes);

        logger.info("<<---------------------------------------||getRecommendThemeOfLoginUser||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("user/default")
    private ResponseEntity<Map<String, Object>> getDefaultThemeOfLoginUser(
            @AuthenticationPrincipal User user) throws Exception {
        logger.info("<<---------------(start)----------------||getDefaultThemeOfLoginUser||------------------------------------>>\n");
        logger.info(">> request : myEmail={}", user.getUsername());

        Map<String, Object> resultMap = new HashMap<>();

        List<PreviewThemeResponse> hotThemes = themeService.getHotThemes();
        resultMap.put("hotThemes", hotThemes);
        logger.info("<< response : hotThemes={}", hotThemes);

        List<ThemeBundleResponse> topThemes = themeService.getTopThemesOfUser(user.getUsername());
        resultMap.put("topThemes", topThemes);
        logger.info("<< response : topThemes={}", topThemes);

        AwardThemeBundleResponse awardThemes = themeService.getAwardThemes();
        resultMap.put("awardThemes", awardThemes);
        logger.info("<< response : awardThemes={}", topThemes);

        logger.info("<<---------------------------------------||getDefaultThemeOfLoginUser||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "게스트 테마 목록 조회", description = "메인 화면의 기본 테마 목록을 불러온다")
    @GetMapping("guest")
    private ResponseEntity<Map<String, Object>> getThemeOfGuest() throws Exception {
        logger.info("<<---------------(start)----------------||getThemeOfGuest||------------------------------------>>\n");
        logger.info(">> request : ");

        Map<String, Object> resultMap = new HashMap<>();

        List<PreviewThemeResponse> hotThemes = themeService.getHotThemes();
        resultMap.put("hotThemes", hotThemes);
        logger.info("<< response : hotThemes={}", hotThemes);

        List<ThemeBundleResponse> topThemes = themeService.getTopThemes();
        resultMap.put("topThemes", topThemes);
        logger.info("<< response : topThemes={}", topThemes);

        AwardThemeBundleResponse awardThemes = themeService.getAwardThemes();
        resultMap.put("awardThemes", awardThemes);
        logger.info("<< response : awardThemes={}", awardThemes);
        logger.info("<<---------------------------------------||getThemeOfGuest||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "테마 검색", description = "검색 필터를 기반으로 일치하는 테마를 불러온다")
    @GetMapping("search")
    private ResponseEntity<Map<String, Object>> searchedTheme(
            @ModelAttribute @Valid SearchThemeRequest searchThemeRequest, Errors errors) throws Exception {
        logger.info("<<---------------(start)----------------||searchedTheme||------------------------------------>>\n");
        logger.info(">> request : SearchThemeRequest={}", searchThemeRequest);

        // searchThemeRequest 입력값 유효성 검사
        for (FieldError error : errors.getFieldErrors())
            throw new Exception(error.getDefaultMessage());
        searchThemeRequest.validation();

        Map<String, Object> resultMap = new HashMap<>();

        Page<PreviewThemeResponse> resultPage = themeService.getSearchThemes(searchThemeRequest);
        resultMap.put("isLast", resultPage.isLast());
        logger.info("<< response : isLast={}", resultPage.isLast());

        resultMap.put("themes", resultPage.getContent());
        logger.info("<< response : themes={}", resultPage.getContent());
        logger.info("<<---------------------------------------||searchedTheme||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "테마의 상세 정보 조회", description = "해당 테마의 상세 정보를 불러온다")
    @GetMapping("{themeId}")
    private ResponseEntity<Map<String, Object>> getThemeInfo(
            @Parameter(description = "해당 테마의 Id", required = true) @PathVariable("themeId") int themeId) throws Exception {
        logger.info("<<---------------(start)----------------||getThemeInfo||------------------------------------>>\n");
        logger.info(">> request : themeId={}", themeId);

        Map<String, Object> resultMap = new HashMap<>();

        ThemeResponse themeResponse = themeService.getThemeInfo(themeId);
        resultMap.put("theme", themeResponse);
        logger.info("<< response : theme={}", themeResponse);
        logger.info("<<---------------------------------------||getThemeInfo||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "테마의 리뷰 목록 조회", description = "해당 테마의 리뷰 목록을 불러온다")
    @GetMapping("{themeId}/reviews")
    private ResponseEntity<Map<String, Object>> getReviewsOfTheme(
            @Parameter(description = "해당 테마의 Id", required = true) @PathVariable int themeId) throws Exception {
        logger.info("<<---------------(start)----------------||getReviewsOfTheme||------------------------------------>>\n");
        logger.info(">> request : themeId={}", themeId);

        Map<String, Object> resultMap = new HashMap<>();

        List<ReviewOfThemeResponse> reviewOfThemeResponses = reviewService.getReviewsOfTheme(themeId);
        resultMap.put("reviews", reviewOfThemeResponses);
        logger.info("<< response : reviews={}", reviewOfThemeResponses);
        logger.info("<<---------------------------------------||getReviewsOfTheme||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "테마 상세 로그인 정보", description = "사용자가 관심있는 테마인지, 리뷰를 작성했는지 여부를 불러온다.")
    @GetMapping("{themeId}/user")
    private ResponseEntity<Map<String, Object>> getUserOfTheme(
            @AuthenticationPrincipal User user,
            @Parameter(description = "해당 테마의 Id", required = true) @PathVariable int themeId) throws Exception {
        logger.info("<<---------------(start)----------------||getUserOfTheme||------------------------------------>>\n");
        logger.info(">> request : themeId={}", themeId);

        Map<String, Object> resultMap = new HashMap<>();

        boolean isInterest = interestThemeService.isInterestTheme(user.getUsername(), themeId);
        resultMap.put("isInterest",isInterest);
        logger.info("<< response : isInterest={}",isInterest);

        boolean isMyReview = reviewService.isMyReview(user.getUsername(), themeId);
        resultMap.put("isMyReview",isMyReview);
        logger.info("<< response : isMyReview={}",isMyReview);
        logger.info("<<---------------------------------------||getUserOfTheme||---------------(end)--------------->>\n");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}