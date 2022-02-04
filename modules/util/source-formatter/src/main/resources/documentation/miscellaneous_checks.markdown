# Miscellaneous Checks

Check | File Extensions | Description
----- | --------------- | -----------
BNDDeprecatedAppBNDsCheck | .bnd | Checks for redundant `app.bnd` in deprecated or archived modules. |
BNDSuiteCheck | .bnd | Checks that deprecated apps are moved to the `archived` folder. |
CreationMenuBuilderCheck | .java | Checks that `CreationMenuBuilder` is used when possible. |
FullyQualifiedNameCheck | .java | Finds cases where a Fully Qualified Name is used instead of importing a class. |
ItemBuilderCheck | .java | Checks that `DropdownItemBuilder`, `LabelItemBuilder` or `NavigationItemBuilder` is used when possible. |
ItemListBuilderCheck | .java | Checks that `DropdownItemListBuilder`, `LabelItemListBuilder` or `NavigationItemListBuilder` is used when possible. |
JSPModuleIllegalImportsCheck | .jsp, .jspf, .jspx, .tag, .tpl or .vm | Finds incorrect use of `com.liferay.registry.Registry` or `com.liferay.util.ContentUtil`. |
JSPParenthesesCheck | .jsp, .jspf, .jspx, .tag, .tpl or .vm | Finds incorrect use of parentheses in statement. |
JSPRedirectBackURLCheck | .jsp, .jspf, .jspx, .tag, .tpl or .vm | Validates values of variable `redirect`. |
JSPServiceUtilCheck | .jsp, .jspf, .jspx, .tag, .tpl or .vm | Finds incorrect use of `*ServiceUtil` in `.jsp` files in modules. |
Java2HTMLCheck | .java | Finds incorrect use of `.java.html` in `.jsp` files. |
JavaDiamondOperatorCheck | .java | Finds cases where Diamond Operator is not used. |
JavaDuplicateVariableCheck | .java | Finds variables where a variable with the same name already exists in an extended class. |
JavaElseStatementCheck | .java | Finds unnecessary `else` statements (when the `if` statement ends with a `return` statement). |
JavaEmptyLineAfterSuperCallCheck | .java | Finds missing emptly line after a `super` call. |
JavaUnusedSourceFormatterChecksCheck | .java | Finds `*Check` classes that are not configured. |
MapBuilderCheck | .java, .jsp, .jspf, .jspx, .tag, .tpl or .vm | Checks that `ConcurrentHashMapBuilder`, `HashMapBuilder`, `LinkedHashMapBuilder` or `TreeMapBuilder` is used when possible. |
PortletURLBuilderCheck | .java, .jsp, .jspf, .jspx, .tag, .tpl or .vm | Checks that `PortletURLBuilder` is used when possible. |
UnicodePropertiesBuilderCheck | .java | Checks that `UnicodePropertiesBuilder` is used when possible. |