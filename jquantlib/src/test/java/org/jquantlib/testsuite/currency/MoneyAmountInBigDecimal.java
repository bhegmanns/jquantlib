package org.jquantlib.testsuite.currency;

import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.hamcrest.collection.IsMapContaining;
import org.jquantlib.currencies.Europe;
import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.testsuite.matcher.IsAmount;
import org.jquantlib.testsuite.unitrule.InTestRule;
import org.jquantlib.currencies.Money;
import org.junit.Rule;
import org.junit.Test;

public class MoneyAmountInBigDecimal {
	
	@Rule
	public InTestRule inTestRule = InTestRule.getWithBaseCurrency(Europe.EUR);

	@Test(expected = NullPointerException.class)
	public void nullAsCurrencyNotAllowed(){
		new Money(null, BigDecimal.ONE);
	}
	
	@Test(expected = NullPointerException.class)
	public void nullAsAmountNotAllowed(){
		new Money(Europe.EUR, null);
	}
	
	@Test
	public void asDouble(){
		Money moneyAmount = new Money(Optional.of(Europe.EUR), Optional.of(BigDecimal.ONE));
		assertThat(moneyAmount, IsAmount.amount(BigDecimal.ONE));
	}
}
