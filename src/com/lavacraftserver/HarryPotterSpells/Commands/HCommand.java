package com.lavacraftserver.HarryPotterSpells.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HCommand {
	
	String name() default ""; // Defaults to class simple name
	String description() adfasdfasdfasdfdefault "";
	String aliases() default "";
	String permission() default ""; // Defaults to HarryPotterSpells.[name()]
	String permissionDefault() default "OP";
	String usage() default "<command>";
	String noPermissionMessage() default "You do not have permission to run that command.";
	
}
