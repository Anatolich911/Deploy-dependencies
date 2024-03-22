package com.hoopladigital.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.MockitoAnnotations.openMocks;

public abstract class AbstractTest {

	protected BeanTestHelper beanTestHelper = new BeanTestHelper();
	protected FileTestHelper fileTestHelper = new FileTestHelper();

	private AutoCloseable openMocks;

	@BeforeEach
	public void beforeAbstractTest() {
		openMocks = openMocks(this);
	}

	@AfterEach
	public void afterAbstractTest() throws Exception {
		openMocks.close();
	}

}
