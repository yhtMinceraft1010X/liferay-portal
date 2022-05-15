# Source Formatter

## Configuration

### Configuration Files

Checks are configured in the following files:
- [checkstyle.xml](src/main/resources/checkstyle.xml)
- [checkstyle-jsp.xml](src/main/resources/checkstyle-jsp.xml)
- [sourcechecks.xml](src/main/resources/sourcechecks.xml)

### Excluding Files or Directories

1. Exclude a **single file** from **one specific check**

   Use `checkstyle-suppressions.xml`:

   ```
   <suppressions>
       <checkstyle>
           <suppress checks="UnusedVariableCheck" files="portal-kernel/src/com/liferay/portal/kernel/util/PortalUtil\.java" />
       </checkstyle>
       <source-check>
           <suppress checks="JavaModuleIllegalImportsCheck" files="portal-kernel/src/com/liferay/portal/kernel/util/PortalUtil\.java" />
       </source-check>
   </suppressions>
   ```

1. Exclude a **single file** from **all checks**

   Use `source-formatter.properties#source.formatter.excludes`:

   ```
   source.formatter.excludes=portal-kernel/src/com/liferay/portal/kernel/util/PortalUtil.java
   ```

1. Exclude **all files in a directory** from **one specific check**

   Use `checkstyle-suppressions.xml`:

   ```
   <suppressions>
       <checkstyle>
           <suppress checks="UnusedVariableCheck" files="portal-kernel/src/com/liferay/portal/kernel/util/.*" />
       </checkstyle>
       <source-check>
           <suppress checks="JavaModuleIllegalImportsCheck" files="portal-kernel/src/com/liferay/portal/kernel/util/.*" />
       </source-check>
   </suppressions>
   ```

1. Exclude a **all files in a directory** from **all checks**

   - Use `source-formatter.properties#source.formatter.excludes`:

      ```
      source.formatter.excludes=portal-kernel/src/com/liferay/portal/kernel/util/**
      ```

   - Add (empty) file `source_formatter.ignore` in the directory

1. Exclude **all files in the project** from **one specific check**

   - Use `checkstyle-suppressions.xml`:

      ```
      <suppressions>
          <checkstyle>
              <suppress checks="UnusedVariableCheck" />
          </checkstyle>
          <source-check>
              <suppress checks="JavaModuleIllegalImportsCheck" />
          </source-check>
      </suppressions>
      ```

   - Use property `enabled` in `source-formatter.properties`:

   ```
   checkstyle.UnusedVariableCheck.enabled=false
   source.check.JavaModuleIllegalImportsCheck.enabled=false
   ```

## Checks

- ### [All Checks](src/main/resources/documentation/all_checks.markdown#all-checks)

