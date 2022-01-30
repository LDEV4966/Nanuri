package com.example.nanuri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 최소 하나이상의 Entity 클래스를 필요로 함, 그래서 @SpringBootTest와 함께 있으면 의도치 않게 Scan 되 버려 이를 분리  시켜줌
public class JpaConfig { // Auditing 기능 활성화
}
