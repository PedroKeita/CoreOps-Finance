package com.coreops.coreops_finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.flyway.enabled=false"
})
class CoreopsFinanceApplicationTests {

    @Test
    void contextLoads() {
    }

}
