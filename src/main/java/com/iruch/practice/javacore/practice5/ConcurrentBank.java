package com.iruch.practice.javacore.practice5;

import com.iruch.practice.javacore.practice5.exception.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class ConcurrentBank {

    private static final Currency DEFAULT_CURRENCY = Currency.RUB;
    private static final String ACCOUNT_NOT_FOUND = "Счет %s не найден: %s";
    private static final String ACCOUNT_IS_SAME = "Перевод денежных средств невозможен с одного и того же банковского счета: from %s";
    private static final String CURRENCY_IS_DIFFERENT = "Валюты счета списания и зачисления различаются: from %s to %s";
    private static final String INSUFFICIENT_FUNDS = "Не хватает денежных средств при списании";
    private static final String DEPOSIT_LIMIT_EXCEEDED = "Превышен лимит при зачислении";

    private final Map<UUID, BankAccount> accounts = new HashMap<>();

    public synchronized BankAccount createAccount(long amount) {
        BankAccount account = new BankAccount(DEFAULT_CURRENCY, BankAccountType.STANDARD, amount);
        accounts.put(account.getAccount(), account);
        return account;
    }

    public synchronized BankAccount createAccount(BankAccountType type, Currency currency, long amount) {
        BankAccount account = new BankAccount(currency,  type, amount);
        accounts.put(account.getAccount(), account);
        return account;
    }

    public synchronized void transfer(BankAccount from, BankAccount to, long amount) {
        if (accounts.get(from.getAccount()) == null) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND.formatted("списания", from.getAccount()));
        }
        if (accounts.get(to.getAccount()) == null) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND.formatted("зачисления", to.getAccount()));
        }
        if (from.getAccount().equals(to.getAccount())) {
            throw new MoneyTransfersIsInvalid(ACCOUNT_IS_SAME.formatted(from.getAccount()));
        }
        if (from.getCurrency() != to.getCurrency()) {
            throw new CurrencyMismatchException(CURRENCY_IS_DIFFERENT.formatted(from.getCurrency(), to.getCurrency()));
        }
        int result = from.getAccount().compareTo(to.getAccount());
        BankAccount first = result < 0 ? from : to;
        BankAccount second = result < 0 ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (!from.canWithdraw(amount)) {
                    throw new InsufficientFundsException(INSUFFICIENT_FUNDS);
                }
                if (!to.canDeposit(amount)) {
                    throw new AccountLimitException(DEPOSIT_LIMIT_EXCEEDED);
                }
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public synchronized long getTotalBalance() {
        return getTotalBalance(DEFAULT_CURRENCY);
    }

    public synchronized long getTotalBalance(Currency currency) {
        return accounts.values().stream()
                .filter(account -> account.getCurrency() == currency)
                .mapToLong(BankAccount::getBalance)
                .sum();
    }

    public synchronized Map<Currency, Long> getTotalBalanceByCurrency() {
        EnumMap<Currency, Long> result = new EnumMap<>(Currency.class);
        accounts.values().forEach(account -> result.merge(account.getCurrency(), account.getBalance(), Long::sum));
        return result;
    }

}
