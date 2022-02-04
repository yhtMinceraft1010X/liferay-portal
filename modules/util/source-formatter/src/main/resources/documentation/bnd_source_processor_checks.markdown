# Checks for .bnd

Check | Category | Description
----- | -------- | -----------
BNDBundleActivatorCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates property value for `Bundle-Activator`. |
BNDBundleCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates `Liferay-Releng-*` properties. |
BNDBundleInformationCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates property values for `Bundle-Version`, `Bundle-Name` and `Bundle-SymbolicName`. |
BNDCapabilityCheck | [Styling](styling_checks.markdown#styling-checks) | Sorts and applies logic to fix line breaks to property values for `Provide-Capability` and `Require-Capability`. |
BNDDefinitionKeysCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates definition keys in `.bnd` files. |
BNDDeprecatedAppBNDsCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Checks for redundant `app.bnd` in deprecated or archived modules. |
BNDDirectoryNameCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks if the directory names of the submodules match the parent module name. |
BNDExportsCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks that modules not ending with `-api`, `-client`, `-spi`, `-tablig`, `-test-util` do not export packages. |
BNDImportsCheck | [Styling](styling_checks.markdown#styling-checks) | Sorts class names and checks for use of wildcards in property values for `-conditionalpackage`, `-exportcontents` and `Export-Package`. |
BNDIncludeResourceCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for unnesecarry including of `test-classes/integration`. |
BNDLiferayEnterpriseAppCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for correct use of property `Liferay-Enterprise-App`. |
BNDLiferayRelengBundleCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks if `.lfrbuild-release-src` file exists for DXP module with `Liferay-Releng-Bundle: true` |
BNDLiferayRelengCategoryCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates `Liferay-Releng-Category` properties |
BNDLineBreaksCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing and unnecessary line breaks in `.bnd` files. |
BNDMultipleAppBNDsCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for duplicate `app.bnd` (when both `/apps/` and `/apps/dxp/` contain the same module). |
BNDRangeCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for use or range expressions. |
BNDSchemaVersionCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks for incorrect use of property `Liferay-Require-SchemaVersion`. |
BNDStylingCheck | [Styling](styling_checks.markdown#styling-checks) | Applies rules to enforce consisteny in code style. |
BNDSuiteCheck | [Miscellaneous](miscellaneous_checks.markdown#miscellaneous-checks) | Checks that deprecated apps are moved to the `archived` folder. |
BNDWebContextPathCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks if the property value for `Web-ContextPath` matches the module directory. |
BNDWhitespaceCheck | [Styling](styling_checks.markdown#styling-checks) | Finds missing and unnecessary whitespace in `.bnd` files. |