package com.fastcampus.boardadmin.repository;

import com.fastcampus.boardadmin.domain.UserAccount;
import com.fastcampus.boardadmin.domain.constant.RoleType;
import org.assertj.core.api.Assertions;
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
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    public void givenUserAccounts_whenSelecting_thenWorksFine() throws Exception{
        //given

        //when
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        //then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    public void givenUserAccount_whenInserting_thenWorksFine() throws Exception{
        //given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);
        //when
        userAccountRepository.save(userAccount);
        //then
        assertThat(userAccountRepository.count())
                .isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    public void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() throws Exception{
        //given
        UserAccount userAccount = userAccountRepository.getReferenceById("ams");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.MANAGER));
        userAccount.removeRoleType(RoleType.ADMIN);
        //when
        UserAccount updatedAccount = userAccountRepository.saveAndFlush(userAccount);
        //then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "ams")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.MANAGER, RoleType.USER));

    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    public void givenUserAccount_whenDeleting_thenWorksFine() throws Exception{
        //given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.getReferenceById("ams");

        //when
        userAccountRepository.delete(userAccount);
        userAccountRepository.flush();
        //then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
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