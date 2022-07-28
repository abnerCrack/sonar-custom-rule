/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MethodTree;

/*
 * 这个类是一个自定义规则实现的示例.
 * The (stupid) rule raises a minor issue each time a method is encountered.
 */
@Rule(key = "AvoidMethodDeclaration")
/*
 * 该类扩展了 BaseTreeVisitor：Java AST 的访问者。
 * 规则的逻辑是 通过覆盖其方法来实现，它还实现了 JavaFileScanner 接口
 * 注入 JavaFileScannerContext 以将问题附加到此上下文。
 */
public class AvoidMethodDeclarationRule extends BaseTreeVisitor implements JavaFileScanner {

  /*
   * 用于存储上下文的私有字段：这是用于创建问题的对象
   */
  private JavaFileScannerContext context;

  /*
   * JavaFileScanner 接口方法的实现。
   * @param 用于将issue附加到源文件的上下文对象
   *
   */
  @Override
  public void scanFile(JavaFileScannerContext context) {
    this.context = context;

    // The call to the scan method on the root of the tree triggers the visit of the AST by this visitor
    scan(context.getTree());
  }

  /*
   * 覆盖访问者方法以实现规则的逻辑。
   * @param tree AST of the visited method.
   */
  @Override
  public void visitMethod(MethodTree tree) {
    // All the code located before the call to the overridden method is executed before visiting the node

    // Adds an issue by attaching it with the tree and the rule
    context.reportIssue(this, tree, "Avoid declaring methods (don't ask why)");

    // The call to the super implementation allows to continue the visit of the AST.
    // Be careful to always call this method to visit every node of the tree.
    super.visitMethod(tree);

    // All the code located after the call to the overridden method is executed when leaving the node
  }

}
