package org.typeframed.api;

import org.junit.Assert;
import org.junit.Test;

public class Int32HeaderProviderTest {

	@Test
	public void trivialNumberTest() throws Exception {
		Int32HeaderProvider provider = new Int32HeaderProvider();
		Assert.assertEquals(666, (int) provider.fromBytes(provider.toBytes(666)));
	}
}
