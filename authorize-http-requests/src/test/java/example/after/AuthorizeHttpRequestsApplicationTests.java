/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.after;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeHttpRequestsApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(authorities = "read")
	void readWhenReadAccessThenLists() throws Exception {
		this.mvc.perform(get("/read"))
				.andExpect(content().string("[]"));
	}

	@Test
	@WithMockUser(authorities = "write")
	void writeWhenWriteAccessThenAdds() throws Exception {
		this.mvc.perform(post("/write").content("item").with(csrf()))
				.andExpect(content().string("[\"item\"]"));
	}

	@Test
	@WithMockUser(authorities = "read")
	void readWhenWriteAccessThen403() throws Exception {
		this.mvc.perform(post("/write").content("item").with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	void indexWhenAuthenticatedThenResponds() throws Exception {
		this.mvc.perform(get("/"))
				.andExpect(content().string("index"));
	}

	@Test
	void indexWhenUnauthenticatedThe401() throws Exception {
		this.mvc.perform(get("/"))
				.andExpect(status().isUnauthorized());
	}
}
