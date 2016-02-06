package org.jquantlib.testsuite.matcher;

import java.io.ObjectStreamClass;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.BaseStream;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jquantlib.currencies.Currency;
import org.jquantlib.currencies.Money;

public class IsAmount extends TypeSafeMatcher<Money>{

	private IsAmount(Money expected) {
		this(Optional.ofNullable(BigDecimal.valueOf(expected.value())), Optional.ofNullable(expected.currency()));
	}
	
	private IsAmount(Optional<BigDecimal> amount, Optional<Currency> currency){
		this.expectedAmount = amount;
		this.expectedCurrency = currency;
	}
	
	private Optional<BigDecimal> expectedAmount;
	private Optional<Currency> expectedCurrency;

	@Override
	public void describeTo(Description description) {
		description.appendValue((expectedAmount.isPresent() ?  "amount=" + expectedAmount.get() : ""))
			.appendValue(expectedCurrency.isPresent() ? "currency=" + expectedCurrency.get() : "");
	}
	
	private Function<Currency, Optional<Boolean>> equalsWithCurrency(final Money money)  {
			Function<Currency, Optional<Boolean>> equalsFunctions = (c) -> {return Optional.of(Boolean.valueOf(money.currency().equals(c)));};
			return  equalsFunctions;
	}
	
	private Function<BigDecimal, Optional<Boolean>> equalsWithAmount(final Money money){
		Function<BigDecimal, Optional<Boolean>> equalsFunction = (c) -> {return Optional.of(Boolean.valueOf(BigDecimal.valueOf(money.value()).compareTo(c) == 0));};
		return equalsFunction;
	}
	
	private boolean maps(Optional<Boolean> ...booleans){
		return Arrays.asList(booleans).stream().filter(b->b.isPresent() && !b.get()).count() == 0;
	}

	@Override
	protected boolean matchesSafely(Money item) {
		Optional<Boolean> currencyMaps = expectedCurrency.flatMap(equalsWithCurrency(item));
		Optional<Boolean> amountMaps = expectedAmount.flatMap(equalsWithAmount(item));
		
		return maps(currencyMaps, amountMaps);
	}

	@Factory
	public static Matcher<Money> amount(Money expected){
		return new IsAmount(expected);
	}
	
	@Factory
	public static Matcher<Money> amount(BigDecimal amount, Currency currency){
		return new IsAmount(Optional.of(amount), Optional.of(currency));
	}
	
	@Factory
	public static Matcher<Money> amount(Optional<BigDecimal> amount, Optional<Currency> currency){
		return new IsAmount(amount, currency);
	}
	
	public static Matcher<Money> currency(Currency currency){
		return amount(Optional.empty(), Optional.of(currency));
	}
	
	public static Matcher<Money> amount(BigDecimal amount){
		return amount(Optional.of(amount), Optional.empty());
	}
}
