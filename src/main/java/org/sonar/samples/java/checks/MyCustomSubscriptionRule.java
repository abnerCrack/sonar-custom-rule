/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol.MethodSymbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "AvoidMethodWithSameTypeInArgument")
/*
 * To use subscription visitor, just extend the IssuableSubscriptionVisitor.
 * 要使用订阅访问者，只需扩展 IssuableSubscriptionVisitor
 */
public class MyCustomSubscriptionRule extends IssuableSubscriptionVisitor {

  @Override
  public List<Tree.Kind> nodesToVisit() {
    // 注册到你希望在访问时调用的节点类型。
    return Collections.singletonList(Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    // 将节点转换为正确的类型 :
    // 在这种情况下，我们只注册了一种类型，因此我们只会收到 MethodTree 查看 Tree.Kind 枚举以了解您可以使用哪种类型
    // 根据种类进行转换。
    MethodTree methodTree = (MethodTree) tree;
    // 检索方法的符号。
    MethodSymbol methodSymbol = methodTree.symbol();
    Type returnType = methodSymbol.returnType().type();
    // 检查方法是否只有一个参数。
    if (methodSymbol.parameterTypes().size() == 1) {
      Type argType = methodSymbol.parameterTypes().get(0);
      // 验证参数类型是否与返回类型相同。.
      if (argType.is(returnType.fullyQualifiedName())) {
        // 在 SyntaxTree 的这个节点上上报issue
        reportIssue(tree, "message");
      }
    }
  }
}
