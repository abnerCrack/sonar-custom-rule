/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;

import java.util.Arrays;
import java.util.List;

@Rule(key = "NoIfStatementInTests")
/*
 *  要使用订阅访问者，只需扩展 IssuableSubscriptionVisitor.
 */
public class NoIfStatementInTestsRule extends IssuableSubscriptionVisitor {

  private final BaseTreeVisitor ifStatementVisitor = new IfStatementVisitor();

  /** 单元测试是一种特殊的方法，所以我们将访问它们。 */
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Arrays.asList(Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodTree method = (MethodTree) tree;
    if (!isJunit4TestMethod(method)) {
      // 任何其他不是测试的方法
      return;
    }
    BlockTree block = method.block();
    if (block == null) {
      // an abstract test method maybe?
      return;
    }
    block.accept(ifStatementVisitor);
  }

  /*
   * 检查给定方法是否使用 JUnit4 'org.junit.Test' 注释进行了注释
   */
  private static boolean isJunit4TestMethod(MethodTree method) {
    return method.symbol()
      .metadata()
      .isAnnotatedWith("org.junit.Test");
  }

  private class IfStatementVisitor extends BaseTreeVisitor {

    @Override
    public void visitIfStatement(IfStatementTree tree) {
      // 碰到if关键字就上报
      reportIssue(tree.ifKeyword(), "Remove this 'if' statement from this test.");
      super.visitIfStatement(tree);
    }

    @Override
    public void visitLambdaExpression(LambdaExpressionTree lambdaExpressionTree) {
      // 跳过 lambdas
    }

    @Override
    public void visitClass(ClassTree tree) {
      // skip 内部或匿名类
    }
  }

}
