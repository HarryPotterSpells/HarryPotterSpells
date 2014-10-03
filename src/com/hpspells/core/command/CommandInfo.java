package com.hpspells.core.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    String name() default ""; // Defaults to class simple name

    String description() default ""; // Should corrospond with a language file entry

    String aliases() default "";

    String permission() default ""; // Defaults to harrypotterspells.[name()]

    String permissionDefault() default "OP";

    String usage() default "<command>";

    String noPermissionMessage() default "cmdNoPermission"; // Should corrospond with a language file entry

}
