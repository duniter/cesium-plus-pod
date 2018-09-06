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


import org.duniter.elasticsearch.util.os.win.WindowsPower;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Desktop {

    private static final Logger LOG = LoggerFactory.getLogger(Desktop.class);

    static DesktopPower desktopPower = null;

    public static DesktopPower getDesktopPower() {
        if (desktopPower == null) {


            if (SystemUtils.IS_OS_WINDOWS) {
                // All Windows version are handled with WindowsPower class
                try {
                    desktopPower = new WindowsPower();
                } catch (Exception e) {
                    LOG.error(e.getLocalizedMessage(), e);
                }


            } else if (SystemUtils.IS_OS_LINUX) {

                // TODO create a Linux/UnixPower because (for example) Kubuntu sends KILL signal when it shutdown, it should sent TERM !!
            }


        }

        return desktopPower;
    }

}
