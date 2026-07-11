package com.fitu.benefitu.domain.benefits.batch.scheduler;

import com.fitu.benefitu.domain.benefits.batch.service.BatchService;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.repository.BenefitsRepository;
import com.fitu.benefitu.domain.benefits.service.BenefitsInnerService;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final BatchService batchService;
    private final BenefitsInnerService benefitsInnerService;
    private final UsersRepository usersRepository;
    private final BenefitsRepository benefitsRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
//        // 테스트 목적으로 임시로 잠가둡니다.
//        // 서버가 완전히 켜진 직후 배치를 실행합니다.
//        batchService.BatchAll();
//        System.out.println("[batch] 작업 완료했습니다.");
//        // 새로 받아온 데이터에 대해 기존 사용자별 신청할 수 있는 혜택을 추출 및 저장합니다.
//        List<Users> users = usersRepository.findAll();
//        List<Benefits> benefits = benefitsRepository.findAll();
//        benefitsInnerService.UpdateAllUsersAppliedBenefits(users, benefits);
    }
}
