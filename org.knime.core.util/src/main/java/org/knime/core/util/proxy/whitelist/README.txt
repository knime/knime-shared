--------------------------
Where does this come from?
--------------------------

This package was extracted and modified from https://github.com/akuhtz/proxy-vole, specifically from the path 
https://github.com/akuhtz/proxy-vole/tree/f34df91/src/main/java/com/github/markusbernhardt/proxy/selector/whitelist.
The original source code of the package is released under Apache License 2.0 (http://www.apache.org/licenses/).

---------------------------
What purpose does it serve?
---------------------------

* The proxy-vole library provides (1) "[...] search strategies to read the proxy settings from the system config [...]."
as well as (2) utilities for handling proxy whitelisting, extending the proxy capabilties of the java.net package.

* However, we only need (2) - a fraction of proxy-vole - for supporting proxies in the AP. Thus, we extract the
com.github.markusbernhardt.proxy.selector.whitelist package, including the files:
    * DefaultWhiteListParser.java
    * HostnameFilter.java
    * IPRangeFilter.java
    * IPWithSubnetChecker.java
    * WhiteListParser.java

* The files were adapted to our codebase by:
    * deletion of ProxyBypassListSelector.java and UseProxyWhiteListSelector.java
    * replacement of UriFilter by Predicate<URI>
    * usage of org.apache.commons.lang3.StringUtils for string handling
    * formatting, Javadoc, and linting-related changes
