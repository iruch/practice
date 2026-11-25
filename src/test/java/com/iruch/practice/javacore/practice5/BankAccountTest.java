package com.iruch.practice.javacore.practice5;

import com.iruch.practice.javacore.practice5.exception.AccountLimitException;
import com.iruch.practice.javacore.practice5.exception.InsufficientFundsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Тестирование банковского аккаунта")
class BankAccountTest {

    @Test
    @DisplayName("Баланс увеличился на сумму")
    void givenAccount_whenDeposit_thenBalanceIncreases() {
        BankAccount account = new BankAccount(Currency.USD);
        account.deposit(100);
        assertThat(account.getBalance()).isEqualTo(100);
    }

    @Test
    @DisplayName("Баланс уменьшился на сумму")
    void givenAccount_whenWithdraw_thenBalanceDecreases() {
        BankAccount account = new BankAccount(Currency.USD);
        account.deposit(1000);
        account.withdraw(300);
        assertThat(account.getBalance()).isEqualTo(700);
    }

    @Test
    @DisplayName("Стандартный баланс не увеличился из-за лимита")
    void givenAccount_whenDepositOverLimit_thenThrowsAndBalanceUnchanged() {
        BankAccount account = new BankAccount(Currency.USD);
        assertThatThrownBy(() -> account.deposit(6000)).isInstanceOf(AccountLimitException.class);
        assertThat(account.getBalance()).isZero();
    }

    @Test
    @DisplayName("Стандартный баланс не уменьшился из-за нехватки средств")
    void givenStandardAccount_whenWithdrawMoreThanBalance_thenThrowsAndBalanceUnchanged() {
        BankAccount account = new BankAccount(Currency.USD);
        assertThatThrownBy(() -> account.withdraw(6000)).isInstanceOf(InsufficientFundsException.class);
        assertThat(account.getBalance()).isZero();
    }
}