/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.source.formatter.checkstyle.check;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.check.util.JavaSourceUtil;
import com.liferay.source.formatter.parser.JavaClass;
import com.liferay.source.formatter.parser.JavaClassParser;
import com.liferay.source.formatter.parser.JavaMethod;
import com.liferay.source.formatter.parser.JavaSignature;
import com.liferay.source.formatter.parser.JavaTerm;
import com.liferay.source.formatter.util.FileUtil;
import com.liferay.source.formatter.util.SourceFormatterUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hugo Huijser
 */
public class ChainingCheck extends BaseCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {
			TokenTypes.CLASS_DEF, TokenTypes.ENUM_DEF, TokenTypes.INTERFACE_DEF,
			TokenTypes.LITERAL_NEW, TokenTypes.RPAREN
		};
	}

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		if (detailAST.getType() == TokenTypes.LITERAL_NEW) {
			_checkChainingOnNewInstance(detailAST);

			return;
		}

		if (detailAST.getType() == TokenTypes.RPAREN) {
			_checkChainingOnParentheses(detailAST);
		}

		DetailAST parentDetailAST = detailAST.getParent();

		if (parentDetailAST != null) {
			return;
		}

		_checkChainingOnMethodCalls(detailAST);
	}

	private void _checkAllowedChaining(DetailAST methodCallDetailAST) {
		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (true) {
			if ((parentDetailAST.getType() == TokenTypes.DOT) ||
				(parentDetailAST.getType() == TokenTypes.METHOD_CALL)) {

				parentDetailAST = parentDetailAST.getParent();

				continue;
			}

			break;
		}

		if (parentDetailAST.getType() != TokenTypes.EXPR) {
			return;
		}

		parentDetailAST = parentDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.ASSIGN) {
			return;
		}

		parentDetailAST = parentDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.VARIABLE_DEF) {
			return;
		}

		String classOrVariableName = getClassOrVariableName(
			methodCallDetailAST);

		if (!Objects.equals(classOrVariableName, "Optional") &&
			!Objects.equals(classOrVariableName, "Stream") &&
			!Objects.equals(classOrVariableName, "Try")) {

			return;
		}

		DetailAST variableNameDetailAST = parentDetailAST.findFirstToken(
			TokenTypes.IDENT);

		String variableName = variableNameDetailAST.getText();

		if (!classOrVariableName.equals(
				getVariableTypeName(
					methodCallDetailAST, variableName, false))) {

			return;
		}

		List<DetailAST> identDetailASTList = _getIdentDetailASTList(
			parentDetailAST.getParent(), variableName);

		if ((identDetailASTList.size() == 2) &&
			Objects.equals(
				_findFirstParent(
					identDetailASTList.get(0), TokenTypes.ELIST,
					TokenTypes.LITERAL_IF),
				_findFirstParent(
					identDetailASTList.get(1), TokenTypes.ELIST,
					TokenTypes.LITERAL_IF))) {

			log(
				methodCallDetailAST, _MSG_ALLOWED_CHAINING,
				StringBundler.concat(
					classOrVariableName, StringPool.PERIOD,
					getMethodName(methodCallDetailAST)));
		}
	}

	private void _checkChainingOnMethodCalls(DetailAST detailAST) {
		List<DetailAST> methodCallDetailASTList = getAllChildTokens(
			detailAST, true, TokenTypes.METHOD_CALL);

		for (DetailAST methodCallDetailAST : methodCallDetailASTList) {
			DetailAST dotDetailAST = methodCallDetailAST.findFirstToken(
				TokenTypes.DOT);

			if (dotDetailAST != null) {
				List<DetailAST> childMethodCallDetailASTList =
					getAllChildTokens(
						dotDetailAST, false, TokenTypes.METHOD_CALL);

				// Only check the method that is first in the chain

				if (!childMethodCallDetailASTList.isEmpty()) {
					continue;
				}
			}

			_checkAllowedChaining(methodCallDetailAST);

			ChainInformation chainInformation = getChainInformation(
				methodCallDetailAST);

			List<String> chainedMethodNames = chainInformation.getMethodNames();

			_checkRequiredChaining(methodCallDetailAST, chainedMethodNames);

			int chainSize = chainedMethodNames.size();

			if (chainSize == 1) {
				continue;
			}

			if (chainSize == 2) {
				DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
					TokenTypes.ELIST);

				if ((elistDetailAST.getChildCount() == 0) &&
					(dotDetailAST == null)) {

					continue;
				}

				_checkMethodName(
					chainedMethodNames, "getClass", methodCallDetailAST);

				if ((isAttributeValue(_ALLOW_CONCAT_CHAIN_KEY) ||
					 isExcludedPath(RUN_OUTSIDE_PORTAL_EXCLUDES)) &&
					Objects.equals(chainedMethodNames.get(0), "concat") &&
					Objects.equals(chainedMethodNames.get(1), "concat")) {

					continue;
				}
			}

			if (chainSize > 3) {
				_checkChainOrder(methodCallDetailAST, chainedMethodNames);
			}

			if (_isAllowedChainingMethodCall(
					methodCallDetailAST, chainInformation, detailAST)) {

				continue;
			}

			int concatsCount = Collections.frequency(
				chainedMethodNames, "concat");

			if ((chainSize == 3) && (concatsCount == 2) &&
				isAttributeValue(_ALLOW_CONCAT_CHAIN_KEY)) {

				continue;
			}

			if ((concatsCount > 1) &&
				!isExcludedPath(RUN_OUTSIDE_PORTAL_EXCLUDES)) {

				log(methodCallDetailAST, _MSG_AVOID_TOO_MANY_CONCAT);
			}
			else {
				log(
					methodCallDetailAST, _MSG_AVOID_METHOD_CHAINING,
					getMethodName(methodCallDetailAST));
			}
		}
	}

	private void _checkChainingOnNewInstance(DetailAST detailAST) {
		DetailAST dotDetailAST = detailAST.getParent();

		if (dotDetailAST.getType() != TokenTypes.DOT) {
			return;
		}

		DetailAST methodCallDetailAST = dotDetailAST.getParent();

		if ((methodCallDetailAST.getType() != TokenTypes.METHOD_CALL) ||
			(detailAST.findFirstToken(TokenTypes.ARRAY_DECLARATOR) != null) ||
			(detailAST.findFirstToken(TokenTypes.OBJBLOCK) != null) ||
			_isAllowedChainingMethodCall(
				methodCallDetailAST, getChainInformation(methodCallDetailAST),
				detailAST)) {

			return;
		}

		log(methodCallDetailAST, _MSG_AVOID_NEW_INSTANCE_CHAINING);
	}

	private void _checkChainingOnParentheses(DetailAST detailAST) {
		if (_isInsideConstructorThisCall(detailAST) ||
			hasParentWithTokenType(detailAST, TokenTypes.SUPER_CTOR_CALL)) {

			return;
		}

		DetailAST parentDetailAST = detailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.DOT) {
			return;
		}

		DetailAST previousSiblingDetailAST = detailAST.getPreviousSibling();

		if (previousSiblingDetailAST.getType() != TokenTypes.TYPECAST) {
			log(detailAST, _MSG_AVOID_PARENTHESES_CHAINING);
		}
		else if (isAttributeValue(_APPLY_TO_TYPE_CAST_KEY)) {
			log(detailAST, _MSG_AVOID_TYPE_CAST_CHAINING);
		}
	}

	private void _checkChainOrder(
		DetailAST methodCallDetailAST, List<String> chainedMethodNames) {

		if (!Objects.equals(chainedMethodNames.get(0), "status") ||
			!Objects.equals(
				chainedMethodNames.get(chainedMethodNames.size() - 1),
				"build") ||
			!Objects.equals(
				getClassOrVariableName(methodCallDetailAST), "Response")) {

			return;
		}

		List<String> middleMethodNames = chainedMethodNames.subList(
			1, chainedMethodNames.size() - 1);

		String unsortedNames = middleMethodNames.toString();

		Collections.sort(middleMethodNames);

		if (!unsortedNames.equals(middleMethodNames.toString())) {
			log(methodCallDetailAST, _MSG_UNSORTED_RESPONSE);
		}
	}

	private void _checkMethodName(
		List<String> chainedMethodNames, String methodName,
		DetailAST methodCallDetailAST) {

		String firstMethodName = chainedMethodNames.get(0);

		if (firstMethodName.equals(methodName) &&
			!_isInsideConstructorThisCall(methodCallDetailAST) &&
			!hasParentWithTokenType(
				methodCallDetailAST, TokenTypes.SUPER_CTOR_CALL)) {

			log(methodCallDetailAST, _MSG_AVOID_METHOD_CHAINING, methodName);
		}
	}

	private void _checkRequiredChaining(
		DetailAST methodCallDetailAST, List<String> chainedMethodNames) {

		String classOrVariableName = getClassOrVariableName(
			methodCallDetailAST);

		if (classOrVariableName == null) {
			return;
		}

		String variableTypeName = getVariableTypeName(
			methodCallDetailAST, classOrVariableName, false);

		String fullyQualifiedClassName = variableTypeName;

		for (String importName : getImportNames(methodCallDetailAST)) {
			if (importName.endsWith("." + variableTypeName)) {
				fullyQualifiedClassName = importName;

				break;
			}
		}

		List<String> requiredChainingMethodNames =
			_getRequiredChainingMethodNames(fullyQualifiedClassName);

		if (requiredChainingMethodNames == null) {
			return;
		}

		String methodName = chainedMethodNames.get(
			chainedMethodNames.size() - 1);

		if (!requiredChainingMethodNames.contains(methodName)) {
			return;
		}

		DetailAST topLevelMethodCallDetailAST = methodCallDetailAST;

		while (true) {
			DetailAST parentDetailAST = topLevelMethodCallDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.DOT) {
				break;
			}

			parentDetailAST = parentDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.METHOD_CALL) {
				break;
			}

			topLevelMethodCallDetailAST = parentDetailAST;
		}

		DetailAST parentDetailAST = topLevelMethodCallDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.EXPR) {
			return;
		}

		DetailAST nextSiblingDetailAST = parentDetailAST.getNextSibling();

		if ((nextSiblingDetailAST == null) ||
			(nextSiblingDetailAST.getType() != TokenTypes.SEMI)) {

			return;
		}

		nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();

		if ((nextSiblingDetailAST == null) ||
			(nextSiblingDetailAST.getType() != TokenTypes.EXPR)) {

			return;
		}

		DetailAST nextMethodCallDetailAST =
			nextSiblingDetailAST.getFirstChild();

		if (nextMethodCallDetailAST.getType() != TokenTypes.METHOD_CALL) {
			return;
		}

		while (true) {
			DetailAST firstChildDetailAST =
				nextMethodCallDetailAST.getFirstChild();

			if (firstChildDetailAST.getType() != TokenTypes.DOT) {
				break;
			}

			firstChildDetailAST = firstChildDetailAST.getFirstChild();

			if (firstChildDetailAST.getType() != TokenTypes.METHOD_CALL) {
				break;
			}

			nextMethodCallDetailAST = firstChildDetailAST;
		}

		if (classOrVariableName.equals(
				getClassOrVariableName(nextMethodCallDetailAST)) &&
			!Objects.equals(getMethodName(nextMethodCallDetailAST), "remove")) {

			log(
				methodCallDetailAST, _MSG_REQUIRED_CHAINING,
				classOrVariableName + "." + methodName);
		}
	}

	private DetailAST _findFirstParent(DetailAST detailAST, int... types) {
		DetailAST parentDetailAST = detailAST.getParent();

		while (true) {
			if (parentDetailAST == null) {
				return null;
			}

			if (ArrayUtil.contains(types, parentDetailAST.getType())) {
				return parentDetailAST;
			}

			parentDetailAST = parentDetailAST.getParent();
		}
	}

	private DetailAST _getGlobalVariableDefinitonDetailAST(
		DetailAST methodCallDetailAST) {

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (true) {
			if ((parentDetailAST == null) ||
				(parentDetailAST.getType() == TokenTypes.CTOR_DEF) ||
				(parentDetailAST.getType() == TokenTypes.METHOD_DEF)) {

				return null;
			}

			if (parentDetailAST.getType() == TokenTypes.VARIABLE_DEF) {
				DetailAST grandParentDetailAST = parentDetailAST.getParent();

				if (grandParentDetailAST.getType() == TokenTypes.OBJBLOCK) {
					return parentDetailAST;
				}

				return null;
			}

			parentDetailAST = parentDetailAST.getParent();
		}
	}

	private List<DetailAST> _getIdentDetailASTList(
		DetailAST detailAST, String name) {

		List<DetailAST> identDetailASTList = getAllChildTokens(
			detailAST, true, TokenTypes.IDENT);

		Stream<DetailAST> stream = identDetailASTList.stream();

		return stream.filter(
			identDetailAST -> StringUtil.equals(name, identDetailAST.getText())
		).collect(
			Collectors.toList()
		);
	}

	private JavaClass _getJavaClass(String requiredChainingClassFileName) {
		File file = SourceFormatterUtil.getFile(
			getBaseDirName(), requiredChainingClassFileName, getMaxDirLevel());

		try {
			if (file != null) {
				return JavaClassParser.parseJavaClass(
					requiredChainingClassFileName, FileUtil.read(file));
			}

			String portalBranchName = getAttributeValue(
				SourceFormatterUtil.GIT_LIFERAY_PORTAL_BRANCH);

			if (Validator.isNull(portalBranchName)) {
				return null;
			}

			URL url = new URL(
				StringBundler.concat(
					SourceFormatterUtil.GIT_LIFERAY_PORTAL_URL,
					portalBranchName, StringPool.SLASH,
					requiredChainingClassFileName));

			return JavaClassParser.parseJavaClass(
				requiredChainingClassFileName,
				StringUtil.read(url.openStream()));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return null;
		}
	}

	private List<String> _getRequiredChainingMethodNames(
		String fullyQualifiedClassName) {

		if (_requiredChainingMethodNamesMap != null) {
			return _requiredChainingMethodNamesMap.get(fullyQualifiedClassName);
		}

		_requiredChainingMethodNamesMap = new HashMap<>();

		List<String> requiredChainingClassFileNames = getAttributeValues(
			_REQUIRED_CHAINING_CLASS_FILE_NAMES_KEY);

		for (String requiredChainingClassFileName :
				requiredChainingClassFileNames) {

			JavaClass javaClass = _getJavaClass(requiredChainingClassFileName);

			if (javaClass == null) {
				continue;
			}

			List<String> requiredChainingMethodNames = new ArrayList<>();

			for (JavaTerm javaTerm : javaClass.getChildJavaTerms()) {
				if (!(javaTerm instanceof JavaMethod)) {
					continue;
				}

				JavaMethod javaMethod = (JavaMethod)javaTerm;

				if (!javaMethod.isPublic()) {
					continue;
				}

				JavaSignature javaSignature = javaMethod.getSignature();

				if (Objects.equals(
						javaClass.getName(), javaSignature.getReturnType())) {

					requiredChainingMethodNames.add(javaMethod.getName());
				}
			}

			_requiredChainingMethodNamesMap.put(
				javaClass.getPackageName() + "." + javaClass.getName(),
				requiredChainingMethodNames);
		}

		return _requiredChainingMethodNamesMap.get(fullyQualifiedClassName);
	}

	private String _getReturnType(
		String methodName, DetailAST classDefinitionDetailAST) {

		List<DetailAST> methodDefinitionDetailASTList = getAllChildTokens(
			classDefinitionDetailAST, true, TokenTypes.METHOD_DEF);

		for (DetailAST methodDefinitionDetailAST :
				methodDefinitionDetailASTList) {

			DetailAST nameDetailAST = methodDefinitionDetailAST.findFirstToken(
				TokenTypes.IDENT);

			if (methodName.equals(nameDetailAST.getText())) {
				return getTypeName(methodDefinitionDetailAST, false);
			}
		}

		return null;
	}

	private boolean _isAllowedChainingMethodCall(
		DetailAST methodCallDetailAST, ChainInformation chainInformation,
		DetailAST detailAST) {

		DetailAST globalVariableDefinitonDetailAST =
			_getGlobalVariableDefinitonDetailAST(methodCallDetailAST);

		if ((globalVariableDefinitonDetailAST != null) &&
			((detailAST.getType() != TokenTypes.CLASS_DEF) ||
			 _isInsideInnerClass(
				 globalVariableDefinitonDetailAST, detailAST))) {

			return true;
		}

		if (_isInsideConstructorThisCall(methodCallDetailAST) ||
			hasParentWithTokenType(
				methodCallDetailAST, TokenTypes.SUPER_CTOR_CALL)) {

			return true;
		}

		List<String> chainedMethodNames = chainInformation.getMethodNames();

		List<String> allowedMethodNames = getAttributeValues(
			_ALLOWED_METHOD_NAMES_KEY);

		for (String allowedMethodName : allowedMethodNames) {
			if (chainedMethodNames.contains(allowedMethodName)) {
				return true;
			}
		}

		String returnType = chainInformation.getReturnType();

		if (returnType != null) {
			List<String> allowedVariableTypeNames = getAttributeValues(
				_ALLOWED_VARIABLE_TYPE_NAMES_KEY);

			for (String allowedVariableTypeName : allowedVariableTypeNames) {
				if (returnType.matches(allowedVariableTypeName)) {
					return true;
				}
			}
		}

		String absolutePath = getAbsolutePath();

		if (absolutePath.contains("/test/") ||
			absolutePath.contains("/testIntegration/")) {

			List<String> allowedMockitoMethodNames = getAttributeValues(
				_ALLOWED_MOCKITO_METHOD_NAMES_KEY);

			for (String allowedMockitoMethodName : allowedMockitoMethodNames) {
				if (chainedMethodNames.contains(allowedMockitoMethodName)) {
					return true;
				}
			}
		}

		DetailAST dotDetailAST = methodCallDetailAST.findFirstToken(
			TokenTypes.DOT);

		if (dotDetailAST == null) {
			String className = JavaSourceUtil.getClassName(absolutePath);

			List<String> allowedClassNames = getAttributeValues(
				_ALLOWED_CLASS_NAMES_KEY);

			for (String allowedClassName : allowedClassNames) {
				if (className.matches(allowedClassName)) {
					return true;
				}
			}

			returnType = _getReturnType(chainedMethodNames.get(0), detailAST);

			if (returnType != null) {
				List<String> allowedVariableTypeNames = getAttributeValues(
					_ALLOWED_VARIABLE_TYPE_NAMES_KEY);

				for (String allowedVariableTypeName :
						allowedVariableTypeNames) {

					if (returnType.matches(allowedVariableTypeName)) {
						return true;
					}
				}
			}

			return false;
		}

		String classOrVariableName = getClassOrVariableName(
			methodCallDetailAST);

		if (classOrVariableName != null) {
			if (_isLambdaVariable(methodCallDetailAST, classOrVariableName)) {
				return true;
			}

			List<String> allowedClassNames = getAttributeValues(
				_ALLOWED_CLASS_NAMES_KEY);

			for (String s :
					StringUtil.split(classOrVariableName, CharPool.PERIOD)) {

				for (String allowedClassName : allowedClassNames) {
					if (s.matches("(?i)" + allowedClassName)) {
						return true;
					}
				}
			}

			String variableTypeName = getVariableTypeName(
				methodCallDetailAST, classOrVariableName, false);

			if (Validator.isNotNull(variableTypeName)) {
				List<String> allowedVariableTypeNames = getAttributeValues(
					_ALLOWED_VARIABLE_TYPE_NAMES_KEY);

				for (String allowedVariableTypeName :
						allowedVariableTypeNames) {

					if (variableTypeName.matches(allowedVariableTypeName)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean _isInsideConstructorThisCall(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		while (parentDetailAST != null) {
			String parentDetailASTText = parentDetailAST.getText();

			if ((parentDetailAST.getType() == TokenTypes.CTOR_CALL) &&
				parentDetailASTText.equals("this")) {

				return true;
			}

			parentDetailAST = parentDetailAST.getParent();
		}

		return false;
	}

	private boolean _isInsideInnerClass(
		DetailAST globalVariableDefinitonDetailAST,
		DetailAST outerClassDefinitionDetailAST) {

		DetailAST detailAST = getParentWithTokenType(
			globalVariableDefinitonDetailAST, TokenTypes.CLASS_DEF,
			TokenTypes.ENUM_DEF, TokenTypes.INTERFACE_DEF,
			TokenTypes.LITERAL_NEW);

		if (!detailAST.equals(outerClassDefinitionDetailAST)) {
			return true;
		}

		return false;
	}

	private boolean _isLambdaVariable(
		DetailAST methodCallDetailAST, String variableName) {

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (parentDetailAST != null) {
			if (parentDetailAST.getType() != TokenTypes.LAMBDA) {
				parentDetailAST = parentDetailAST.getParent();

				continue;
			}

			DetailAST nameDetailAST = parentDetailAST.findFirstToken(
				TokenTypes.IDENT);

			if ((nameDetailAST != null) &&
				variableName.equals(nameDetailAST.getText())) {

				return true;
			}

			return false;
		}

		return false;
	}

	private static final String _ALLOW_CONCAT_CHAIN_KEY = "allowConcatChain";

	private static final String _ALLOWED_CLASS_NAMES_KEY = "allowedClassNames";

	private static final String _ALLOWED_METHOD_NAMES_KEY =
		"allowedMethodNames";

	private static final String _ALLOWED_MOCKITO_METHOD_NAMES_KEY =
		"allowedMockitoMethodNames";

	private static final String _ALLOWED_VARIABLE_TYPE_NAMES_KEY =
		"allowedVariableTypeNames";

	private static final String _APPLY_TO_TYPE_CAST_KEY = "applyToTypeCast";

	private static final String _MSG_ALLOWED_CHAINING = "chaining.allowed";

	private static final String _MSG_AVOID_METHOD_CHAINING =
		"chaining.avoid.method";

	private static final String _MSG_AVOID_NEW_INSTANCE_CHAINING =
		"chaining.avoid.new.instance";

	private static final String _MSG_AVOID_PARENTHESES_CHAINING =
		"chaining.avoid.parentheses";

	private static final String _MSG_AVOID_TOO_MANY_CONCAT =
		"concat.avoid.too.many";

	private static final String _MSG_AVOID_TYPE_CAST_CHAINING =
		"chaining.avoid.type.cast";

	private static final String _MSG_REQUIRED_CHAINING = "chaining.required";

	private static final String _MSG_UNSORTED_RESPONSE = "response.unsorted";

	private static final String _REQUIRED_CHAINING_CLASS_FILE_NAMES_KEY =
		"requiredChainingClassFileNames";

	private static final Log _log = LogFactoryUtil.getLog(ChainingCheck.class);

	private Map<String, List<String>> _requiredChainingMethodNamesMap;

}