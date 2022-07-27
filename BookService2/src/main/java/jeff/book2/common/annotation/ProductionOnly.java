package jeff.book2.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 表明某個元件只會作用於正式環境，測試環境將不起作用。
 * 
 * @author Jeff Huang
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(value = { "prod" })
public @interface ProductionOnly {

}
