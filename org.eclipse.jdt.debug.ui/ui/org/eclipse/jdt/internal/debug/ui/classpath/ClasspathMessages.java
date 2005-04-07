/**********************************************************************
 * Copyright (c) 2005 IBM Corporation and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.jdt.internal.debug.ui.classpath;

import org.eclipse.osgi.util.NLS;

public class ClasspathMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jdt.internal.debug.ui.classpath.ClasspathMessages";//$NON-NLS-1$
	//
	// Copyright (c) 2000, 2005 IBM Corporation and others.
	// All rights reserved. This program and the accompanying materials
	// are made available under the terms of the Eclipse Public License v1.0
	// which accompanies this distribution, and is available at
	// http://www.eclipse.org/legal/epl-v10.html
	//
	// Contributors:
	//     IBM Corporation - initial API and implementation
	//

	public static String ClasspathModel_0;
	public static String ClasspathModel_1;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, ClasspathMessages.class);
	}
}