This fragment addresses problems with CXF 4.0.1+, where OSGi
code was deliberately removed from CXF 3->4 (https://cxf.apache.org/docs/40-migration-guide.html)

It serves two different purposes:

* Registering additional extension on a CXF bus, 
** a life cycle listener to clean up cxf threads after use (https://knime-com.atlassian.net/browse/AP-20749 and CXF-8885)
** a coduit configurer setting http/1.1 transport 
* Registering a custom BusFactory using the extended class loader (note the required bundles)
  to make classes such as AsyncHTTPConduit known to the bus
