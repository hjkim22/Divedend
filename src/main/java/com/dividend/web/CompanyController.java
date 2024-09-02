package com.dividend.web;

import com.dividend.model.Company;
import com.dividend.model.constants.CacheKey;
import com.dividend.persist.CompanyRepository;
import com.dividend.persist.entity.CompanyEntity;
import com.dividend.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static com.dividend.model.constants.CacheKey.KEY_FINANCE;

/**
 * 회사 정보 관련 API 제공 컨트롤러
 */
@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final CacheManager redisCacheManager;

    /**
     * 자동완성 기능을 위한 회사명 리스트 반환
     *
     * @param keyword 검색할 prefix
     * @return prefix로 검색된 회사명 리스트를 포함하는 응답
     */
    @GetMapping("/autoComplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = this.companyService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 관리 중인 모든 회사 목록을 반환
     *
     * @param pageable 페이징 정보를 포함한 객체
     * @return 회사 목록을 포함하는 응답. 결과는 `Page` 인터페이스 형태로 반환
     */
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 새로운 회사 정보를 추가
     *
     * @param request 추가할 회사 정보가 담긴 객체
     * @return 추가된 회사 정보를 포함하는 응답
     * @throws RuntimeException ticker가 비어있는 경우
     */
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        this.companyService.addAutoCompleteKeyword(company.getName());
        return ResponseEntity.ok(company);
    }

    /**
     * 지정된 ticker에 해당하는 회사 정보를 삭제
     *
     * @param ticker 삭제할 회사의 ticker
     * @return 삭제된 회사 이름을 포함하는 응답
     */
    @DeleteMapping("/{ticker}")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = this.companyService.deleteCompany(ticker);
        this.clearFinanceCache(companyName);

        return ResponseEntity.ok(companyName);
    }

    /**
     * Redis 캐시에서 회사의 배당금 정보를 제거
     *
     * @param companyName 삭제할 회사의 이름
     */
    public void clearFinanceCache(String companyName) {
        this.redisCacheManager.getCache(KEY_FINANCE).evict(companyName);

    }
}
