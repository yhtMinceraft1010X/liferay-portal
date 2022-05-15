# Checks for .ipynb, .json or .npmbridgerc

Check | Category | Description
----- | -------- | -----------
[JSONDeprecatedPackagesCheck](check/json_deprecated_packages_check.markdown#jsondeprecatedpackagescheck) | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Finds incorrect use of deprecated packages in `package.json` files. |
JSONPackageJSONBNDVersionCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks the version for dependencies in `package.json` files. |
JSONPackageJSONCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks content of `package.json` files. |
JSONPackageJSONDependencyVersionCheck | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Checks the version for dependencies in `package.json` files. |
JSONPackageJSONRedundantDependenciesCheck | [Performance](performance_checks.markdown#performance-checks) | Checks for preventing internal dependencies from being added to `package.json`. |
JSONStylingCheck | [Styling](styling_checks.markdown#styling-checks) | Applies rules to enforce consistency in code style. |
[JSONValidationCheck](check/json_validation_check.markdown#jsonvalidationcheck) | [Bug Prevention](bug_prevention_checks.markdown#bug-prevention-checks) | Validates content of `.json` files. |