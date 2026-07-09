package com.fitu.benefitu.domain.benefits.batch.scheduler;

import com.fitu.benefitu.domain.benefits.batch.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final BatchService batchService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 테스트 목적으로 임시로 잠가둡니다.
//        // 서버가 완전히 켜진 직후 배치를 실행합니다.
//        batchService.BatchAll();
//        System.out.println("[batch] 작업 완료했습니다.");
    }
}
