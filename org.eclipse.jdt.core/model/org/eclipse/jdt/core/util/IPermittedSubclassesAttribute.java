/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.util;

/**
 * Description of permitted subclasses attribute as described in the JVM
 * specifications.
 *
 * @since 3.22 BETA_JAVA15
 */
public interface IPermittedSubclassesAttribute extends IClassFileAttribute {

	/**
	 * Answer back the number of permitted subclasses as specified in
	 * the JVM specifications.
	 *
	 * @return the number of permitted subclasses as specified in
	 * the JVM specifications
	 */
	int getNumberOfPermittedSubclasses();

	/**
	 * Answer back the array of permitted subclass attribute entries as specified in
	 * the JVM specifications, or an empty array if none.
	 *
	 * @return the array of permitted subclass attribute entries as specified in
	 * the JVM specifications, or an empty array if none
	 */
	IPermittedSubclassesAttributeEntry[] getPermittedSubclassAttributesEntries();
}
