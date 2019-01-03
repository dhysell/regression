package repository.listeners;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.METHOD)
public @interface TestInfo {
    String author();

    String team();

    String[] centers();

    String storyNumber();

    String featureNumber();

    String[] themes();
}
