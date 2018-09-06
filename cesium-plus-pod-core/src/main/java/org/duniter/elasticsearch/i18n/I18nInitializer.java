package org.duniter.elasticsearch.i18n;

/*
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.nuiton.i18n.bundle.I18nBundle;
import org.nuiton.i18n.init.DefaultI18nInitializer;
import org.nuiton.i18n.init.UserI18nInitializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blavenie on 10/01/17.
 */
public class I18nInitializer extends org.nuiton.i18n.init.I18nInitializer{
    protected final File userDirectory;

    private String[] bundleNames;
    private String i18nPath;
    private List<UserI18nInitializer> delegates;


    public I18nInitializer(File userDirectory, String[] bundleNames) throws NullPointerException {
        this((String)null, userDirectory, bundleNames);
    }

    public I18nInitializer(String i18nPath, File userDirectory, String[] bundleNames) throws NullPointerException {
        super();

        this.i18nPath = i18nPath;
        this.bundleNames = bundleNames;
        this.userDirectory = userDirectory;
        this.delegates = createDelegates(userDirectory, bundleNames);

        if(userDirectory == null) {
            throw new NullPointerException("parameter \'userDirectory\' can not be null");
        }
    }

    public File getUserDirectory() {
        return this.userDirectory;
    }


    @Override
    public I18nBundle[] resolvBundles() throws Exception {

        List<I18nBundle> result = new ArrayList<>();
        for(DefaultI18nInitializer delegate: delegates) {
            I18nBundle[] bundles = delegate.resolvBundles();
            for(I18nBundle bundle: bundles) {
                result.add(bundle);
            }
        }

        return result.toArray(new I18nBundle[result.size()]);
    }

    /* -- private methods -- */

    private List<UserI18nInitializer> createDelegates(File userDirectory, String[] bundleNames) {
        List<UserI18nInitializer> result = new ArrayList<>();
        for(String bundleName: bundleNames) {
            UserI18nInitializer delegate = new UserI18nInitializer(userDirectory, new DefaultI18nInitializer(bundleName));
            result.add(delegate);
        }
        return result;
    }

}
