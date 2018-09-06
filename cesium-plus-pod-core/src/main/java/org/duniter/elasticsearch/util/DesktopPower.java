package org.duniter.elasticsearch.util;

/*
 * #%L
 * Reef DB :: UI
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Ifremer
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

import java.util.HashSet;
import java.util.Set;

public abstract class DesktopPower {

    public interface Listener {
        /**
         * os asks to App to quit.
         * 
         * Windows machines - on reboot / logout. Mac - on reboot / logout +
         * Command + Q Linux - reboot / logout
         */
        void quit();

    }

    protected Set<Listener> listeners = new HashSet<Listener>();

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    abstract public void close();

}