- ### By Category:
   - [Bug Prevention](src/main/resources/documentation/bug_prevention_checks.markdown#bug-prevention-checks)
   - [Documentation](src/main/resources/documentation/documentation_checks.markdown#documentation-checks)
   - [Javadoc](src/main/resources/documentation/javadoc_checks.markdown#javadoc-checks)
   - [Miscellaneous](src/main/resources/documentation/miscellaneous_checks.markdown#miscellaneous-checks)
   - [Naming Conventions](src/main/resources/documentation/naming_conventions_checks.markdown#naming-conventions-checks)
   - [Performance](src/main/resources/documentation/performance_checks.markdown#performance-checks)
   - [Security](src/main/resources/documentation/security_checks.markdown#security-checks)
   - [Styling](src/main/resources/documentation/styling_checks.markdown#styling-checks)
   - [Upgrade](src/main/resources/documentation/upgrade_checks.markdown#upgrade-checks)

- ### By File Extensions:
   - [.action, .function, .jrxml, .macro, .pom, .project, .properties, .svg, .testcase, .toggle, .tpl, .wsdl, .xml or .xsd](src/main/resources/documentation/xml_source_processor_checks.markdown#checks-for-action-function-jrxml-macro-pom-project-properties-svg-testcase-toggle-tpl-wsdl-xml-or-xsd)
   - [.bnd](src/main/resources/documentation/bnd_source_processor_checks.markdown#checks-for-bnd)
   - [.bndrun](src/main/resources/documentation/bnd_run_source_processor_checks.markdown#checks-for-bndrun)
   - [.cfg or .config](src/main/resources/documentation/config_source_processor_checks.markdown#checks-for-cfg-or-config)
   - [.cql](src/main/resources/documentation/cql_source_processor_checks.markdown#checks-for-cql)
   - [.css or .scss](src/main/resources/documentation/css_source_processor_checks.markdown#checks-for-css-or-scss)
   - [.dtd](src/main/resources/documentation/dtd_source_processor_checks.markdown#checks-for-dtd)
   - [.eslintignore, .prettierignore or .properties](src/main/resources/documentation/properties_source_processor_checks.markdown#checks-for-eslintignore-prettierignore-or-properties)
   - [.expect or .sh](src/main/resources/documentation/sh_source_processor_checks.markdown#checks-for-expect-or-sh)
   - [.ftl](src/main/resources/documentation/ftl_source_processor_checks.markdown#checks-for-ftl)
   - [.function, .jar, .lar, .macro, .path, .testcase, .war or .zip](src/main/resources/documentation/poshi_source_processor_checks.markdown#checks-for-function-jar-lar-macro-path-testcase-war-or-zip)
   - [.gradle](src/main/resources/documentation/gradle_source_processor_checks.markdown#checks-for-gradle)
   - [.groovy](src/main/resources/documentation/groovy_source_processor_checks.markdown#checks-for-groovy)
   - [.html or .path](src/main/resources/documentation/html_source_processor_checks.markdown#checks-for-html-or-path)
   - [.ipynb, .json or .npmbridgerc](src/main/resources/documentation/json_source_processor_checks.markdown#checks-for-ipynb-json-or-npmbridgerc)
   - [.java](src/main/resources/documentation/java_source_processor_checks.markdown#checks-for-java)
   - [.js or .jsx](src/main/resources/documentation/js_source_processor_checks.markdown#checks-for-js-or-jsx)
   - [.jsp, .jspf, .jspx, .tag, .tpl or .vm](src/main/resources/documentation/jsp_source_processor_checks.markdown#checks-for-jsp-jspf-jspx-tag-tpl-or-vm)
   - [.lfrbuild-*](src/main/resources/documentation/lfr_build_source_processor_checks.markdown#checks-for-lfrbuild)
   - [.markdown or .md](src/main/resources/documentation/markdown_source_processor_checks.markdown#checks-for-markdown-or-md)
   - [.py](src/main/resources/documentation/python_source_processor_checks.markdown#checks-for-py)
   - [.soy](src/main/resources/documentation/soy_source_processor_checks.markdown#checks-for-soy)
   - [.sql](src/main/resources/documentation/sql_source_processor_checks.markdown#checks-for-sql)
   - [.tf](src/main/resources/documentation/tf_source_processor_checks.markdown#checks-for-tf)
   - [.tld](src/main/resources/documentation/tld_source_processor_checks.markdown#checks-for-tld)
   - [.ts or .tsx](src/main/resources/documentation/ts_source_processor_checks.markdown#checks-for-ts-or-tsx)
   - [.txt](src/main/resources/documentation/txt_source_processor_checks.markdown#checks-for-txt)
   - [.yaml or .yml](src/main/resources/documentation/yml_source_processor_checks.markdown#checks-for-yaml-or-yml)
   - [CODEOWNERS](src/main/resources/documentation/codeowners_source_processor_checks.markdown#checks-for-codeowners)
   - [Dockerfile](src/main/resources/documentation/dockerfile_source_processor_checks.markdown#checks-for-dockerfile)
   - [packageinfo](src/main/resources/documentation/packageinfo_source_processor_checks.markdown#checks-for-packageinfo)