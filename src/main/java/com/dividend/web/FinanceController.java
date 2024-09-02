package com.dividend.web;

import com.dividend.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 배당금 관련 API 제공 컨트롤러
 */
@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    /**
     * 회사 이름을 입력받아 해당 회사의 배당금 정보 반환
     *
     * @param companyName 조회할 회사 이름
     * @return 회사의 배당금 정보를 포함하는 응답
     */
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        var result = this.financeService.getDividendByCompanyNames(companyName);
        return ResponseEntity.ok(result);
    }
}
