package org.jquantlib.testsuite.unitrule;

import java.util.Optional;

import org.jquantlib.currencies.Currency;
import org.jquantlib.currencies.Europe;
import org.jquantlib.currencies.Money;
import org.jquantlib.currencies.Money.ConversionType;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 
 * @author B. Hegmanns
 */
public class InTestRule implements TestRule{

	private static final String SYSTEM_TEST_PROPERTY = "EXPERIMENTAL";
	
	private Optional<Currency> baseCurrency = Optional.empty();
	private Optional<ConversionType> conversionTypeBeforeTest = Optional.empty();
	private Optional<Currency> baseCurrencyBeforeTest = Optional.empty();
	
	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {
			
			@Override
			public void evaluate() throws Throwable {
				Optional<String> testPropertyBeforeTest = Optional.ofNullable(System.getProperty(SYSTEM_TEST_PROPERTY));
				System.setProperty(SYSTEM_TEST_PROPERTY, "1");
				baseCurrency.ifPresent(c -> {baseCurrencyBeforeTest = Optional.ofNullable(Money.baseCurrency); conversionTypeBeforeTest = Optional.ofNullable(Money.conversionType); Money.conversionType = ConversionType.BaseCurrencyConversion; Money.baseCurrency = c;});
				try{
				base.evaluate();
				}finally{
					System.clearProperty(SYSTEM_TEST_PROPERTY);
					Money.baseCurrency = null;
					Money.conversionType = null;
					testPropertyBeforeTest.ifPresent(p -> System.setProperty(SYSTEM_TEST_PROPERTY, p));
					baseCurrencyBeforeTest.ifPresent(c -> Money.baseCurrency = c);
					conversionTypeBeforeTest.ifPresent(t -> Money.conversionType = t);
				}
			}
		};
	}

	public static InTestRule get() {
		return new InTestRule();
	}
	
	public static InTestRule getWithBaseCurrency(Currency baseCurrency){
		InTestRule inTestRule = get();
		inTestRule.baseCurrency = Optional.of(baseCurrency);
		inTestRule.conversionTypeBeforeTest = Optional.of(ConversionType.BaseCurrencyConversion);
		return inTestRule;
	}
	
	public static InTestRule getWithEurBaseCurrency(){
		return getWithBaseCurrency(Europe.EUR);
	}

}
