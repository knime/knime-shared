This fragment attaches to the re-packaged com.squareup.okhttp3 bundle, which packages
OkHttp 4.12.x ("annotations", "okhttp", and "okio" JARs).

It serves one purpose:

* Providing OkHttpProxyAuthenticator, a helper class that simplifies using authenticated
  HTTP proxies with OkHttp. The class originally lived in com.squareup.okhttp3 but was
  moved here (org.knime.okhttp3.OkHttpProxyAuthenticator); the re-packaging bundle is
  kept as-is to avoid breaking existing dependencies.
