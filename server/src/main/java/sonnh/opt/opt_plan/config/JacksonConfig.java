package sonnh.opt.opt_plan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// Handle lazy loading
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// Configure date/time format
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// Configure property inclusion
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		// Configure Hibernate5 module
		mapper.registerModule(new Hibernate5JakartaModule()
				.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, false)
				.configure(Hibernate5JakartaModule.Feature.USE_TRANSIENT_ANNOTATION,
						true));

		return mapper;
	}
}