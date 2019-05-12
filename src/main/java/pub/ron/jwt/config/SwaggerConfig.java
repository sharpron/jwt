package pub.ron.jwt.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import pub.ron.jwt.security.jwt.JwtUtils;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * swagger 配置
 *
 * @author ron
 * 2019.01.17
 */
@EnableSwagger2
@Configuration
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {


    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name(JwtUtils.JWT_AUTH_HEADER)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .description("json web token")
                .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Api")
                .version("2.0")
                .build();
    }

    /**
     * 处理{@link Pageable} 在swagger-ui中参数的问题
     *
     * @param resolver          类型解析器
     * @param restConfiguration rest配置
     * @return 转换规则
     */
    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver,
            final RepositoryRestConfiguration restConfiguration) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Collections.singletonList(
                        newRule(resolver.resolve(Pageable.class),
                                resolver.resolve(pageableMixin(restConfiguration)))
                );
            }
        };
    }

    private Type pageableMixin(RepositoryRestConfiguration restConfiguration) {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class, restConfiguration.getPageParamName()),
                        property(Integer.class, restConfiguration.getLimitParamName()),
                        property(String.class, restConfiguration.getSortParamName())
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }
}
