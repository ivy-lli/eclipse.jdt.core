/*******************************************************************************
 * Copyright (c) 2007, 2023 BEA Systems, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    wharley@bea.com - initial API and implementation
 *    IBM Corporation - fix for 342598
 *******************************************************************************/

package org.eclipse.jdt.internal.compiler.apt.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileManager;

import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.apt.model.ElementsImpl;
import org.eclipse.jdt.internal.compiler.apt.model.Factory;
import org.eclipse.jdt.internal.compiler.apt.model.TypesImpl;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.ModuleBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;

/**
 * Implementation of ProcessingEnvironment that is common to batch and IDE environments.
 */
public abstract class BaseProcessingEnvImpl implements ProcessingEnvironment {

	// Initialized in subclasses:
	protected Filer _filer;
	protected Messager _messager;
	protected Map<String, String> _processorOptions;
	protected Compiler _compiler;

	// Initialized in this base class:
	protected Elements _elementUtils;
	protected Types _typeUtils;
	private List<ICompilationUnit> _addedUnits;
	private List<ReferenceBinding> _addedClassFiles;
	private List<ICompilationUnit> _deletedUnits;
	private boolean _errorRaised;
	private Factory _factory;
	public ModuleBinding _current_module;

	public BaseProcessingEnvImpl() {
		this._addedUnits = new ArrayList<>();
		this._addedClassFiles = new ArrayList<>();
		this._deletedUnits = new ArrayList<>();
		this._elementUtils = ElementsImpl.create(this);
		this._typeUtils = new TypesImpl(this);
		this._factory = new Factory(this);
		this._errorRaised = false;
	}

	public void addNewUnit(ICompilationUnit unit) {
		this._addedUnits.add(unit);
	}

	public void addNewClassFile(ReferenceBinding binding) {
		this._addedClassFiles.add(binding);
	}

	public Compiler getCompiler() {
		return this._compiler;
	}

	public ICompilationUnit[] getDeletedUnits() {
		ICompilationUnit[] result = new ICompilationUnit[this._deletedUnits.size()];
		this._deletedUnits.toArray(result);
		return result;
	}

	public ICompilationUnit[] getNewUnits() {
		ICompilationUnit[] result = new ICompilationUnit[this._addedUnits.size()];
		this._addedUnits.toArray(result);
		return result;
	}

	@Override
	public Elements getElementUtils() {
		return this._elementUtils;
	}

	@Override
	public Filer getFiler() {
		return this._filer;
	}

	@Override
	public Messager getMessager() {
		return this._messager;
	}

	@Override
	public Map<String, String> getOptions() {
		return this._processorOptions;
	}

	@Override
	public Types getTypeUtils() {
		return this._typeUtils;
	}

	public LookupEnvironment getLookupEnvironment() {
		return this._compiler.lookupEnvironment;
	}

	@Override
	public SourceVersion getSourceVersion() {
		if (this._compiler.options.sourceLevel <= ClassFileConstants.JDK1_5) {
			return SourceVersion.RELEASE_5;
		}
		if (this._compiler.options.sourceLevel == ClassFileConstants.JDK1_6) {
			return SourceVersion.RELEASE_6;
		}
		try {
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK1_7) {
				return SourceVersion.valueOf("RELEASE_7"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK1_8) {
				return SourceVersion.valueOf("RELEASE_8"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK9) {
				return SourceVersion.valueOf("RELEASE_9"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK10) {
				return SourceVersion.valueOf("RELEASE_10"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK11) {
				return SourceVersion.valueOf("RELEASE_11"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK12) {
				return SourceVersion.valueOf("RELEASE_12"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK13) {
				return SourceVersion.valueOf("RELEASE_13"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK14) {
				return SourceVersion.valueOf("RELEASE_14"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK15) {
				return SourceVersion.valueOf("RELEASE_15"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK16) {
				return SourceVersion.valueOf("RELEASE_16"); //$NON-NLS-1$
			}
			if (this._compiler.options.sourceLevel == ClassFileConstants.JDK17) {
				return SourceVersion.valueOf("RELEASE_17"); //$NON-NLS-1$
			}
		} catch(IllegalArgumentException e) {
			// handle call on a JDK 6
			return SourceVersion.RELEASE_6;
		}
		// handle call on a JDK 6 by default
		return SourceVersion.RELEASE_6;
	}

	/**
	 * Called when AnnotationProcessorManager has retrieved the list of
	 * newly generated compilation units (ie, once per round)
	 */
	public void reset() {
		this._addedUnits.clear();
		this._addedClassFiles.clear();
		this._deletedUnits.clear();
	}

	/**
	 * Has an error been raised in any of the rounds of processing in this build?
	 * @return error flag
	 */
	public boolean errorRaised()
	{
		return this._errorRaised;
	}

	/**
	 * Set or clear the errorRaised flag.  Typically this will be set by the Messager
	 * when an error has been raised, and it will never be cleared.
	 */
	public void setErrorRaised(boolean b)
	{
		this._errorRaised = true;
	}

	public Factory getFactory()
	{
		return this._factory;
	}

	public ReferenceBinding[] getNewClassFiles() {
		ReferenceBinding[] result = new ReferenceBinding[this._addedClassFiles.size()];
		this._addedClassFiles.toArray(result);
		return result;
	}
	@Override
    public boolean isPreviewEnabled() {
        return this._compiler.options.enablePreviewFeatures;
    }

	public JavaFileManager getFileManager() {
		return null;
	}

}
