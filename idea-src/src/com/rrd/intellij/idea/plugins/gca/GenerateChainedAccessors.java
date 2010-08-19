package com.rrd.intellij.idea.plugins.gca;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.psi.PsiClass;


/**
 * An {@link com.intellij.openapi.editor.actionSystem.EditorAction} that displays
 * a "Generate Chained Accessors" option when a user is editing a Java file
 * and allows the user to generate such accessors
 *
 * @author erachitskiy
 */
public class GenerateChainedAccessors extends EditorAction {
	public GenerateChainedAccessors(){
		super(new GenerateChainedAccessorsActionHandler());
	}
	protected GenerateChainedAccessors(EditorActionHandler defaultHandler) {
		super(defaultHandler);
	}
	public void update(Editor editor, Presentation presentation, DataContext dataContext) {		
		IdeaUtil util=ApplicationManager.getApplication()
					.getComponent(IdeaUtil.class);
		/* figure out when to display the option to generate chained accessors */
		if(editor==null){
			presentation.setVisible(false);
			return;
		}
		PsiClass javaClass=util.getCurrentClass(editor);
		if(javaClass==null||javaClass.isInterface()){
			presentation.setVisible(false);
		}else{
			presentation.setVisible(true);
		}
	}
	

	


}
