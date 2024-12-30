package com.fastcampus.boardadmin.repository;

import com.fastcampus.boardadmin.domain.AdminAccount;
import com.fastcampus.boardadmin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final AdminAccountRepository adminAccountRepository;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    public void givenAdminAccounts_whenSelecting_thenWorksFine() throws Exception{
        //given

        //when
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();
        //then
        assertThat(adminAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    public void givenAdminAccount_whenInserting_thenWorksFine() throws Exception{
        //given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = AdminAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);
        //when
        adminAccountRepository.save(adminAccount);
        //then
        assertThat(adminAccountRepository.count())
                .isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    public void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() throws Exception{
        //given
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("ams");
        adminAccount.addRoleType(RoleType.DEVELOPER);
        adminAccount.addRoleTypes(List.of(RoleType.USER, RoleType.MANAGER));
        adminAccount.removeRoleType(RoleType.ADMIN);
        //when
        AdminAccount updatedAccount = adminAccountRepository.saveAndFlush(adminAccount);
        //then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "ams")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.MANAGER, RoleType.USER));

    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    public void givenAdminAccount_whenDeleting_thenWorksFine() throws Exception{
        //given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("ams");

        //when
        adminAccountRepository.delete(adminAccount);
        adminAccountRepository.flush();
        //then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount - 1);
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("ams");
        }
    }
}