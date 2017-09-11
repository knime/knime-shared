/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   05.03.2016 (thor): created
 */
package org.knime.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility methods for access host information, such as IP and MAC addresses.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 5.4
 */
public final class HostUtils {
    private static final Log LOGGER = LogFactory.getLog(HostUtils.class);

    private HostUtils() {}

    private static Set<InetAddress> ipAddresses;

    private static Set<String> macAddresses;

    private static final char[] HEX_DIGITS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String macToString(final byte[] hardwareAddress) {
        StringBuilder buf = new StringBuilder(hardwareAddress.length * 3);
        for (int j = 0; j < hardwareAddress.length; j++) {
            buf.append(HEX_DIGITS[(hardwareAddress[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[hardwareAddress[j] & 0x0f]);
            if (j < hardwareAddress.length - 1) {
                buf.append(':');
            }
        }
        return buf.toString();

    }

    private static Set<String> getMACsFromSystem() throws IOException {
        String osname = System.getProperty("os.name", "");

        Process process;
        Pattern pattern;
        if (osname.startsWith("Windows")) {
            process = Runtime.getRuntime().exec("ipconfig /all");
            pattern = Pattern.compile(": ((?:[0-9a-fA-F]{2,2}-){5,5}[0-9a-fA-F]{2,2})$");
        } else if (osname.equals("Linux") || osname.equals("Mac OS X")) {
            File ifConfig = new File("/sbin/ifconfig");
            if (!ifConfig.exists()) {
                ifConfig = new File("/bin/ifconfig");
            }
            if (!ifConfig.exists()) {
                ifConfig = new File("/usr/sbin/ifconfig");
            }
            if (!ifConfig.exists()) {
                ifConfig = new File("/usr/bin/ifconfig");
            }
            process = Runtime.getRuntime().exec(ifConfig.getAbsolutePath() + " -a", new String[]{"LANG=C"});
            pattern = Pattern.compile("(?:HWaddr|ether)\\s+((?:[0-9a-fA-F]{2,2}:){5,5}[0-9a-fA-F]{2,2})");
        } else {
            return new HashSet<String>();
        }

        Set<String> macs = new HashSet<String>();
        try (BufferedReader in =
            new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
            String line;
            while ((line = in.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    macs.add(m.group(1).toUpperCase().replace('-', ':'));
                }
            }
        }
        return macs;
    }

    private static Set<String> getMACsFromJava() throws SocketException {
        Set<String> macs = new HashSet<String>();
        for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (NetworkInterface viface : Collections.list(iface.getSubInterfaces())) {
                if (!viface.isLoopback() && (viface.getHardwareAddress() != null)) {
                    macs.add(macToString(viface.getHardwareAddress()));
                }
            }

            if (!iface.isLoopback() && (iface.getHardwareAddress() != null)
                && (iface.getHardwareAddress().length == 6)) {
                macs.add(macToString(iface.getHardwareAddress()));
            }
        }

        return macs;
    }

    /**
     * Returns all known IP addresses for this host.
     *
     * @return a set with IP addresses
     */
    public synchronized static Set<InetAddress> determineHostIPs() {
        if (ipAddresses == null) {
            Set<InetAddress> temp = new HashSet<InetAddress>();
            ipAddresses = Collections.unmodifiableSet(temp);
            try {
                for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    for (NetworkInterface viface : Collections.list(iface.getSubInterfaces())) {
                        for (InetAddress vaddr : Collections.list(viface.getInetAddresses())) {
                            if (!isLocalAddress(vaddr) && (vaddr instanceof Inet4Address)) {
                                temp.add(vaddr);
                            }
                        }
                    }

                    for (InetAddress raddr : Collections.list(iface.getInetAddresses())) {
                        if (!isLocalAddress(raddr) && (raddr instanceof Inet4Address)) {
                            temp.add(raddr);
                        }
                    }
                }
            } catch (SocketException ex) {
                LOGGER.error("Error while querying host addresses");
                LOGGER.debug("Error while querying host addresses", ex);
            }
        }

        return ipAddresses;
    }

    private static boolean isLocalAddress(final InetAddress address) {
        return address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress();
    }

    /**
     * Returns all known MAC addresses for this host.
     *
     * @return a set with MAC addresses
     */
    public synchronized static Set<String> determineHostMACs() {
        if (macAddresses == null) {
            Set<String> temp = new HashSet<String>();
            macAddresses = Collections.unmodifiableSet(temp);
            try {
                temp.addAll(getMACsFromJava());
            } catch (SocketException ex) {
                LOGGER.error("Error while querying MAC addresses");
                LOGGER.debug("Error while querying MAC addresses", ex);
            }
            try {
                temp.addAll(getMACsFromSystem());
            } catch (IOException ex) {
                LOGGER.error("Error while querying MAC addresses");
                LOGGER.debug("Error while querying MAC addresses", ex);
            }
        }

        return macAddresses;
    }

    /**
     * Checks if the string is a valid IPv4 address or at most a class A network.
     *
     * @param ip a string
     * @return <code>true</code> if it is a valid IPv4 address, <code>false</code> otherwise
     */
    public static boolean checkIP(final String ip) {
        String host;
        String mask;
        if (ip.indexOf('/') > 0) {
            host = ip.substring(0, ip.indexOf('/'));
            mask = ip.substring(ip.indexOf('/') + 1);
        } else {
            host = ip;
            mask = "255.255.255.255";
        }

        // check host ip
        String[] s = host.split("\\.");
        if (s.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            try {
                int b = Integer.parseInt(s[i]);
                if ((b > 255) || (b < 0)) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        // check netmask, we allow at most class A networks
        s = mask.split("\\.");
        if (s.length != 4) {
            return false;
        }
        try {
            int b = Integer.parseInt(s[0]);
            if (b != 255) {
                return false;
            }
            b = Integer.parseInt(s[1]);
            if ((b > 255) || (b < 0)) {
                return false;
            }
            b = Integer.parseInt(s[2]);
            if ((b > 255) || (b < 0)) {
                return false;
            }
            b = Integer.parseInt(s[3]);
            if ((b > 255) || (b < 0)) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given string is a valid MAC address.
     *
     * @param mac a string
     * @return <code>true</code> if the string is valid MAC address, <code>false</code> otherwise
     */
    public static boolean checkMAC(final String mac) {
        return mac.matches("(?:[0-9a-fA-F]{2,2}:){5,5}[0-9a-fA-F]{2,2}");
    }
}
