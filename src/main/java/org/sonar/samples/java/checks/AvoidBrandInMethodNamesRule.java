/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.sonar.samples.java.checks;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.samples.java.utils.PrinterVisitor;

import java.util.Locale;

@Rule(key = "AvoidBrandInMethodNames")
public class AvoidBrandInMethodNamesRule extends BaseTreeVisitor implements JavaFileScanner {

  private static final Logger LOGGER = Loggers.get(AvoidBrandInMethodNamesRule.class);

  private JavaFileScannerContext context;

  protected static final String COMPANY_NAME = "MyCompany";

  @Override
  public void scanFile(JavaFileScannerContext context) {
    this.context = context;

    // 调用tree上的scan方法触发该访问者对AST的访问
    scan(context.getTree());

    // 出于调试目的，您可以打印出分析文件的整个AST语法树
    // 在生产中，一旦日志级别设置为 DEBUG，这将显示所有语法树
    PrinterVisitor.print(context.getTree(), LOGGER::debug);
  }

  /**
   * 覆盖访问者方法以实现规则的逻辑。
   *
   * @param tree 访问方法的AST语法树.
   */
  @Override
  public void visitMethod(MethodTree tree) {

    if (tree.simpleName().name().toUpperCase(Locale.ROOT).contains(COMPANY_NAME.toUpperCase(Locale.ROOT))) {
      // Adds an issue by attaching it with the tree and the rule
      context.reportIssue(this, tree, "Avoid using Brand in method name");
    }
    // 调用 super 实现允许继续访问 AST.
    // 注意: 总是调用这个方法来访问树的每个节点.
    super.visitMethod(tree);

    // All the code located after the call to the overridden method is executed when leaving the
    // node
    // 离开节点时执行被覆盖方法调用之后的所有代码
  }
}
