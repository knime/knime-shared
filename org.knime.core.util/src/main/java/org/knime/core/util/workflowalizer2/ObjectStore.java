package org.knime.core.util.workflowalizer2;

import java.io.File;

/**
 * @since 5.11
 */
public interface ObjectStore {

    public String saveObject(File file, String name, String path, String workflowId) throws Exception;
}