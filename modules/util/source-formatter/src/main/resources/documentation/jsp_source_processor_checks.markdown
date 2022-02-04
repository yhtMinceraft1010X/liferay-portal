# Checks for .jsp, .jspf, .jspx, .tag, .tpl or .vm

Check | Category | Description
----- | -------- | -----------
AppendCheck | [Styling](styling_checks.markdown#styling-checks) | Checks instances where literal Strings are appended. |
ArrayCheck | [Performance](performance_checks.markdown#performance-checks) | Checks if performance can be improved by using different mehods that can be used by collections. |
[ArrayTypeStyleCheck](https://checkstyle.sourceforge.io/config_misc.html#ArrayTypeStyle) | [Styling](styling_checks.markdown#styling-checks) | Checks the style of array type definitions. |
[AvoidNestedBlocksCheck](https://checkstyle.sourceforge.io/config_blocks.html#AvoidNestedBlocks) | [Styling](styling_checks.markdown#styling-checks) | Finds nested blocks (blocks that are used freely in the code). |
CamelCaseNameCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks variable names for correct use of `CamelCase`. |
ChainingCheck | [Styling](styling_checks.markdown#styling-checks) | Checks that chaining is only applied on certain types and methods. |
CompanyIterationCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that `CompanyLocalService.forEachCompany` or `CompanyLocalService.forEachCompanyId` is used when iterating over companies |
CompatClassImportsCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that classes are imported from `compat` modules, when possible. |
ConcatCheck | [Performance](performance_checks.markdown#performance-checks) | Checks for correct use of `StringBundler.concat`. |
ConstantNameCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that variable names of constants follow correct naming rules. |
ContractionsCheck | [Styling](styling_checks.markdown#styling-checks) | Finds contractions in Strings (such as `can't` or `you're`). |
CopyrightCheck | [Styling](styling_checks.markdown#styling-checks) | Validates `copyright` header. |
[DefaultComesLastCheck](https://checkstyle.sourceforge.io/config_coding.html#DefaultComesLast) | [Styling](styling_checks.markdown#styling-checks) | Checks that the `default` is after all the cases in a `switch` statement. |
EmptyCollectionCheck | [Styling](styling_checks.markdown#styling-checks) | Checks that there are no calls to `Collections.EMPTY_LIST`, `Collections.EMPTY_MAP` or `Collections.EMPTY_SET`. |
ExceptionMessageCheck | [Styling](styling_checks.markdown#styling-checks) | Validates messages that are passed to exceptions. |
FactoryCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds cases where `*Factory` should be used when creating new instances of an object. |
GenericTypeCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that generics are always specified to provide compile-time checking and removing the risk of `ClassCastException` during runtime. |
GetterUtilCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases where the default value is passed to `GetterUtil.get*` or `ParamUtil.get*`. |
IfStatementCheck | [Styling](styling_checks.markdown#styling-checks) | Finds empty if-statements and consecutive if-statements with identical bodies |
InstanceofOrderCheck | [Styling](styling_checks.markdown#styling-checks) | Check the order of `instanceof` calls. |
JSONNamingCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks if variable names follow naming conventions. |
JSONUtilCheck | [Styling](styling_checks.markdown#styling-checks) | Checks for utilization of class `JSONUtil`. |
JSPArrowFunctionCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that there are no array functions. |
JSPCoreTaglibCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases where a `c:choose` or `c:if` tag can be used instead of an if-statement. |
JSPDefineObjectsCheck | [Performance](performance_checks.markdown#performance-checks) | Checks for unnesecarry duplication of code that already exists in `defineObjects`. |
JSPEmptyLinesCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing and unnecessary empty lines. |
JSPExceptionOrderCheck | [Styling](styling_checks.markdown#styling-checks) | Checks the order of exceptions in `.jsp` files. |
JSPFileNameCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks if the file name of `.jsp` or `.jspf` follows the naming conventions. |
JSPFunctionNameCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Check if the names of functions in `.jsp` files follow naming conventions. |
JSPIllegalSyntaxCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds incorrect use of `System.out.print`, `console.log` or `debugger.*` in `.jsp` files. |
JSPImportsCheck | [Styling](styling_checks.markdown#styling-checks) | Sorts and groups imports in `.jsp` files. |
JSPIncludeCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates values of `include` in `.jsp` files. |
JSPIndentationCheck | [Styling](styling_checks.markdown#styling-checks) | Finds incorrect indentation in `.jsp` files. |
JSPInlineVariableCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases where variables can be inlined. |
JSPJavaParserCheck | [Styling](styling_checks.markdown#styling-checks) | Performs JavaParser on `.java` files. |
JSPLanguageKeysCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds missing language keys in `Language.properties`. |
JSPLanguageUtilCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds cases where Locale is passed to `LanguageUtil.get` instead of `HttpServletRequest`. |
JSPLineBreakCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing and unnecessary line breaks in `.jsp` lines. |
JSPLogFileNameCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates the value that is passed to `LogFactoryUtil.getLog` in `.jsp`. |
JSPMethodCallsCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that type `LiferayPortletResponse` is used to call `getNamespace()`. |
JSPMissingTaglibsCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for missing taglibs. |
JSPModuleIllegalImportsCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Finds incorrect use of `com.liferay.registry.Registry` or `com.liferay.util.ContentUtil`. |
JSPParenthesesCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Finds incorrect use of parentheses in statement. |
JSPRedirectBackURLCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Validates values of variable `redirect`. |
JSPSendRedirectCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that there are no calls to `HttpServletResponse.sendRedirect` from `jsp` files. |
JSPServiceUtilCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Finds incorrect use of `*ServiceUtil` in `.jsp` files in modules. |
JSPSessionKeysCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that messages send to `SessionsErrors` or `SessionMessages` follow naming conventions. |
JSPStylingCheck | [Styling](styling_checks.markdown#styling-checks) | Applies rules to enforce consisteny in code style. |
JSPTagAttributesCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Performs several checks on tag attributes. |
JSPTaglibVariableCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks if variable names follow naming conventions. |
JSPUnusedJSPFCheck | [Performance](performance_checks.markdown#performance-checks) | Finds `.jspf` files that are not used. |
JSPUnusedTermsCheck | [Performance](performance_checks.markdown#performance-checks) | Finds taglibs, variables and imports that are unused. |
JSPUpgradeRemovedTagsCheck | [Upgrade](upgrade_checks.markdown#upgrade-checks) | Finds removed tags when upgrading. |
JSPVarNameCheck | [Styling](styling_checks.markdown#styling-checks) | Checks that values of attribute `var` follow naming conventions. |
JSPVariableOrderCheck | [Styling](styling_checks.markdown#styling-checks) | Checks if variable names are in alphabetical order. |
JSPWhitespaceCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing and unnecessary whitespace in `.jsp` files. |
JSPXSSVulnerabilitiesCheck | [Security](security_checks.markdown#security-checks) | Finds xss vulnerabilities. |
LambdaCheck | [Styling](styling_checks.markdown#styling-checks) | Checks that `lambda` statements are as simple as possible. |
ListUtilCheck | [Styling](styling_checks.markdown#styling-checks) | Checks for utilization of class `ListUtil`. |
LiteralStringEqualsCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases where `Objects.equals` should be used. |
[LocalFinalVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#LocalFinalVariableName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that local final variable names conform to a specified pattern. |
[LocalVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#LocalVariableName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that local, non-final variable names conform to a specified pattern. |
LocaleUtilCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds cases where `com.liferay.portal.kernel.util.LocaleUtil` should be used (instead of `java.util.Locale`). |
LogParametersCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates the values of parameters passed to `_log.*` calls. |
MapBuilderCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Checks that `ConcurrentHashMapBuilder`, `HashMapBuilder`, `LinkedHashMapBuilder` or `TreeMapBuilder` is used when possible. |
MapIterationCheck | [Performance](performance_checks.markdown#performance-checks) | Checks that there are no unnecessary map iterations. |
[MemberNameCheck](https://checkstyle.sourceforge.io/config_naming.html#MemberName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that instance variable names conform to a specified pattern. |
MethodCallsOrderCheck | [Styling](styling_checks.markdown#styling-checks) | Sorts method calls for certain object (for example, `put` calls in `java.util.HashMap`). |
[MethodNameCheck](https://checkstyle.sourceforge.io/config_naming.html#MethodName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that method names conform to a specified pattern. |
MethodNamingCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that method names follow naming conventions. |
[MethodParamPadCheck](https://checkstyle.sourceforge.io/config_whitespace.html#MethodParamPad) | [Styling](styling_checks.markdown#styling-checks) | Checks the padding between the identifier of a method definition, constructor definition, method call, or constructor invocation; and the left parenthesis of the parameter list. |
MissingDiamondOperatorCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for missing diamond operator for types that require diamond operator. |
MissingEmptyLineCheck | [Styling](styling_checks.markdown#styling-checks) | Checks for missing line breaks around variable declarations. |
MissingParenthesesCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing parentheses in conditional statement. |
[ModifierOrderCheck](https://checkstyle.sourceforge.io/config_modifier.html#ModifierOrder) | [Styling](styling_checks.markdown#styling-checks) | Checks that the order of modifiers conforms to the suggestions in the Java Language specification, § 8.1.1, 8.3.1, 8.4.3 and 9.4. |
[MultipleVariableDeclarationsCheck](https://checkstyle.sourceforge.io/config_coding.html#MultipleVariableDeclarations) | [Styling](styling_checks.markdown#styling-checks) | Checks that each variable declaration is in its own statement and on its own line. |
NestedIfStatementCheck | [Styling](styling_checks.markdown#styling-checks) | Finds nested if statements that can be combined. |
[NoLineWrapCheck](https://checkstyle.sourceforge.io/config_whitespace.html#NoLineWrap) | [Styling](styling_checks.markdown#styling-checks) | Checks that chosen statements are not line-wrapped. |
[NoWhitespaceAfterCheck](https://checkstyle.sourceforge.io/config_whitespace.html#NoWhitespaceAfter) | [Styling](styling_checks.markdown#styling-checks) | Checks that there is no whitespace after a token. |
[NoWhitespaceBeforeCheck](https://checkstyle.sourceforge.io/config_whitespace.html#NoWhitespaceBefore) | [Styling](styling_checks.markdown#styling-checks) | Checks that there is no whitespace before a token. |
NumberSuffixCheck | [Styling](styling_checks.markdown#styling-checks) | Verifies that uppercase `D`, `F`, or `L` is used when denoting Double/Float/Long. |
[OneStatementPerLineCheck](https://checkstyle.sourceforge.io/config_coding.html#OneStatementPerLine) | [Styling](styling_checks.markdown#styling-checks) | Checks that there is only one statement per line. |
OperatorOperandCheck | [Styling](styling_checks.markdown#styling-checks) | Verifies that operand do not go over too many lines and make the operator hard to read. |
[OperatorWrapCheck](https://checkstyle.sourceforge.io/config_whitespace.html#OperatorWrap) | [Styling](styling_checks.markdown#styling-checks) | Checks the policy on how to wrap lines on operators. |
[ParameterNameCheck](https://checkstyle.sourceforge.io/config_naming.html#ParameterName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that method parameter names conform to a specified pattern. |
ParsePrimitiveTypeCheck | [Performance](performance_checks.markdown#performance-checks) | Verifies that `GetterUtil.parse*` is used to parse primitive types, when possible. |
PersistenceCallCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds illegal persistence calls across component boundaries. |
PlusStatementCheck | [Styling](styling_checks.markdown#styling-checks) | Performs several checks to statements where `+` is used for concatenation. |
PortletURLBuilderCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Checks that `PortletURLBuilder` is used when possible. |
PrimitiveWrapperInstantiationCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds cases where `new Type` is used for primitive types (use `Type.valueOf` instead). |
PrincipalExceptionCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds calls to `PrincipalException.class.getName()` (use `PrincipalException.getNestedClasses()` instead). |
ResourceBundleCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that there are no calls to `java.util.ResourceBundle.getBundle`. |
SemiColonCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases of unnecessary semicolon. |
SetUtilMethodsCheck | [Performance](performance_checks.markdown#performance-checks) | Finds cases of inefficient SetUtil operations. |
SizeIsZeroCheck | [Styling](styling_checks.markdown#styling-checks) | Finds cases of calls like `list.size() == 0` (use `list.isEmpty()` instead). |
[StaticVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#StaticVariableName) | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks that static, non-final variable names conform to a specified pattern. |
StringBundlerNamingCheck | [Naming Conventions](naming_conventions_checks.markdown#naming-conventions-checks) | Checks for consistent naming on variables of type 'StringBundler'. |
StringCastCheck | [Performance](performance_checks.markdown#performance-checks) | Finds cases where a redundant `toString()` is called on variable type `String`. |
[StringLiteralEqualityCheck](https://checkstyle.sourceforge.io/config_coding.html#StringLiteralEquality) | [Styling](styling_checks.markdown#styling-checks) | Checks that string literals are not used with == or !=. |
StringMethodsCheck | [Performance](performance_checks.markdown#performance-checks) | Checks if performance can be improved by using different String operation methods. |
SubstringCheck | [Performance](performance_checks.markdown#performance-checks) | Finds cases like `s.substring(1, s.length())` (use `s.substring(1)` instead). |
TernaryOperatorCheck | [Styling](styling_checks.markdown#styling-checks) | Finds use of ternary operator in `java` files (use if statement instead). |
[UnnecessaryParenthesesCheck](https://checkstyle.sourceforge.io/config_coding.html#UnnecessaryParentheses) | [Styling](styling_checks.markdown#styling-checks) | Checks if unnecessary parentheses are used in a statement or expression. |
UnnecessaryTypeCastCheck | [Performance](performance_checks.markdown#performance-checks) | Finds unnecessary Type Casting. |
UnparameterizedClassCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds `Class` instantation without generic type. |
ValidatorEqualsCheck | [Performance](performance_checks.markdown#performance-checks) | Checks that there are no calls to `Validator.equals(Object, Object)`. |
ValidatorIsNullCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Ensures that only variable of type `Long`, `Serializable` or `String` is passed to method `com.liferay.portal.kernel.util.Validator.isNull`. |
[WhitespaceAfterCheck](https://checkstyle.sourceforge.io/config_whitespace.html#WhitespaceAfter) | [Styling](styling_checks.markdown#styling-checks) | Checks that a token is followed by whitespace, with the exception that it does not check for whitespace after the semicolon of an empty for iterator. |
[WhitespaceAroundCheck](https://checkstyle.sourceforge.io/config_whitespace.html#WhitespaceAround) | [Styling](styling_checks.markdown#styling-checks) | Checks that a token is surrounded by whitespace. |