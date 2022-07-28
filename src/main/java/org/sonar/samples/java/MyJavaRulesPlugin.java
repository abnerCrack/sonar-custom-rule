/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.sonar.samples.java;

import org.sonar.api.Plugin;

/*
 * 包含自定义规则的插件入口点
 */
public class MyJavaRulesPlugin implements Plugin {

  @Override
  public void define(Context context) {
    // server extensions -> objects are instantiated during server startup
    context.addExtension(MyJavaRulesDefinition.class);

    // batch extensions -> objects are instantiated during code analysis
    context.addExtension(MyJavaFileCheckRegistrar.class);

  }

}
