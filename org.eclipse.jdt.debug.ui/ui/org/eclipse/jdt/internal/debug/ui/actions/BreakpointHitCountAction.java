/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui.actions;


import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class BreakpointHitCountAction extends ObjectActionDelegate {

	private static final String INITIAL_VALUE= "1"; //$NON-NLS-1$

	/**
	 * A dialog that sets the focus to the text area.
	 */
	class HitCountDialog extends InputDialog {
		
		private boolean fHitCountEnabled;
		
		protected  HitCountDialog(Shell parentShell,
									String dialogTitle,
									String dialogMessage,
									String initialValue,
									IInputValidator validator) {
			super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		}
		
		
		/**
		 * @see Dialog#createDialogArea(Composite)
		 */
		protected Control createDialogArea(Composite parent) {
			Composite area= (Composite)super.createDialogArea(parent);
			
			final Button checkbox = new Button(area, SWT.CHECK);
			GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
			checkbox.setLayoutData(data);
			checkbox.setFont(parent.getFont());
			checkbox.setText(ActionMessages.BreakpointHitCountAction_Enable_Hit_Count_1); //$NON-NLS-1$
			checkbox.setSelection(true);
			fHitCountEnabled = true;
			checkbox.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					fHitCountEnabled = checkbox.getSelection();
					getText().setEnabled(fHitCountEnabled);
					if (fHitCountEnabled) {
						validateInput();
					} else {
						setErrorMessage(null); //$NON-NLS-1$
					}
				}
				
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			
			return area;
		}

		protected boolean isHitCountEnabled() {
			return fHitCountEnabled;
		}
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IStructuredSelection selection= getCurrentSelection();
		if (selection == null) {
			return;
		}
		Iterator itr= selection.iterator();
		if (!itr.hasNext()) {
			return;
		}

		while (itr.hasNext()) {
			IJavaBreakpoint breakpoint= (IJavaBreakpoint)itr.next();
			try {
				int oldHitCount= breakpoint.getHitCount();
				int newHitCount= hitCountDialog(breakpoint);
				if (newHitCount != -1) {					
					if (oldHitCount == newHitCount && newHitCount == 0) {
						return;
					}
					breakpoint.setHitCount(newHitCount);
				}
			} catch (CoreException ce) {
				JDIDebugUIPlugin.errorDialog(ActionMessages.BreakpointHitCountAction_Exception_occurred_attempting_to_set_hit_count_1, ce); //$NON-NLS-1$
			}
		}
	}
	
	protected int hitCountDialog(IJavaBreakpoint breakpoint) {
		String title= ActionMessages.BreakpointHitCountAction_Set_Breakpoint_Hit_Count_2; //$NON-NLS-1$
		String message= ActionMessages.BreakpointHitCountAction__Enter_the_new_hit_count_for_the_breakpoint__3; //$NON-NLS-1$
		IInputValidator validator= new IInputValidator() {
			int hitCount= -1;
			public String isValid(String value) {
				try {
					hitCount= Integer.valueOf(value.trim()).intValue();
				} catch (NumberFormatException nfe) {
					hitCount= -1;
				}
				if (hitCount < 1) {
					return ActionMessages.BreakpointHitCountAction_Value_must_be_positive_integer; //$NON-NLS-1$
				}
				//no error
				return null;
			}
		};

		int currentHitCount= 0;
		try {
			currentHitCount = breakpoint.getHitCount();
		} catch (CoreException e) {
			JDIDebugUIPlugin.log(e);
		}
		String initialValue;
		if (currentHitCount > 0) {
			initialValue= Integer.toString(currentHitCount);
		} else {
			initialValue= INITIAL_VALUE;
		}
		Shell activeShell= JDIDebugUIPlugin.getActiveWorkbenchShell();
		HitCountDialog dialog= new HitCountDialog(activeShell, title, message, initialValue, validator);
		if (dialog.open() != Window.OK) {
			return -1;
		}
		if (dialog.isHitCountEnabled()) {
			return Integer.parseInt(dialog.getValue().trim());
		}
		return 0;
	}
}
