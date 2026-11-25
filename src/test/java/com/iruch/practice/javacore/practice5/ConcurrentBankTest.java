package com.iruch.practice.javacore.practice5;

import com.iruch.practice.javacore.practice5.exception.CurrencyMismatchException;
import com.iruch.practice.javacore.practice5.exception.InsufficientFundsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Тестирование банка")
class ConcurrentBankTest {

    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(4);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    @DisplayName("У двух созданных счетов разные номера")
    void givenBank_whenCreateAccounts_thenAccountsHaveUniqueIds() {
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(BankAccountType.STANDARD, Currency.EUR, 100);
        BankAccount account2 = bank.createAccount(BankAccountType.OVERDRAFT, Currency.USD, 200);
        assertThat(account1.getAccount()).isNotEqualTo(account2.getAccount());
    }

    @Test
    @DisplayName("С одного счета списали, на другой зачислили столько же")
    void givenTwoAccounts_whenTransfer_thenMoneyMovesFromSourceToTarget() {
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(BankAccountType.STANDARD, Currency.USD, 1000);
        BankAccount account2 = bank.createAccount(BankAccountType.STANDARD, Currency.USD, 1000);
        bank.transfer(account1, account2, 500);
        assertThat(account1.getBalance()).isEqualTo(500);
        assertThat(account2.getBalance()).isEqualTo(1500);
    }

    @Test
    @DisplayName("Исключение, оба баланса не изменились из-за разной валюты")
    void givenDifferentCurrencies_whenTransfer_thenThrowsAndBalancesUnchanged() {
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(BankAccountType.STANDARD, Currency.USD, 1000);
        BankAccount account2 = bank.createAccount(BankAccountType.STANDARD, Currency.EUR, 1000);
        assertThatThrownBy(() -> bank.transfer(account1, account2, 500)).isInstanceOf(CurrencyMismatchException.class);
        assertThat(account1.getBalance()).isEqualTo(1000);
        assertThat(account2.getBalance()).isEqualTo(1000);
    }

    @Test
    @DisplayName("Исключение, оба баланса не изменились из-за нехватки средств")
    void givenInsufficientFunds_whenTransfer_thenThrowsAndBalancesUnchanged() {
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(BankAccountType.STANDARD, Currency.EUR, 1000);
        BankAccount account2 = bank.createAccount(BankAccountType.STANDARD, Currency.EUR, 1000);
        assertThatThrownBy(() -> bank.transfer(account1, account2, 2000)).isInstanceOf(InsufficientFundsException.class);
        assertThat(account1.getBalance()).isEqualTo(1000);
        assertThat(account2.getBalance()).isEqualTo(1000);
    }

    @Test
    @DisplayName("Суммы балансов всех счетов")
    void givenSeveralAccounts_whenGetTotalBalance_thenReturnsSum() {
        ConcurrentBank bank = new ConcurrentBank();
        bank.createAccount(BankAccountType.STANDARD, Currency.USD, 1000);
        bank.createAccount(BankAccountType.OVERDRAFT, Currency.EUR, 100);
        bank.createAccount(BankAccountType.STANDARD, Currency.RUB, 100);
        bank.createAccount(BankAccountType.OVERDRAFT, Currency.EUR, 200);
        bank.createAccount(BankAccountType.STANDARD, Currency.EUR, 100);
        bank.createAccount(BankAccountType.OVERDRAFT, Currency.EUR, 300);
        bank.createAccount(BankAccountType.STANDARD, Currency.RUB, 100);
        bank.createAccount(BankAccountType.STANDARD, Currency.RUB, 300);
        bank.createAccount(BankAccountType.OVERDRAFT, Currency.EUR, 400);
        bank.createAccount(BankAccountType.OVERDRAFT, Currency.EUR, 1000);
        assertThat(bank.getTotalBalance()).isEqualTo(500); // в рублях по дефолту
        assertThat(bank.getTotalBalance(Currency.EUR)).isEqualTo(2100); // в евро
        assertThat(bank.getTotalBalanceByCurrency())
                .isEqualTo(Map.of(Currency.USD, 1000L, Currency.EUR, 2100L, Currency.RUB, 500L));
    }

    @Test
    @DisplayName("Много потоков A-B и B-A")
    void givenTwoAccounts_whenConcurrentOppositeTransfers_thenNoDeadlockAndTotalPreserved() throws Exception {
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(BankAccountType.STANDARD, Currency.USD, 1000);
        BankAccount account2 = bank.createAccount(BankAccountType.STANDARD, Currency.USD, 500);

        Future<?> future1 = executor.submit(() -> bank.transfer(account1, account2, 200));
        Future<?> future2 = executor.submit(() -> bank.transfer(account2, account1, 100));
        future1.get(5, TimeUnit.SECONDS);
        future2.get(5, TimeUnit.SECONDS);

        assertThat(account1.getBalance()).isEqualTo(900);
        assertThat(account2.getBalance()).isEqualTo(600);
        assertThat(bank.getTotalBalance(Currency.USD)).isEqualTo(1500);
    }


}