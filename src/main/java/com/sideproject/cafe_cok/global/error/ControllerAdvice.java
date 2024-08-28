//package com.sideproject.cafe_cok.global.error;
//
//import com.sideproject.cafe_cok.admin.exception.NoWithdrawalMemberException;
//import com.sideproject.cafe_cok.auth.exception.*;
//import com.sideproject.cafe_cok.global.error.dto.ErrorReportRequest;
//import com.sideproject.cafe_cok.global.error.dto.ErrorResponse;
//import com.sideproject.cafe_cok.global.error.exception.MissingRequiredValueException;
//import com.sideproject.cafe_cok.keword.exception.NoSuchKeywordException;
//import com.sideproject.cafe_cok.plan.exception.NoSuchPlanSortException;
//import com.sideproject.cafe_cok.review.exception.NoSuchReviewException;
//import com.sideproject.cafe_cok.utils.S3.exception.FileUploadException;
//import com.sideproject.cafe_cok.utils.tmap.exception.TmapException;
//import com.sideproject.cafe_cok.auth.exception.*;
//import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
//import com.sideproject.cafe_cok.cafe.exception.InvalidCafeException;
//import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderDeletionNotAllowedException;
//import com.sideproject.cafe_cok.bookmark.exception.NoSuchBookmarkException;
//import com.sideproject.cafe_cok.bookmark.exception.NoSuchFolderException;
//import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
//import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeImageException;
//import com.sideproject.cafe_cok.cafe.exception.NoSuchCategoryException;
//import com.sideproject.cafe_cok.combination.exception.NoSuchCombinationException;
//import com.sideproject.cafe_cok.member.exception.InvalidMemberException;
//import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
//import com.sideproject.cafe_cok.plan.exception.NoSuchPlanException;
//import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.multipart.support.MissingServletRequestPartException;
//import org.springframework.web.servlet.resource.NoResourceFoundException;
//
//import javax.naming.NoPermissionException;
//
//
//@RestControllerAdvice
//public class ControllerAdvice {
//
//    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);
//    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "%s 필드는 %s (전달된 값: %s)";
//
//    @ExceptionHandler({
//            InvalidMemberException.class,
//            InvalidCafeException.class,
//            MissingRequiredValueException.class,
//            NoSuchPlanSortException.class,
//            NoWithdrawalMemberException.class
//    })
//    public ResponseEntity<ErrorResponse> handleInvalidData(final RuntimeException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
//
//    @ExceptionHandler(MissingServletRequestPartException.class)
//    public ResponseEntity<ErrorResponse> handleMissingRequestPart(final MissingServletRequestPartException e) {
//        ErrorResponse errorResponse = new ErrorResponse("요청 파트 중 <" + e.getRequestPartName() + "> 파트가 존재하지 않습니다.");
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
//
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidRequestBody() {
//        ErrorResponse errorResponse = new ErrorResponse("잘못된 형식의 Request Body 입니다.");
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidDtoField(final MethodArgumentNotValidException e) {
//        FieldError firstFieldError = e.getFieldErrors().get(0);
//        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT, firstFieldError.getField(),
//                firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());
//
//        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<ErrorResponse> handleTypeMismatch() {
//        ErrorResponse errorResponse = new ErrorResponse("잘못된 데이터 타입입니다.");
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
//
//    @ExceptionHandler({
//            EmptyAuthorizationHeaderException.class,
//            InvalidTokenException.class
//    })
//    public ResponseEntity<ErrorResponse> handleInvalidAuthorization(final RuntimeException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//    }
//
//    @ExceptionHandler({
//            NoPermissionException.class,
//            DefaultFolderDeletionNotAllowedException.class,
//            DefaultFolderUpdateNotAllowedException.class,
//            InvalidRestoreMemberException.class
//    })
//    public ResponseEntity<ErrorResponse> handleNoPermission(final RuntimeException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//    }
//
//    @ExceptionHandler({
//            NoSuchCafeException.class, NoSuchTokenException.class,
//            NoSuchFolderException.class, NoSuchFolderException.class,
//            NoSuchMemberException.class, NoSuchCategoryException.class,
//            NoSuchBookmarkException.class, NoResourceFoundException.class,
//            NoSuchCafeImageException.class, NoSuchOAuthTokenException.class,
//            NoSuchPlanKeywordException.class, NoSuchCombinationException.class,
//            NoSuchKeywordException.class, NoSuchPlanException.class, NoSuchReviewException.class})
//    public ResponseEntity<ErrorResponse> handleNoSuchData(final RuntimeException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<ErrorResponse> handleNotSupportedMethod() {
//        ErrorResponse errorResponse = new ErrorResponse("지원하지 않는 HTTP 메소드 요청입니다.");
//        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
//    }
//
//
//    @ExceptionHandler({OAuthException.class, TmapException.class})
//    public ResponseEntity<ErrorResponse> handleRestClientException(final RuntimeException e) {
//        log.error(e.getMessage(), e);
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.internalServerError().body(errorResponse);
//    }
//
//
//    @ExceptionHandler(FileUploadException.class)
//    public ResponseEntity<ErrorResponse> handleFileUploadException(RuntimeException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception e,
//                                                                   final HttpServletRequest request) {
//        ErrorReportRequest errorReport = new ErrorReportRequest(request, e);
//        log.error(errorReport.getLogMessage(), e);
//
//        ErrorResponse errorResponse = new ErrorResponse("예상하지 못한 서버 에러가 발생했습니다.");
//        return ResponseEntity.internalServerError().body(errorResponse);
//    }
//}
