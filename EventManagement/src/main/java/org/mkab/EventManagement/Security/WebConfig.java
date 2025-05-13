package org.mkab.EventManagement.Security;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow all endpoints to be accessed from the specified frontend origin
        registry.addMapping("/**")  // Apply to all endpoints
                .allowedOrigins("http://localhost:4200")  // Allow the frontend Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(false);  // Allow cookies (if needed)
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations("file:/home/nurekowser01/Documents/GitHub/EventManagement_v6_Deepseek/EventManagement/uploads/");
    }
}

