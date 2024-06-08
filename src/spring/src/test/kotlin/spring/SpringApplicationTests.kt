package spring

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class SpringApplicationTests {

	var testRestTemplate = TestRestTemplate()

	@Test
	fun simpleGetTest() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:8000/api/employee"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)

		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
		Assertions.assertEquals("[]", result.body)
	}
}
