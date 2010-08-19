package com.rrd.intellij.idea.plugins.gca;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateChainedAccessorsActionHandler extends EditorWriteActionHandler {
	@Override
	public void executeWriteAction(final Editor editor, final DataContext dataContext) {


		IdeaUtil util = ApplicationManager.getApplication().getComponent(IdeaUtil.class);		
		JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(editor.getProject());
		PsiElementFactory psiElementFactory = psiFacade.getElementFactory();
		PsiClass clazz = util.getCurrentClass(editor);




		List<PsiMethod> clazzMethods = Arrays.asList(clazz.getMethods());
		List<String> clazzMethodNames = new ArrayList<String>(clazzMethods.size());
		for (PsiMethod clazzMethod : clazzMethods) {
			clazzMethodNames.add(clazzMethod.getName());
		}
		for (PsiField field : clazz.getFields()) {
			String methodNameSuffix = util.getCapitalizedPropertyName(field);
			String fieldType = field.getType().getCanonicalText();
			String fieldName = field.getName();
			String getterMethodName = "get" + methodNameSuffix;
			if (!clazzMethodNames.contains(getterMethodName)) {
				clazz.add(psiElementFactory.createMethodFromText(getGetterText(fieldName, getterMethodName, fieldType), clazz));
			}

			String setterMethodName = "set" + methodNameSuffix;
			boolean isFinalMethod=true;
			try{
				isFinalMethod=field.getModifierList().hasModifierProperty("final");
			}catch(Exception e){
				/* swallow */
			}
			if (!isFinalMethod && !clazzMethodNames.contains(setterMethodName)) {
				clazz.add(psiElementFactory.createMethodFromText(getSetterText(fieldName, setterMethodName, fieldType, clazz.getName()), clazz));
			}
		}
	}


	protected String getGetterText(String fieldName, String methodName, String fieldType) {
		return new StringBuffer().append("public ").append(fieldType).append(" ").append(methodName).append("(){\nreturn this.").append(fieldName).append(";\n}").toString();
	}

	protected String getSetterText(String fieldName, String methodName, String fieldType, String className) {
		return new StringBuffer().
				append("public ").append(className)
				.append(" ")
				.append(methodName).append("(").append(fieldType).append(" ").append(fieldName).append("){\nthis.").append(fieldName).append("=").append(fieldName).append(";").append("return this;\n}").toString();
	}
}